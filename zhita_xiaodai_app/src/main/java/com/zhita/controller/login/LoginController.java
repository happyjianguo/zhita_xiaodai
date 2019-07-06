package com.zhita.controller.login;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import com.zhita.model.manage.User;
import com.zhita.service.manage.login.IntLoginService;
import com.zhita.service.manage.source.IntSourceService;
import com.zhita.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/app_login")
public class LoginController {
    @Autowired
    IntLoginService loginService;
    
    @Autowired
    IntSourceService intSourceService;


    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

//    // 发送验证码
//    @RequestMapping("/sendSMS")
//    @ResponseBody
//    public Map<String, String> sendSMS(String phone, String company, HttpServletRequest request) {
//        String currentIp = getIpAddress(request);
//        RedisClientUtil redis = new RedisClientUtil();
//        Map<String, String> map = new HashMap<>();
//        if (null == redis.getSourceClick(currentIp)) {
//            SMSUtil smsUtil = new SMSUtil();
//            String state = smsUtil.sendSMS(phone, "json", company);
//            if (state.equals("提交成功")) {
//                redis.set(currentIp, "1", 3600);
//            }
//            map.put("msg", state);
//            return map;
//        } else {
//            map.put("msg", "发送失败");
//            return map;
//        }
//
//    }

    @RequestMapping("/sendShortMessage")
    @ResponseBody
    public Map<String, String> sendShortMessage(String phone, int companyId, String appNumber, String code) {
    	String company ="";
    	if (companyId==1) {
    	 company = "借吧";
    	}
        Map<String, String> map = new HashMap<>();
        DateFormat format = new SimpleDateFormat("yyyy/M/d");
        String result = MD5Utils.getMD5(phone + appNumber + format.format(new Date()) + "@xiaodai");
        if (result.length() == 31) {
            result = 0 + MD5Utils.getMD5(phone + appNumber + format.format(new Date()) + "@xiaodai");
        }
        if (result.equals(code)) {
            SMSUtil smsUtil = new SMSUtil();
            String state = smsUtil.sendSMS(phone, "json", company);
            map.put("msg", state);
            return map;
        } else {
            map.put("msg", "发送失败");
            return map;
        }

    }


    // 验证码登陆

    /**
     * @param phone            手机号
     * @param code             验证码
     * @param company          公司名
     * @param registeClient 软件类型
     * @return
     */
    @RequestMapping("/codelogin")
    @ResponseBody
    @Transactional
    public Map<String, Object> codeLogin(String phone, String code, int companyId, String registeClient, String sourceName, String useMarket) {
        Map<String, Object> map = new HashMap<String, Object>();
        String loginStatus = "1";
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)|| StringUtils.isEmpty(companyId)|| StringUtils.isEmpty(registeClient)|| StringUtils.isEmpty(sourceName)|| StringUtils.isEmpty(useMarket)) {
            map.put("msg", "phone,code,companyId,registrationType,sourceName和useMarket不能为空");
            return map;
        } else {
//			int num1 = sourceDadSonService.getSourceDadSon(sourceId,sonSourceName,company);
//			if (num1 == 0) {
//			sourceDadSonService.setSourceDadSon(sourceId,sonSourceName,company);
//			}
            PhoneDeal phoneDeal = new PhoneDeal();
            String newPhone = phoneDeal.encryption(phone);
            RedisClientUtil redisClientUtil = new RedisClientUtil();
            String key = phone + "xiaodaiKey";
            String redisCode = redisClientUtil.get(key);
            if (redisCode == null) {
                map.put("msg", "验证码已过期，请重新发送");
                map.put("SCode", "402");
                return map;
            }
            if (redisCode.equals(code)) {
                redisClientUtil.delkey(key);// 验证码正确就从redis里删除这个key
                String registrationTime = System.currentTimeMillis() + "";  //获取当前时间戳
                User user = loginService.findphone(newPhone, companyId); // 判断该用户是否存在
                if (user == null) {
                    int merchantId = intSourceService.getsourceId(sourceName);
                    int number = loginService.insertUser1(newPhone, loginStatus, companyId, registeClient, registrationTime, merchantId, useMarket);
                    if (number == 1) {
                        int id = loginService.getId(newPhone, companyId); //获取该用户的id
                        map.put("msg", "用户登录成功，数据插入成功，让用户添加密码");
                        map.put("SCode", "201");
                        map.put("loginStatus", loginStatus);
                        map.put("userId", id);
                        map.put("phone", phone);
                    } else {
                        map.put("msg", "用户登录失败，用户数据插入失败");
                        map.put("SCode", "405");
                    }
                } else {
                    String loginTime = System.currentTimeMillis() + "";
                    int num = loginService.updateStatus(loginStatus, newPhone, companyId, loginTime);
                    if (num == 1) {
                        int id = loginService.getId(newPhone, companyId); // 获取该用户的id
                        String pwd = loginService.getPwd(id);
                        if (pwd == null) {
                            map.put("msg", "用户登录成功，登录状态修改成功，让用户添加密码");
                            map.put("SCode", "201");
                            map.put("loginStatus", loginStatus);
                            map.put("userId", id);
                            map.put("phone", phone);
                        } else {
                            map.put("msg", "用户登录成功，登录状态修改成功");
                            map.put("SCode", "200");
                            map.put("loginStatus", loginStatus);
                            map.put("userId", id);
                            map.put("phone", phone);
                        }
                    } else {
                        map.put("msg", "用户登录失败，登录状态修改失败");
                        map.put("SCode", "406");
                    }
                }
            } else {
                map.put("msg", "验证码错误");
                map.put("SCode", "403");
                return map;
            }

            return map;
        }

    }

   

    // 退出登录

    /**
     * @return
     */
    @RequestMapping("/logOut")
    @ResponseBody
    @Transactional
    public Map<String, String> appLogOut(int userId, String companyId) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(userId)) {
            map.put("msg", "userId不能为空");
            return map;
        } else {
            String loginStatus = "0";
            int number = loginService.updatelogOutStatus(loginStatus, userId, companyId);
            if (number == 1) {
                map.put("msg", "用户退出成功，登录状态修改成功");
                map.put("SCode", "200");
                map.put("loginStatus", loginStatus);
            } else {
                map.put("msg", "用户退出失败，登录状态修改失败");
                map.put("SCode", "400");
            }
        }

        return map;

    }

}

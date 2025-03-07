package com.zhita.service.manage.source;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import com.zhita.model.manage.Source;
import com.zhita.model.manage.TongjiSorce;
import com.zhita.model.manage.User;

public interface IntSourceService {
	
	//后台管理---查询渠道表所有信息
    public Map<String,Object> queryAll(Integer comapnyId,Integer page);
    
    //后台管理---添加功能（查询出所有公司   模板和风控）
    public Map<String, Object> queryAllCompany(Integer companyId);
    
    //后台管理---添加功能
    public int insert(Source record);
    
    //后台管理---根据id查询当前对象信息
    public Map<String,Object> selectByPrimaryKey(Integer companyId,Integer id);
    
    //后台管理---根据id查询
    public Source selectByid(Integer id);
    
    //后台管理---编辑功能
    public int updateByPrimaryKey(Source record)throws ParseException;
    
    //后台管理---根据id  对当前对象的假删除状态进行修改
    public int updateFalDel(Integer id);
    
    //后台管理---查询当天各个渠道在用户表的注册数量
    public List<TongjiSorce> queryAllSourceByUser(Integer companyid,String StartTime,String EndTime);
    
    //后台管理---查询当天各个渠道在用户表的注册数量(通过渠道查询)
    public List<TongjiSorce> queryAllSourceBySouce(Integer companyid,String StartTime,String EndTime,Integer sourceid);
    
    //后台管理---查询某一天某个渠道的注册数量
    public TongjiSorce queryAllSourceByUserDetail(Integer companyid,String StartTime,String EndTime,Integer sourceid);
    
    //后台管理---查询user表所有的注册时间
  	public List<String> qeuryAllUserRegistetime(Integer companyId);
    
    //定时任务
    //后台管理----做定时任务需要执行的方法（每日0点  将各个渠道的历史数据存入历史表）
    public void selAllTongji() throws ParseException;
    
    //后台管理----通过渠道名字查询当前渠道在历史表的信息
    public List<TongjiSorce> queryAllBySourceName(Integer sourceName);
    
    //后台管理 ------查询统计申请数 
    public int queryApplicationNumber(Integer companyId,Integer sourceName,String startTime,String endTime);
    
    //后台管理---通过渠道名称查询出当前渠道的折扣率
    public String queryDiscount(Integer sourceName,Integer companyId);


	public int getsourceId(String sourceName);

    
    //后台管理---通过渠道和时间查询在历史表是否有数据
    public TongjiSorce queryBySourcenameAndDate(Integer sourcename,String startdate,String enddate);
    
    //后台管理---查询当前渠道下有多少用户是登录过得
    public int queryCount(Integer sourceid,String startTime,String endTime);
    //后台管理---当前渠道下所有的用户id
    public List<Integer> queryUserid(Integer sourceid);
    //后台管理---查询当前用户id是否在个人信息认证表有值
    public int queryIfExist(Integer userid,String startTime,String endTime);
    //后台管理---查询当前用户id是否在银行卡表有值
    public int queryIfExist1(Integer userid,String startTime,String endTime);
    //后台管理---查询当前用户id是否在运营商表有值
    public int queryIfExist2(Integer userid,String startTime,String endTime);
  	//后台管理---渠道统计模块——申请人数字段
  	public int queryNum(Integer companyId,Integer sourceid,String starttime,String endtime);
  	//后台管理---查询当前渠道所使用的风控       机审风控分数段的值
    public String querymancon(String sourceName);
   //后台管理---渠道统计模块——机审通过字段
  	public int queryNum1(Integer companyId,String sourcename,String startscore,String endscore);

	public int getmanageControlId(String sourceName);
  	
  	//后台管理---查询出source表所有的信息
  	public List<Source> querysource(Integer companyId);
  	
	//后台管理----通过人数（包含机审通过和人审通过）
	public int  querypass(Integer sourceid,String starttime,String endtime);
	
	//后台管理----已借款人数
	public int queryorderpass(Integer sourceid,String starttime,String endtime);

	public List<String> getstateAndDeleted(int companyId, String sourceName);

	public List<String> getDeleted(int companyId, String sourceName);

	public int queryByLike1(String source, int companyId);

	public String getsourceName(int sourceId);

	//后台管理----根据前端传过来的链接判断该链接是否存在source表
	public int queryIfLink(String link);
	
	//后台管理----渠道方登录（用户名和密码）
	public Source queryByAccAndPwd(String account);
	
	//后台管理---根据公司id查询公司名字
	public String querycompany(Integer companyid);
	
	//后台管理---查询所有渠道
    public List<TongjiSorce> queryAllSource(Integer companyId);
    
    //后台管理---查询所有渠道  根据渠道查询
    public List<TongjiSorce> queryAllSourceBysourceid(Integer companyId,Integer sourceid);

	public String getstatus(int companyId, String sourceName);

}

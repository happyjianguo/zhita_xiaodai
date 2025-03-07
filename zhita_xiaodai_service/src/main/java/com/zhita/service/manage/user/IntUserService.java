package com.zhita.service.manage.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhita.model.manage.OrderQueryParameter;
import com.zhita.model.manage.Orders;
import com.zhita.model.manage.User;
import com.zhita.model.manage.UserLikeParameter;

public interface IntUserService {
	//后台管理----用户列表(公司id，page,姓名，手机号，注册开始时间，注册结束时间，用户认证状态，银行卡认证状态，运营商认证状态)
	public Map<String, Object> queryUserList(UserLikeParameter userLikeParameter);
	
	//后台管理----用户列表(公司id，page,pagesize,姓名，手机号，注册开始时间，注册结束时间，用户认证状态，银行卡认证状态，运营商认证状态)——查询数量
	public int queryUserListcount(UserLikeParameter userLikeParameter);
		
	//后台管理----用户列表（导出Excel）
	public List<User> queryUserListExcel(UserLikeParameter userLikeParameter);
	
	//后台管理---添加黑名单——人工添加
	public int insertBlacklist(Integer companyId,Integer userId,Integer operator);
	
	//后台管理---添加黑名单——人审拒绝
	public int insertBlacklistno(Integer companyId,Integer userId,Integer operator);
	
	//后台管理---解除黑名单
	public int removeBlacklist(Integer companyId,Integer userId);
	
	//后台管理----订单 查询（公司id，page,订单号，姓名，手机号，注册开始时间，注册结束时间     渠道id  用户id）
  	public Map<String,Object> queryAllOrdersByUserid(OrderQueryParameter orderQueryParameter);
  	
	//后台管理----订单 查询（公司id  page，姓名，手机号，身份证号，注册开始时间，注册结束时间     渠道id）
  	public Map<String, Object> queryAllOrdersByUserid1(OrderQueryParameter orderQueryParameter);
  	
  	//后台管理----订单 查询（公司id，姓名，手机号，身份证号，注册开始时间，注册结束时间     渠道id）——查询数量
  	public int queryAllOrdersByUserid1count(OrderQueryParameter orderQueryParameter);
  	
  	//后台管理----订单 查询（导出Excel）
  	public List<Orders> queryAllOrdersByUserid1Excel(OrderQueryParameter orderQueryParameter);
  	
	//后台管理---用户认证信息
	public Map<String,Object> queryUserAttesta(Integer userid);

	public void updateScore(int score, int userId, String shareOfState);

	public Integer getRiskControlPoints(int userId);

	public String getshareOfState(int userId);

	public int getdelayTimes(int userId);

	public void updateshareOfState(int userId, String shareOfState);

	public void setuser(int userId, String timStamp, String applynumber);

	public void updatename(String name, int userId);

	public void updateifBlacklist(int userId);

	public String getphone(int id);

	public void updateCanBorrowLines(BigDecimal finalLine, int userId);

	public BigDecimal getcanBorrowLines(Integer userId);

	public int getsourceId(int userId);

	public void updateScore1(int userId, String shareOfState);

	public String getifBlacklist2(int userId);

	public String getapplynumber(int userId);

	public void updateuser(int userId, String timStamp, String applynumber);

	public void updateapplyState(String applyState, int userId);

	public String getapplyState(int userId);

	public String getregisteTime(int userId);

	public void setRiskControlPoints(int userId, int riskControlPoints);

	public void setModel(int userId, String rString);

	public String getModel(int userId);





}

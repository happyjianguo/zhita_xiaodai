package com.zhita.controller.order;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhita.model.manage.OrderQueryParameter;
import com.zhita.service.manage.order.IntOrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private IntOrderService intOrderService;
	
	//后台管理----机审订单     (公司id，page，订单号，订单开始时间，订单结束时间，风控反馈)
	@ResponseBody
	@RequestMapping("/queryatrOrders")
	public Map<String, Object> queryatrOrders(OrderQueryParameter orderQueryParameter){
		System.out.println("controller:"+orderQueryParameter);
		Map<String, Object> map=intOrderService.queryatrOrders(orderQueryParameter);
		return map;
	}
	
	//后台管理----机审拒绝未人审订单     (公司id，page，订单号，订单开始时间，订单结束时间)
	@ResponseBody
	@RequestMapping("/queryroaOrders")
	public Map<String, Object> queryroaOrders(OrderQueryParameter orderQueryParameter){
		System.out.println("controller:"+orderQueryParameter);
		Map<String, Object> map=intOrderService.queryroaOrders(orderQueryParameter);
		return map;
	}
	
	//后台管理----机审拒绝未人审订单——审核通过操作
	@ResponseBody
	@RequestMapping("/upaOrderIfpeopleWhose")
  	public int upaOrderIfpeopleWhose(Integer orderid,String assessor){
  		int num=intOrderService.upaOrderIfpeopleWhose(orderid, assessor);
  		return num;
  	}
	
	//后台管理----已机审已人审（公司id，page,订单号，订单开始时间，订单结束时间      审核员    风控名字       风控分数开始   风控分数结束）
	@ResponseBody
	@RequestMapping("/queryroasOrders")
  	public Map<String, Object> queryroasOrders(OrderQueryParameter orderQueryParameter){
  		Map<String, Object> map=intOrderService.queryroasOrders(orderQueryParameter);
  		return map;
  	}
	
	//后台管理----订单 查询（公司id，page,订单号，订单开始时间，订单结束时间     渠道id）
	@ResponseBody
	@RequestMapping("/queryAllOrders")
  	public Map<String, Object> queryAllOrders(OrderQueryParameter orderQueryParameter){
		Map<String, Object> map=intOrderService.queryAllOrders(orderQueryParameter);
		return map;
	}
}

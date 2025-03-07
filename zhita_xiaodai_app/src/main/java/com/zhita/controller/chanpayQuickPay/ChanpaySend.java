package com.zhita.controller.chanpayQuickPay;





import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zhita.chanpayutil.BaseConstant;
import com.zhita.chanpayutil.BaseParameter;
import com.zhita.chanpayutil.ChanPayUtil;
import com.zhita.dao.manage.OrderdetailsMapper;
import com.zhita.model.manage.Bankcard;
import com.zhita.model.manage.Loan_setting;
import com.zhita.model.manage.Orderdetails;
import com.zhita.model.manage.Payment_record;
import com.zhita.service.manage.borrowmoneymessage.IntBorrowmonmesService;
import com.zhita.service.manage.chanpayQuickPay.Chanpayservice;
import com.zhita.service.manage.order.IntOrderService;
import com.zhita.service.manage.user.IntUserService;
import com.zhita.util.RedisClientUtil;
import com.zhita.util.Timestamps;




@Controller
@RequestMapping("sendPara")
public class ChanpaySend extends BaseParameter{
		
		@Autowired
		private Chanpayservice chanser;
		
		
		@Autowired
		IntOrderService intOrderService;
		
		
		@Autowired
		IntUserService intUserService;
		
		
		
		@Autowired
	    OrderdetailsMapper orderdetailsMapper;
		
		
		
		@Autowired
		IntBorrowmonmesService intBorrowmonmesService;
		
		
		
		
	
		
	
	
	/**
	 * 放款
	 * @param orderNumber
	 * @param AcctNo
	 * @param AcctName
	 * @param TransAmt
	 * @return
	 * 
	 *  * String registeClient,
	 * String sourceName
	 *BigDecimal shouldTotalAmount
	 *BigDecimal totalInterest,
	 *BigDecimal averageDailyInterest
	 */
	@ResponseBody
	@RequestMapping("send")
	@Transactional
	public Map<String, Object> SendMoney(Integer userId,String TransAmt,Integer companyId,int lifeOfLoan,BigDecimal finalLine,String registeClient,String sourceName,
			BigDecimal shouldTotalAmount,BigDecimal totalInterest,BigDecimal averageDailyInterest){
		System.out.println("金额类:"+finalLine+"基恩:"+shouldTotalAmount+"CC:");
		
		
		SimpleDateFormat sin = new SimpleDateFormat("yyyy-MM-dd");
		String time = sin.format(new Date());
		RedisClientUtil redis = new RedisClientUtil();
		Loan_setting loan = new Loan_setting();
		loan.setCompanyId(companyId);
		loan.setName("畅捷支付");
		String a =  chanser.loanSetStatu(loan);//放款状态  1  开启    2 关闭
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = intBorrowmonmesService.getborrowMoneyMessage(companyId); 
		Integer id = chanser.SelectOrdersId(userId);
		String borrowingScheme = chanser.SelectBorrowing(companyId);
		BigDecimal actualAmountReceived = null;
		BigDecimal acmoney = null;
		Integer j = null;
		int platformfeeRatio =  ((int) map2.get("platformfeeRatio"));//平台服务费比率
		Double pladata = platformfeeRatio*0.01;
		BigDecimal pr = new BigDecimal(0);
		pr=BigDecimal.valueOf((Double)pladata);//平台服务费比率
		BigDecimal platformServiceFee = (finalLine.multiply(pr)).setScale(2,BigDecimal.ROUND_HALF_UP);//平台服务费
		
		
		
		
		if(borrowingScheme.equals("2")){
			BigDecimal sa = new BigDecimal(TransAmt);
			j = shouldTotalAmount.compareTo(sa);
			}
		actualAmountReceived = new BigDecimal(TransAmt); //实际到账金额
		acmoney = new BigDecimal(TransAmt);
		j = actualAmountReceived.compareTo(acmoney);
		System.out.println(j+"金额:"+acmoney+"实际到账:"+actualAmountReceived);
			
			int borrowNumber = intOrderService.borrowNumber(userId,companyId); //用户还款次数
			Bankcard ba = new Bankcard();
			ba.setCompanyId(companyId);
			ba.setUserId(userId);
			Bankcard ban = chanser.SelectBank(ba);
			System.out.println("数据:"+ban.getTiedCardPhone() + ban.getBankcardName() + ban.getCstmrnm() + ban.getBankcardTypeName());
			Calendar now = Calendar.getInstance(); 
	    	String year = now.get(Calendar.YEAR)+""; //年
	    	String month = now.get(Calendar.MONTH) + 1 + "";//月
	    	if(Integer.parseInt(month)<10) {
	    		month = "0"+Integer.parseInt(month);
	    	}
	    	String day = now.get(Calendar.DAY_OF_MONTH)+"";//日
	    	String hour = now.get(Calendar.HOUR_OF_DAY)+"";//时
	    	String minute = now.get(Calendar.MINUTE)+"";//分
	    	String second = now.get(Calendar.SECOND)+"";//秒
	    	String afterFour = ban.getTiedCardPhone().substring(ban.getTiedCardPhone().length()-4); 
	    	String orderNumber ="DD_"+year+month+day+hour+minute+second+afterFour+"0"+(lifeOfLoan+"")+((borrowNumber+1)+"");//订单编号
	    	
	    	
		try {
			
		
		String chanpaysenduserid = redis.get("ChanpaySenduserId"+userId);
		if(chanpaysenduserid != null){
			map1.put("code", "205");
			map1.put("Ncode", 0);
			map1.put("msg", "系统错误,无法借款");
			return map1;
		}else{
		
		if(j==0 || j==1){//j = 0 证明 actualAmountReceived == acmoney j = 1 actualAmountReceived > acmoney
			
		if(id == null){
			
		
		if(a.equals("1")){
		Orderdetails ord = new Orderdetails();
		
		
		ord.setCompanyId(companyId);
		try {
			ord.setStart_time(Timestamps.dateToStamp1(time+" 00:00:00"));
			ord.setEnd_time(Timestamps.dateToStamp1(time+" 23:59:59"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Integer maxmoney = chanser.loanMaxMoney(companyId);//获取限额
		BigDecimal SumPaymoney = chanser.SumpayMoney(ord);//当天放款额度
		BigDecimal maxMon = new BigDecimal(maxmoney);
		
		if(SumPaymoney==null){
			SumPaymoney = new BigDecimal(0);
		}
		
		
		if(maxmoney != 0){
			Integer i = SumPaymoney.compareTo(maxMon);//i == -1 sumPaymoney 小于 maxMon   0  sumPaymoney 相等 maxMon   1  sumPaymoney 大于 maxMon
			if(i == 1 || i == 0){
				map1.put("msg", "今日放款已达限额,请明日再来");
				map1.put("Ncode", 0);
				map1.put("code", 0);
				return map1;
			}
		}
		
		map1.put("Ncode", 2000);
		if(ban.getTiedCardPhone() != null && ban.getBankcardName() != null && ban.getCstmrnm() != null && ban.getBankcardTypeName() != null
				&& ban.getTiedCardPhone() != "" && ban.getBankcardName() != "" && ban.getCstmrnm() != "" && ban.getBankcardTypeName() != ""){
		
    	
    	
		if(userId != null && TransAmt != null && companyId != null && lifeOfLoan != 0){
		
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, +1);
		date = calendar.getTime();
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
		
		int borrowNumberR = intOrderService.borrowNumber(userId,companyId); //用户还款次数
	    int	howManyTimesBorMoney = borrowNumberR+1;//第几次借款
	    String orderCreateTime = String.valueOf(System.currentTimeMillis());//订单生成时间戳
    	Integer riskmanagementFraction = 0;
    	Integer asc = intUserService.getRiskControlPoints(userId);//获取风控分数
    	if(asc != null){
    		riskmanagementFraction=asc;
    	}
    	String shouldReturned = getShouldReturned(lifeOfLoan-1);//应还日时间戳,因为借款当天也算一天，所以要减去一天
    	String borrowMoneyWay = "立即贷";//贷款方式
    	
    	int num = 0;
		try {
			num = intOrderService.setOrder(companyId,userId,orderNumber,orderCreateTime,lifeOfLoan,howManyTimesBorMoney,shouldReturned,riskmanagementFraction,borrowMoneyWay);
		} catch (Exception e) {
			redis.set("ChanpaySenduserId"+userId, String.valueOf(userId));
			map1.put("code", "203");
			map1.put("desc", "已放款,未保存");
			map1.put("Ncode", 0);
			return map1;
		}
		actualAmountReceived = new BigDecimal(TransAmt);
    	if(num==1) {
    		int orderId = intOrderService.getOrderId(orderNumber);
	    	BigDecimal surplus_money = finalLine;
    		num = orderdetailsMapper.setororderdetails(orderId,finalLine,averageDailyInterest,totalInterest,platformServiceFee,actualAmountReceived,
	    			registeClient,sourceName,shouldTotalAmount,surplus_money);
	    	if(num==1) {
				map1.put("code", 200);
				map1.put("Ncode", 2000);
				redis.delkey("ChanpaySenduserId"+userId);//删除字段
	    		map1.put("desc","订单已生成");
	    		map1.put("msg","订单生成成功");
    	
		    	Map<String, String> map = this.requestBaseParameter();
				map.put("TransCode", "T10000"); // 交易码
				map.put("OutTradeNo", orderNumber); // 商户网站唯一订单号
				map.put("CorpAcctNo", "");  //可空
				map.put("BusinessType", "0"); // 业务类型：0对私 1对公
				map.put("BankCommonName", ban.getBankcardTypeName()); // 通用银行名称
				map.put("BankCode", "");//对公必填
				map.put("AccountType", "00"); // 账户类型
				map.put("AcctNo", ChanPayUtil.encrypt(ban.getBankcardName(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET)); // 对手人账号(此处需要用真实的账号信息)
				map.put("AcctName", ChanPayUtil.encrypt(ban.getCstmrnm(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET)); // 对手人账户名称
				map.put("TransAmt", TransAmt);
				map.put("CorpPushUrl", "http://172.20.11.16");		
				map.put("PostScript", "放款");
				String rea = ChanPayUtil.sendPost(map, BaseConstant.CHARSET,
						BaseConstant.MERCHANT_PRIVATE_KEY);
				ReturnSend returnchanpay = JSON.parseObject(rea, ReturnSend.class);
    	
    	
    	
    	
    	/*                           获取银行卡信息         */
    	Bankcard bas = new Bankcard();
    	bas.setCompanyId(companyId);
		bas.setUserId(userId);
		Bankcard banAA = chanser.SelectBank(bas);
		
		Payment_record pay = new Payment_record();
		pay.setUserId(userId);
		pay.setPipelinenumber(returnchanpay.getPartnerId());
		pay.setDeleted("0");
		pay.setPaymentmoney(new BigDecimal(TransAmt));
		pay.setCardnumber(ban.getBankcardName());
		
		String statu = returnchanpay.getAcceptStatus();
		if(statu.equals("S")){
			
			pay.setStatus("支付成功");
			String pipelnen = "lsn_"+returnchanpay.getTradeDate()+returnchanpay.getTradeTime();
			pay.setPipelinenumber(pipelnen);
			pay.setOrderId(orderId);
			chanser.AddPayment_record(pay);
			map1.put("ShortReturn", returnchanpay);
			map1.put("code", 200);
			map1.put("msg", "放款成功");
			map1.put("Ncode", 2000);
			map1.put("desc", "借款成功");
		}else{
			String orderStatus = returnchanpay.getRetMsg();
			pay.setStatus("支付失败");
			chanser.AddPayment_record(pay);
			chanser.DeleteOrderNumber(orderNumber,orderStatus);
			map1.put("ShortReturn", returnchanpay);
			map1.put("code", 0);
			map1.put("msg", orderStatus);
			map1.put("Ncode", 0);
			map1.put("desc", "借款失败");
			map1.put("code", 0);
		}
		
	    }else {
	    		redis.delkey("ChanpaySenduserId"+userId);//删除字段
	    		map1.put("Ncode", 0);
				map1.put("code",405); 
				map1.put("desc","订单已生成");
	    		map1.put("msg","订单生成失败");
				
			}
    	}
		
		}else{
			map1.put("msg", "userId,TransAmt,companyId,lifeOfLoan不能为空");
			map1.put("code", 406);
			map1.put("Ncode", 0);
		}
		}else{
			map1.put("msg", "数据异常");
			map1.put("code", 402);
			map1.put("Ncode", 0);
		}
		}else{
			map1.put("msg", "渠道关闭,请联系客服");
			map1.put("code", 407);
			map1.put("Ncode", 0);
		}
		}else{
			map1.put("msg", "您有订单未还清");
			map1.put("code", 403);
			map1.put("Ncode", 0);
		}
		}else{
			map1.put("msg", "金额异常");
			map1.put("code", 401);
			map1.put("Ncode", 0);
		}
		}
		} catch (Exception e) {
			redis.set("ChanpaySenduserId"+userId, String.valueOf(userId));
			map1.put("code", "203");
			map1.put("desc", "已放款,未保存");
			map1.put("msg", "已放款,未保存");
			map1.put("Ncode", 0);
			return map1;
		}
		return map1;
		
	}
	
	
	
	
	/**
	 * String OriOutTradeNo
	 * String BeginDate	开始时间  例子 20190726
	 * String EndDate	结束时间  例子 20190727
	 * @param OriOutTradeNo
	 * @param BeginDate
	 * @param EndDate
	 * orderNumber  订单编号
	 * 
	 * String registeClient,
	 * String sourceName
	 *BigDecimal shouldTotalAmount
	 *BigDecimal totalInterest,
	 *BigDecimal averageDailyInterest
	 * @return
	 */
	@ResponseBody
	@RequestMapping("Mingxi")
	public Map<String, Object> SendMing(String orderNumber,int lifeOfLoan,String sourceName,String registeClient,
			Integer userId,Integer companyId,BigDecimal finalLine,BigDecimal averageDailyInterest,BigDecimal totalInterest,
			BigDecimal platformServiceFee,BigDecimal actualAmountReceived,BigDecimal shouldTotalAmount) {
		int borrowNumberR = intOrderService.borrowNumber(userId,companyId); //用户还款次数
	    int	howManyTimesBorMoney = borrowNumberR+1;//第几次借款
	    String orderCreateTime = String.valueOf(System.currentTimeMillis());//订单生成时间戳
	    Map<String, String> map = this.requestBaseParameter();
		Map<String, Object> map1 = new HashMap<String, Object>();
    	Integer riskmanagementFraction = 0;
    	Integer asc = intUserService.getRiskControlPoints(userId);//获取风控分数
    	if(asc != null){
    		riskmanagementFraction=asc;
    	}
    	String shouldReturned = getShouldReturned(lifeOfLoan-1);//应还日时间戳,因为借款当天也算一天，所以要减去一天
    	String borrowMoneyWay = "立即贷";//贷款方式
    	
    	
    	/*                           获取银行卡信息         */
    	Bankcard bas = new Bankcard();
    	bas.setCompanyId(companyId);
		bas.setUserId(userId);
		Bankcard banAA = chanser.SelectBank(bas);
		System.out.println("数据:"+banAA.getTiedCardPhone() + banAA.getBankcardName() + banAA.getCstmrnm() + banAA.getBankcardTypeName());
		
		Payment_record pay = new Payment_record();
		map.put("TransCode", "C00000");
		map.put("BusinessType", "0");
		map.put("BankCommonName", "");//卡类型可空
		map.put("AcctNo", banAA.getBankcardName());
		map.put("AcctName", banAA.getName());
		map.put("TransAmt", "");//交易金额  可空
		map.put("OutTradeNo", ChanPayUtil.generateOutTradeNo());
		map.put("OriOutTradeNo", orderNumber);
		String staring = ChanPayUtil.sendPost(map, BaseConstant.CHARSET,
				BaseConstant.MERCHANT_PRIVATE_KEY);
		System.out.println("返回:"+staring);
		System.out.println("kaishu:"+companyId+","+userId+","+orderNumber+","+orderCreateTime+","+lifeOfLoan+","+shouldReturned+","+riskmanagementFraction+","+borrowMoneyWay+"");
		Jiaoyi sreturn = JSON.parseObject(staring,Jiaoyi.class);
		System.out.println("111111");
		pay.setPipelinenumber(sreturn.getPartnerId());
		pay.setDeleted("0");
		pay.setPaymentmoney(actualAmountReceived);
		String statu = sreturn.getAcceptStatus();

		if(statu.equals("S")){
			System.out.println("支付成功");

			pay.setStatus("支付成功");
			String pipelnen = "lsn_"+sreturn.getPartnerId();
			pay.setPipelinenumber(pipelnen);
			pay.setOrderNumber(orderNumber);
			Integer addId = chanser.AddPayment_record(pay);
			if(addId != null){
				
				System.out.println("kaishu:"+companyId+","+userId+","+orderNumber+","+orderCreateTime+","+lifeOfLoan+","+shouldReturned+","+riskmanagementFraction+","+borrowMoneyWay+"");
				int num = intOrderService.setOrder(companyId,userId,orderNumber,orderCreateTime,lifeOfLoan,howManyTimesBorMoney,shouldReturned,riskmanagementFraction,borrowMoneyWay);
		    	if(num==1) {
		    		int orderId = intOrderService.getOrderId(orderNumber);
			    	BigDecimal surplus_money = finalLine;
		    		num = orderdetailsMapper.setororderdetails(orderId,finalLine,averageDailyInterest,totalInterest,platformServiceFee,actualAmountReceived,
			    			registeClient,sourceName,shouldTotalAmount,surplus_money);
			    	pay.setOrderId(orderId);
			    	chanser.UpdatePayment(pay);
			    	if(num==1) {
			    		map.put("ShortReturn", staring);
			    		map1.put("code", 200);
			    		map1.put("Ncode", 2000);
			    		map1.put("msg","插入成功");
			    	}else {
			    		map1.put("Ncode", 2000);
						map1.put("code",405); 
						map1.put("msg", "插入失败");
					}
		    	}
			}
			

		}else if(statu.equals("F")){

			map1.put("Ncode", 0);
			pay.setStatus("支付失败");
			chanser.AddPayment_record(pay);
			map1.put("ShortReturn", sreturn);
			map1.put("code", 0);
			map1.put("msg", sreturn);
		}
	
		return map1;
	}
	
	
	
	
	
	public static String getShouldReturned(int day) {
	    Date date = new Date();//取时间 
	    Calendar calendar  =   Calendar.getInstance();		 
	    calendar.setTime(date); //需要将date数据转移到Calender对象中操作
	    calendar.add(Calendar.DATE, day);//把日期往后增加n天.正数往后推,负数往前移动 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    date=calendar.getTime();  //这个时间就是日期往后推一天的结果 
	    System.out.println(sdf.format(date));
	    try {
			date =sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String shouldReturned= (date.getTime()+86399000)+"";//应还日时间戳
	    return shouldReturned;
   }
	
	
	
	
	
	
	
	
	
	
	
	
}

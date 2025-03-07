package com.zhita.service.manage.chanpayQuickPay;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhita.dao.manage.PaymentRecordMapper;
import com.zhita.dao.manage.StatisticsDao;
import com.zhita.model.manage.Bankcard;
import com.zhita.model.manage.Bankdeductions;
import com.zhita.model.manage.Deferred;
import com.zhita.model.manage.Loan_setting;
import com.zhita.model.manage.Orderdetails;
import com.zhita.model.manage.Orders;
import com.zhita.model.manage.Payment_record;
import com.zhita.model.manage.Repayment;
import com.zhita.model.manage.User;
import com.zhita.util.Timestamps;


@Service
public class Chanpayserviceimp implements Chanpayservice{
	
	
	@Autowired
	private StatisticsDao stdao;
	
	
	@Autowired
	private PaymentRecordMapper padao;

	
	/**
	 * 添加银行卡
	 */
	@Override
	public Integer AddBankcard(Bankcard bank) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			bank.setAuthentime(Timestamps.dateToStamp1(sim.format(new Date())));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return stdao.AddBankcard(bank);
	}

	/**
	 * 查询银行卡ID
	 */
	@Override
	public Integer SelectTrxId(Bankcard bank) {
		return stdao.SelectTrxId(bank);
	}

	
	/**
	 * 修改认证状态
	 */
	@Override
	public Integer UpdateChanpay(Integer id) {
		return stdao.UpdateStatu(id);
	}

	
	/**
	 * 添加还款记录
	 */
	@Override
	public Integer AddRepayment(Repayment repay) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			repay.setRepaymentDate(Timestamps.dateToStamp(sim.format(new Date())));
		} catch (Exception e) {
			// TODO: handle exception
		}
		repay.setPaymentbtiao("畅捷支付");
		Orders o = stdao.SelectOrderId(repay.getOrderNumber());
		repay.setOrderid(o.getId());
		System.out.println(repay.getOrderid());
		return stdao.AddRepay(repay);
	}

	
	/**
	 * 修改订单状态
	 */
	@Override
	public Integer UpdateOrders(Orders ord) {
		ord = stdao.SelectOrderId(ord.getOrderNumber());//根据编号查询订单ID
		Integer id = stdao.UpdateOrders(ord);//
		if(id != null){
			Integer delaytimes = 0;
			ord.setChenggNum(delaytimes);
			stdao.UpdateUser(ord);
		}else{
			return 0;
		}
		return 200;
	}
	
	
	
	/**
	 * 修改延期状态
	 */
	@Override
	public Integer UpdateDefeOrders(Orders ord) {
		Integer num = stdao.SelectUserdelayTimes(ord.getUserId());
		ord = stdao.SelectOrderId(ord.getOrderNumber());
		ord.setShouldReturnTime(stdao.DefeDefeAfertime(ord.getId()));
		Integer delaytimes = num+1;
		System.out.println("数据:"+delaytimes);
		ord.setChenggNum(delaytimes);
		Integer updateId = stdao.DefeOrder(ord);
		System.out.println(updateId);
		Integer a = null;
		if(updateId!=null){
			a=stdao.UpdateUser(ord);
		}else{
			a=0;
		}
		return a;
	}
	
	

	@Override
	public Integer AddPayment_record(Payment_record pay) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			pay.setRemittanceTime(Timestamps.dateToStamp1(sim.format(new Date())));
		} catch (Exception e) {
			// TODO: handle exception
		}
		stdao.UpdatehowMany(pay.getUserId());
		pay.setProfessionalWork("放款");
		pay.setThirdparty_id(1);
		return stdao.AddPaymentRecord(pay);
	}

	@Override
	public Integer AddDeferred(Deferred defe) {
		defe.setDeleted("0");
		Orders o = stdao.SelectOrderId(defe.getOrderNumber());
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			defe.setDeferredTime(Timestamps.dateToStamp1(sim.format(new Date())));
			defe.setDeferBeforeReturntime(Timestamps.dateToStamp2(defe.getDeferBeforeReturntime()));
			defe.setDeferAfterReturntime(Timestamps.dateToStamp2(defe.getDeferAfterReturntime()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		defe.setOrderid(o.getId());
		return stdao.AddDeferred(defe);
	}

	@Override
	public Integer UpdatePayment(Payment_record pay) {
		Orders o = stdao.SelectOrderId(pay.getOrderNumber());
		pay.setOrderId(o.getId());
		return stdao.UpdatePaymentrecord(pay);
	}

	@Override
	public Bankcard SelectBank(Bankcard userId) {
		return stdao.SelectBanl(userId);
	}
	
	

	@Override
	public Orders OneOrders(Orders or) {
		return stdao.OneOrders(or);
	}

	@Override
	public Integer DeleteChan(Integer userId) {
		return stdao.DeleteChan(userId);
	}

	@Override
	public Integer SelectUserId(Integer userId) {
		return stdao.SelectUserId(userId);
	}

	@Override
	public Integer deleteBank(Integer userId) {
		return stdao.DeleteChan(userId);
	}

	@Override
	public void SelectId(String orderNumber) {
	Orders	ord = stdao.SelectOrderId(orderNumber);//根据编号查询订单ID
		System.out.println(ord.getCompanyId()+ord.getId()+ord.getUserId());
	}

	@Override
	public String loanSetStatu(Loan_setting loan) {
		return stdao.SelectLoanStatus(loan);
	}

	@Override
	public Integer loanMaxMoney(Integer companyId) {
		return stdao.SelectMaxMoney(companyId);
	}

	@Override
	public BigDecimal SumpayMoney(Orderdetails ord) {
		return stdao.SumPayMoney(ord);
	}

	@Override
	public Integer SelectOrdersId(Integer userId) {
		return stdao.SelectOrders(userId);
	}

	@Override
	public String SelectBankName(Integer userId) {
		return stdao.Cardnumber(userId);
	}

	@Override
	public String SelectBorrowing(Integer companyId) {
		return stdao.SelectBorrowing(companyId);
	}

	@Override
	public Integer UpdateRepayStatus(String pipelinenu,String orderNumber) {
		Integer a = stdao.UpdateRepaystatus(pipelinenu);
		Orders orderId = stdao.SelectOrderId(orderNumber);
		return padao.UserDefeNum(orderId.getId());
	}

	@Override
	public User OneUser(Integer userId) {
		return padao.SelectOneUser(userId);
	}

	@Override
	public Integer DeleteOrderNumber(String orderNumber,String orderStatus) {
		Orders ord = padao.OneOrders(orderNumber);//查询订单
		ord.setOrderStatus(orderStatus);
		Orderdetails orderdetails = padao.OneOrderdetails(ord.getId());//查询订单详情
		Integer ca = 0;
		Integer addOrderId = padao.Adddiscardorders(ord);//添加废弃订单表
		if(addOrderId ==1){
			Integer a = padao.DeleteOrderDetailsNumber(orderNumber);//删除订单
			if(a != null){
				Integer addOrderdetails = padao.Adddiscardordertails(orderdetails);
				if(addOrderdetails==1){
					ca = padao.DeleteOrderNumber(orderNumber);//删除订单详情
				}
			}
		}
		
		
		return ca;
	}

	@Override
	public Orders SelectOrdersUser(Integer orderId) {
		return padao.OneOrdersUser(orderId);
	}

	@Override
	public Integer OrderStatus(Integer orderId) {
		return padao.OrderStatus(orderId);
	}

	@Override
	public Integer Addbankdeduction(Bankdeductions bank) {
		Integer addId = stdao.Addbankdeduction(bank);//添加银行卡扣款
		if(addId != null){
			if(bank.getDeductionstatus().equals("扣款成功")){
				Orders ord = stdao.SelectUpdateShouldMoney(bank.getOrderId());//查询
				ord.setShouldReapyMoney(ord.getShouldReapyMoney().subtract(bank.getDeduction_money()));
				System.out.println("订单应还金额:"+ord.getShouldReapyMoney());
				ord.setId(bank.getOrderId());
				stdao.UpdateOrdersShouldMoney(ord);
			}
		}
		return addId;
	}
	
	
	
	
	

}

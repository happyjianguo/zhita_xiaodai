package com.zhita.service.manage.finance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhita.dao.manage.CollectionMapper;
import com.zhita.dao.manage.PaymentRecordMapper;
import com.zhita.model.manage.Accountadjustment;
import com.zhita.model.manage.Bankdeduction;
import com.zhita.model.manage.Deferred;
import com.zhita.model.manage.Orderdetails;
import com.zhita.model.manage.Payment_record;
import com.zhita.model.manage.Thirdparty_interface;
import com.zhita.model.manage.Undertheline;
import com.zhita.util.PageUtil;
import com.zhita.util.Timestamps;



@Service
public class FinanceServiceimp implements FinanceService{
	
	
	
	@Autowired
	private PaymentRecordMapper padao;
	
	
	
	@Autowired
	private CollectionMapper coldao;
	
	
	

	@Override
	public Map<String, Object> AllPaymentrecord(Payment_record payrecord) {
		try {
			payrecord.setStart_time(Timestamps.dateToStamp(payrecord.getStart_time()));
			payrecord.setEnd_time(Timestamps.dateToStamp(payrecord.getEnd_time()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Integer totalCount = padao.TotalCountPayment(payrecord);
		PageUtil pages = new PageUtil(payrecord.getPage(), totalCount);
		payrecord.setPage(pages.getPage());
		payrecord.setProfessionalWork("放款");
		List<Payment_record> payments = padao.PaymentAll(payrecord);
		for(int i=0;i<payments.size();i++){
			payments.get(i).setRemittanceTime(Timestamps.stampToDate(payments.get(i).getRemittanceTime()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PaymentRecord", payments);
		return map;
	}




	@Override
	public Map<String, Object> Huankuan(Payment_record payrecord) {
		try {
			payrecord.setStart_time(Timestamps.dateToStamp(payrecord.getStart_time()));
			payrecord.setEnd_time(Timestamps.dateToStamp(payrecord.getEnd_time()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Integer totalCount = padao.RepaymentTotalCount(payrecord);
		PageUtil pages = new PageUtil(payrecord.getPage(), totalCount);
		payrecord.setPage(pages.getPage());
		payrecord.setProfessionalWork("还款");
		List<Payment_record> rapay = padao.PaymentAll(payrecord);
		//List<Payment_record> rapay = padao.RepaymentAll(payrecord);
		for(int i = 0 ;i<rapay.size();i++){
			rapay.get(i).setRepaymentDate(Timestamps.stampToDate(rapay.get(i).getRepaymentDate()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Repayment", rapay);
		return map;
	}




	@Override
	public Map<String, Object> OrderPayment(Orderdetails orderNumber) {
		orderNumber.setCompanyId(3);
		Orderdetails ordea = padao.SelectPaymentOrder(orderNumber);
		if(ordea.getInterestSum() == null){
			ordea.setRealityBorrowMoney(ordea.getRealityBorrowMoney());
		}else{
			ordea.setRealityBorrowMoney(ordea.getInterestPenaltySum().add(ordea.getRealityBorrowMoney()));
		}
		ordea.setOrderCreateTime(Timestamps.stampToDate(ordea.getOrderCreateTime()));//时间戳转换
		Deferred defe =  coldao.DefNum(ordea.getOrderId());
		orderNumber.setDefeNum(defe.getId());
		orderNumber.setDefeMoney(defe.getInterestOnArrears());
		ordea.setOrderCreateTime(Timestamps.stampToDate(ordea.getOrderCreateTime()));
		ordea.setShouldReturnTime(Timestamps.stampToDate(ordea.getShouldReturnTime()));
		ordea.setDeferAfterReturntime(Timestamps.stampToDate(ordea.getDeferAfterReturntime()));
		ordea.setDeferBeforeReturntime(Timestamps.stampToDate(ordea.getDeferBeforeReturntime()));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Orderdetails", ordea);
		return map;
	}




	@Override
	public Map<String, Object> Accountadjus(Accountadjustment acc) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			acc.setAmou_time(Timestamps.dateToStamp(sim.format(new Date())));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		Integer addId = padao.AddCAccount(acc);
		if(addId != null){
			map.put("code", 200);
			map.put("desc", "添加成功");
		}else{
			map.put("code", 0);
			map.put("desc", "添加失败");
		}
		return map;
	}




	@Override
	public Map<String, Object> OrderAccount(Orderdetails orderNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		Orderdetails ordetails = padao.OrdeRepayment(orderNumber);
		System.out.println(ordetails.getInterestSum()+""+ordetails.getMakeLoans()+""+ordetails.getOrderId());
		ordetails.setOrderCreateTime(Timestamps.stampToDate(ordetails.getOrderCreateTime()));
		ordetails.setOrder_money(ordetails.getInterestSum().add(ordetails.getMakeLoans()));
		ordetails.setRealityBorrowMoney(ordetails.getInterestPenaltySum().add(ordetails.getRealityBorrowMoney()));
		Deferred defe =  coldao.DefNum(ordetails.getOrderId());
		orderNumber.setDefeNum(defe.getId());
		orderNumber.setDefeMoney(defe.getInterestOnArrears());
		ordetails.setOrderCreateTime(Timestamps.stampToDate(ordetails.getOrderCreateTime()));
		ordetails.setShouldReturnTime(Timestamps.stampToDate(ordetails.getShouldReturnTime()));
		ordetails.setDeferAfterReturntime(Timestamps.stampToDate(ordetails.getDeferAfterReturntime()));
		ordetails.setDeferBeforeReturntime(Timestamps.stampToDate(ordetails.getDeferBeforeReturntime()));
		map.put("Orderdetails", ordetails);
		return map;
	}




	@Override
	public Map<String, Object> SelectOrderAccount(Orderdetails ordetail) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			ordetail.setAccounttime(Timestamps.dateToStamp(sim.format(new Date())));
			ordetail.setStart_time(Timestamps.dateToStamp(ordetail.getStart_time()));
			ordetail.setEnd_time(Timestamps.dateToStamp(ordetail.getEnd_time()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Integer totalCount = padao.AccountTotalCount(ordetail);
		PageUtil pages = new PageUtil(ordetail.getPage(), totalCount);
		ordetail.setPage(pages.getPage());
		List<Accountadjustment> accounts = padao.AllAccount(ordetail);
		for (int i = 0; i < accounts.size(); i++) {
			accounts.get(i).setAccounttime(Timestamps.stampToDate(accounts.get(i).getAccounttime()));
			accounts.get(i).setAmou_time(Timestamps.stampToDate(accounts.get(i).getAmou_time()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Accountadjustment", accounts);
		return map;
	}




	@Override
	public Map<String, Object> SelectNoMoney(Orderdetails ordetail) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			ordetail.setAccounttime(Timestamps.dateToStamp(sim.format(new Date())));
			ordetail.setStart_time(Timestamps.dateToStamp(ordetail.getStart_time()));
			ordetail.setEnd_time(Timestamps.dateToStamp(ordetail.getEnd_time()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Integer totalCount = padao.AccountTotalCount(ordetail);
		PageUtil pages = new PageUtil(ordetail.getPage(), totalCount);
		ordetail.setPage(pages.getPage());
		List<Accountadjustment> accounts = padao.AllStatu(ordetail);
		for (int i = 0; i < accounts.size(); i++) {
			accounts.get(i).setAccounttime(Timestamps.stampToDate(accounts.get(i).getAccounttime()));
			accounts.get(i).setAmou_time(Timestamps.stampToDate(accounts.get(i).getAmou_time()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Accountadjustment", accounts);
		return map;
	}




	@Override
	public Map<String, Object> SelectOkMoney(Orderdetails ordetail) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			ordetail.setAccounttime(Timestamps.dateToStamp(sim.format(new Date())));
			ordetail.setStart_time(Timestamps.dateToStamp(ordetail.getStart_time()));
			ordetail.setEnd_time(Timestamps.dateToStamp(ordetail.getEnd_time()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Integer totalCount = padao.AccountTotalCount(ordetail);
		PageUtil pages = new PageUtil(ordetail.getPage(), totalCount);
		ordetail.setPage(pages.getPage());
		List<Accountadjustment> accounts = padao.AllNotMoneyStatu(ordetail);
		for (int i = 0; i < accounts.size(); i++) {
			accounts.get(i).setAccounttime(Timestamps.stampToDate(accounts.get(i).getAccounttime()));
			accounts.get(i).setAmou_time(Timestamps.stampToDate(accounts.get(i).getAmou_time()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Accountadjustment", accounts);
		return map;
	}




	@Override
	public Map<String, Object> Selectoffine(Orderdetails ordetail) {
		try {
			ordetail.setStart_time(Timestamps.dateToStamp(ordetail.getStart_time()));
			ordetail.setEnd_time(Timestamps.dateToStamp(ordetail.getEnd_time()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Integer totalCount = padao.SelectUnderthTotalCount(ordetail);
		PageUtil pages = new PageUtil(ordetail.getPage(), totalCount);
		ordetail.setPage(pages.getPage());
		ordetail.setIds(padao.Sys_userIds(ordetail.getCompanyId()));
		List<Undertheline> under = padao.AllUnderthe(ordetail);
		for (int i = 0; i < under.size(); i++) {
			under.get(i).setUnderthe_time(Timestamps.stampToDate(under.get(i).getUnderthe_time()));
		}
		map.put("Undertheline", under);
		return map;
	}




	@Override
	public Map<String, Object> ThirdpatyAll(Integer compayId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Thirdparty_interface> third = padao.SelectThird(compayId);
		map.put("Thirdparty_interface", third);
		return map;
	}




	@Override
	public Map<String, Object> AddUnderthe(Undertheline unde) {
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			unde.setUnderthe_time(Timestamps.dateToStamp(sim.format(new Date())));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Integer addId = padao.AddUndertheline(unde);
		if(addId != null){
			map.put("code", 200);
			map.put("desc", "添加成功");
		}else{
			map.put("code", 0);
			map.put("desc", "添加失败");
		}
		return map;
	}




	@Override
	public Map<String, Object> SelectBankDeductOrders(Bankdeduction bank) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			bank.setStartu_time(Timestamps.dateToStamp(bank.getStartu_time()));
			bank.setEnd_time(Timestamps.dateToStamp(bank.getEnd_time()));
			bank.setStatu_timeOrder(Timestamps.dateToStamp(bank.getStatu_timeOrder()));
			bank.setEnd_timeOrder(Timestamps.dateToStamp(bank.getEnd_timeOrder()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Integer totalCount = padao.BankDeduOrderNum(bank);
		PageUtil pages = new PageUtil(bank.getPage(), totalCount);
		bank.setPage(pages.getPage());
		List<Orderdetails> orders = padao.BankDeduOrder(bank);
		for(int i=0;i<orders.size();i++){
			orders.get(i).setBorrowTimeLimit(Timestamps.stampToDate(orders.get(i).getBorrowTimeLimit()));
			orders.get(i).setShouldReturnTime(Timestamps.stampToDate(orders.get(i).getShouldReturnTime()));
			orders.get(i).setCollectionTime(Timestamps.stampToDate(orders.get(i).getCollectionTime()));
			orders.get(i).setOrderCreateTime(Timestamps.stampToDate(orders.get(i).getOrderCreateTime()));
			orders.get(i).setDeferBeforeReturntime(Timestamps.stampToDate(orders.get(i).getDeferBeforeReturntime()));
			orders.get(i).setDeferAfterReturntime(Timestamps.stampToDate(orders.get(i).getDeferAfterReturntime()));
		}
		map.put("Orderdetails", orders);
		return map;
	}




	@Override
	public Map<String, Object> AllBank(Integer orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(orderId != null){
			List<Bankdeduction> banls = padao.BanAll(orderId);
			map.put("bankde", banls);
		}else{
			map.put("bankde", "无数据");
		}
		return map;
	}




	@Override
	public Map<String, Object> AddBank(Bankdeduction banl) {
		
		return null;
	}




	@Override
	public Map<String, Object> AllDelayStatis(Bankdeduction banl) {
		try {
			banl.setStartu_time(Timestamps.dateToStamp(banl.getStartu_time()));
			banl.setEnd_time(Timestamps.dateToStamp(banl.getEnd_time()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Integer totalCount = padao.DelayTatolCount(banl);
		if(totalCount !=null){
			PageUtil pages = new PageUtil(banl.getPage(), totalCount);
			banl.setPage(pages.getPage());
		}else{
			PageUtil pages = new PageUtil(banl.getPage(), 0);
			banl.setPage(pages.getPage());
		}
		
		
		List<Bankdeduction> banks = padao.DelayStatisc(banl);
		for (int i = 0; i < banks.size(); i++) {
			banks.get(i).setDeferAfterReturntime(Timestamps.stampToDate(banks.get(i).getDeferAfterReturntime()));
		}
		map.put("Bankdeduction", banks);
		return map;
	}




	@Override
	public Map<String, Object> Financialover(Bankdeduction banl) {
		try {
			banl.setStartu_time(Timestamps.dateToStamp(banl.getStartu_time()));
			banl.setEnd_time(Timestamps.dateToStamp(banl.getEnd_time()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Bankdeduction bank = padao.OneBank(banl);//realborrowing     实借笔数        realexpenditure   世界金额 
		bank.setDeferredamount(padao.YanMoney(banl));
		bank.setBankcardName(""+bank.getRealborrowing()+","+bank.getRealexpenditure()+","+0+"");//实借笔数    实借金额
		bank.setDeductionstatus(""+bank.getRealreturn()+","+0+","+bank.getPaymentamount()+"");//实还笔数    实还金额
		bank.setOrderNumber(""+0+","+bank.getOverdueamount()+","+0+"");//实借笔数    实借金额
		bank.setName(""+0+","+bank.getDeferredamount()+","+0+"");//实借笔数    实借金额
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Bankdeduction", bank);
		return map;
	}

}

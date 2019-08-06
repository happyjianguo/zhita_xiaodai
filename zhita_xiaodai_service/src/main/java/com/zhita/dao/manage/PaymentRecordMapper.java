package com.zhita.dao.manage;

import java.math.BigDecimal;
import java.util.List;

import com.zhita.model.manage.Accountadjustment;
import com.zhita.model.manage.Bankdeductions;
import com.zhita.model.manage.Deferred;
import com.zhita.model.manage.Deferred_settings;
import com.zhita.model.manage.Loan_setting;
import com.zhita.model.manage.Offlinedelay;
import com.zhita.model.manage.Offlinetransfer;
import com.zhita.model.manage.Offlinjianmian;
import com.zhita.model.manage.Orderdetails;
import com.zhita.model.manage.Payment_record;
import com.zhita.model.manage.Repayment_setting;
import com.zhita.model.manage.Undertheline;



public interface PaymentRecordMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(Payment_record record);

    int insertSelective(Payment_record record);

    Payment_record selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Payment_record record);

    int updateByPrimaryKey(Payment_record record);
    
    
    Integer TotalCountPayment(Payment_record record);
    
    
    List<Payment_record> PaymentAll(Payment_record record);
    
    
    Integer RepaymentTotalCount(Payment_record record);
    
    
    List<Payment_record> RepaymentAll(Payment_record record);
    
    
    Orderdetails SelectPaymentOrder(Orderdetails orderNumber);
    
    
    Orderdetails OrdeRepayment(Orderdetails orderNumber);
    
    
    Integer AddCAccount(Accountadjustment acount);
    
    
    Integer AccountTotalCount(Orderdetails orde);
    
    
    List<Accountadjustment> AllAccount(Orderdetails orde);
    
    
    List<Accountadjustment> AllStatu(Orderdetails orde);
    
    
    List<Accountadjustment> AllNotMoneyStatu(Orderdetails orde);
    
    
    List<Loan_setting> SelectThird(Integer companyId);
    
    
    Integer AddUndertheline(Offlinetransfer unde);
    
    
    Integer SelectUnderthTotalCount(Orderdetails order);

    
    List<Offlinetransfer> AllUnderthe(Orderdetails order);
    
    
    Integer BankDeduOrderNum(Bankdeductions bank);
    
    
    List<Orderdetails> BankDeduOrder(Bankdeductions bank);
    
    
    List<Bankdeductions> BanAll(Integer orderId);
    
    
    Integer DelayTatolCount(Bankdeductions bank);
    
    
    List<Bankdeductions> DelayStatisc(Bankdeductions bank);
    
    
    Bankdeductions OneBank(Bankdeductions banl);
    
    
    BigDecimal YanMoney(Bankdeductions banl);
    
    
    List<Integer> Sys_userIds(Integer companyId);
    
    
    Integer AddOffJianMian(Offlinjianmian off);
    
    
    List<Offlinjianmian> XiaOrder(Orderdetails order);
    
    
    List<Integer> XiaTotalCount(Orderdetails ord);
    
    
    List<Repayment_setting> SelectRepay(Integer companyId);
    
    
    Integer UpdateOrderType(Integer orderId);
    
    
    Bankdeductions SelectBank(Bankdeductions ban);
    
    
    Deferred_settings OneCompanyDeferr(Integer companyId);
    
    
    Deferred DeleteNumMoney(Integer orderId);
    
    
    Integer AddDelay(Offlinedelay of);
    
    
    Integer OffTotalCount(Offlinedelay of);
    
    
    List<Offlinedelay> Allofflinedelay(Offlinedelay of);
    
    
    Bankdeductions DefeRRe(Bankdeductions ban);
    
    
    List<Bankdeductions> BankdeduCtionsData(Bankdeductions banl);
}
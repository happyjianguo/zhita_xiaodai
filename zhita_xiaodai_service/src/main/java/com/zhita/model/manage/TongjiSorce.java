package com.zhita.model.manage;

import java.math.BigDecimal;

//渠道的统计数据
public class TongjiSorce {
	private Integer id;//将查询出来的渠道id  当做主键id
	private String date;//日期
	private String date1;
	private Integer sourceid;//渠道id
	private String sourcename;//渠道名称
	private Integer uv;//uv
	private float registernum;//真实的注册人数
	private float registernumdis;//折扣后的注册人数
	private String cvr;//uv到注册人数转化率(注册率%)
	private Integer applynum;//申请人数
	private String cvr1;//注册到申请转化率（申请率）
	private Integer activatecount;//激活人数
	private Integer authencount;//认证人数
	private Integer machineauditpass;//通过人数
	private Integer orderpass;//已借款人数
	private String cvr2;//已借款率
	private Integer companyid;//公司名id
	private BigDecimal price;//渠道的流量单价  
	private String clearingform;//结算方式（1：uv；2：注册数；3；已借款人数）
	private BigDecimal flowcharge;//流量费

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	
	public Integer getSourceid() {
		return sourceid;
	}
	public void setSourceid(Integer sourceid) {
		this.sourceid = sourceid;
	}
	public String getSourcename() {
		return sourcename;
	}
	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}
	public Integer getUv() {
		return uv;
	}
	public void setUv(Integer uv) {
		this.uv = uv;
	}
	public float getRegisternum() {
		return registernum;
	}
	public void setRegisternum(float registernum) {
		this.registernum = registernum;
	}
	public float getRegisternumdis() {
		return registernumdis;
	}
	public void setRegisternumdis(float registernumdis) {
		this.registernumdis = registernumdis;
	}
	public String getCvr() {
		return cvr;
	}
	public void setCvr(String cvr) {
		this.cvr = cvr;
	}
	public Integer getApplynum() {
		return applynum;
	}
	public void setApplynum(Integer applynum) {
		this.applynum = applynum;
	}
	public String getCvr1() {
		return cvr1;
	}
	public void setCvr1(String cvr1) {
		this.cvr1 = cvr1;
	}
	public Integer getMachineauditpass() {
		return machineauditpass;
	}
	public void setMachineauditpass(Integer machineauditpass) {
		this.machineauditpass = machineauditpass;
	}
	public Integer getActivatecount() {
		return activatecount;
	}
	public void setActivatecount(Integer activatecount) {
		this.activatecount = activatecount;
	}
	public Integer getAuthencount() {
		return authencount;
	}
	public void setAuthencount(Integer authencount) {
		this.authencount = authencount;
	}
	public Integer getOrderpass() {
		return orderpass;
	}
	public void setOrderpass(Integer orderpass) {
		this.orderpass = orderpass;
	}
	public String getCvr2() {
		return cvr2;
	}
	public void setCvr2(String cvr2) {
		this.cvr2 = cvr2;
	}
	public Integer getCompanyid() {
		return companyid;
	}
	public void setCompanyid(Integer companyid) {
		this.companyid = companyid;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getClearingform() {
		return clearingform;
	}
	public void setClearingform(String clearingform) {
		this.clearingform = clearingform;
	}
	public BigDecimal getFlowcharge() {
		return flowcharge;
	}
	public void setFlowcharge(BigDecimal flowcharge) {
		this.flowcharge = flowcharge;
	}
	
}

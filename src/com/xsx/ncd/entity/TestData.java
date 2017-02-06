package com.xsx.ncd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="TESTDATA")
@Entity
public class TestData {
	
	private Integer id;
	private String cnum;						//编号
	private Integer cardid;
	private Integer deviceid;
	private Integer userid;
	private String t_name;					//测试人姓名
	private String sid;						//样品id -- 测试时输入
	private java.sql.Timestamp testtime;	//测试时间
	private Float e_t;						//环境温度
	private Float o_t;						//检测卡温度
	private Integer outt;					//超时时间
	private Integer c_l;					//c线位置
	private Integer t_l;					//T线位置
	private Integer b_l;					//基线位置
	private String serie_a;					//测试曲线1
	private String serie_b;					//测试曲线2
	private String serie_c;					//测试曲线3
	private Float t_c_v;					//峰高比
	private Float a_p;						//校准参数
	private Float b_v;						//原始结果
	private Float a_v;						//校准后结果
	private String t_re;					//设备报告的测试结果说明
	private String result;					//报告结果
	private String r_desc;					//报告说明
	private java.sql.Timestamp	uptime;		//报告上传时间
	private java.sql.Timestamp	handletime;	//报告处理时间
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getCnum() {
		return cnum;
	}
	public void setCnum(String cnum) {
		this.cnum = cnum;
	}

	public Integer getCardid() {
		return cardid;
	}

	public void setCardid(Integer cardid) {
		this.cardid = cardid;
	}

	public Integer getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(Integer deviceid) {
		this.deviceid = deviceid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getT_name() {
		return t_name;
	}
	public void setT_name(String t_name) {
		this.t_name = t_name;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public java.sql.Timestamp getTesttime() {
		return testtime;
	}
	public void setTesttime(java.sql.Timestamp testtime) {
		this.testtime = testtime;
	}
	public Float getE_t() {
		return e_t;
	}
	public void setE_t(Float e_t) {
		this.e_t = e_t;
	}
	public Float getO_t() {
		return o_t;
	}
	public void setO_t(Float o_t) {
		this.o_t = o_t;
	}
	public Integer getOutt() {
		return outt;
	}
	public void setOutt(Integer outt) {
		this.outt = outt;
	}
	public Integer getC_l() {
		return c_l;
	}
	public void setC_l(Integer c_l) {
		this.c_l = c_l;
	}
	public Integer getT_l() {
		return t_l;
	}
	public void setT_l(Integer t_l) {
		this.t_l = t_l;
	}
	public Integer getB_l() {
		return b_l;
	}
	public void setB_l(Integer b_l) {
		this.b_l = b_l;
	}
	
	@Column(length=600)
	public String getSerie_a() {
		return serie_a;
	}
	public void setSerie_a(String serie_a) {
		this.serie_a = serie_a;
	}
	
	@Column(length=600)
	public String getSerie_b() {
		return serie_b;
	}
	public void setSerie_b(String serie_b) {
		this.serie_b = serie_b;
	}
	
	@Column(length=600)
	public String getSerie_c() {
		return serie_c;
	}
	public void setSerie_c(String serie_c) {
		this.serie_c = serie_c;
	}
	public Float getT_c_v() {
		return t_c_v;
	}
	public void setT_c_v(Float t_c_v) {
		this.t_c_v = t_c_v;
	}
	public Float getA_p() {
		return a_p;
	}
	public void setA_p(Float a_p) {
		this.a_p = a_p;
	}
	public Float getB_v() {
		return b_v;
	}
	public void setB_v(Float b_v) {
		this.b_v = b_v;
	}
	public Float getA_v() {
		return a_v;
	}
	public void setA_v(Float a_v) {
		this.a_v = a_v;
	}
	public String getT_re() {
		return t_re;
	}
	public void setT_re(String t_re) {
		this.t_re = t_re;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getR_desc() {
		return r_desc;
	}
	public void setR_desc(String r_desc) {
		this.r_desc = r_desc;
	}
	public java.sql.Timestamp getUptime() {
		return uptime;
	}
	public void setUptime(java.sql.Timestamp uptime) {
		this.uptime = uptime;
	}
	public java.sql.Timestamp getHandletime() {
		return handletime;
	}
	public void setHandletime(java.sql.Timestamp handletime) {
		this.handletime = handletime;
	}
	
}

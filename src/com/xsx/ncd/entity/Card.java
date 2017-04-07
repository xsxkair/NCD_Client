package com.xsx.ncd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="CARD")
@Entity
public class Card {
	
	private Integer id;
	private String cid;
	private String item;
	private Integer channel;
	private Float low;
	private Float high;
	private String normal;
	private String danwei;
	private Integer xsd;
	private Integer t_l;
	private Integer waitt;
	private Integer c_l;
	private java.sql.Date outdate;			//过期时间
	private Float fend1;
	private Float fend2;
	private Float qu1_a;
	private Float qu1_b;
	private Float qu1_c;
	private Float qu2_a;
	private Float qu2_b;
	private Float qu2_c;
	private Float qu3_a;
	private Float qu3_b;
	private Float qu3_c;
	private java.sql.Timestamp uptime;
	private String maker;
	private String manager;
	private java.sql.Timestamp managetime;
	private String mstatus;
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Float getLow() {
		return low;
	}

	public void setLow(Float low) {
		this.low = low;
	}

	public Float getHigh() {
		return high;
	}

	public void setHigh(Float high) {
		this.high = high;
	}

	public String getNormal() {
		return normal;
	}

	public void setNormal(String normal) {
		this.normal = normal;
	}

	public String getDanwei() {
		return danwei;
	}

	public void setDanwei(String danwei) {
		this.danwei = danwei;
	}

	public Integer getXsd() {
		return xsd;
	}

	public void setXsd(Integer xsd) {
		this.xsd = xsd;
	}

	public Integer getT_l() {
		return t_l;
	}

	public void setT_l(Integer t_l) {
		this.t_l = t_l;
	}

	public Integer getWaitt() {
		return waitt;
	}

	public void setWaitt(Integer waitt) {
		this.waitt = waitt;
	}

	public Integer getC_l() {
		return c_l;
	}

	public void setC_l(Integer c_l) {
		this.c_l = c_l;
	}

	public java.sql.Date getOutdate() {
		return outdate;
	}

	public void setOutdate(java.sql.Date outdate) {
		this.outdate = outdate;
	}

	public Float getFend1() {
		return fend1;
	}

	public void setFend1(Float fend1) {
		this.fend1 = fend1;
	}

	public Float getFend2() {
		return fend2;
	}

	public void setFend2(Float fend2) {
		this.fend2 = fend2;
	}

	public Float getQu1_a() {
		return qu1_a;
	}

	public void setQu1_a(Float qu1_a) {
		this.qu1_a = qu1_a;
	}

	public Float getQu1_b() {
		return qu1_b;
	}

	public void setQu1_b(Float qu1_b) {
		this.qu1_b = qu1_b;
	}

	public Float getQu1_c() {
		return qu1_c;
	}

	public void setQu1_c(Float qu1_c) {
		this.qu1_c = qu1_c;
	}

	public Float getQu2_a() {
		return qu2_a;
	}

	public void setQu2_a(Float qu2_a) {
		this.qu2_a = qu2_a;
	}

	public Float getQu2_b() {
		return qu2_b;
	}

	public void setQu2_b(Float qu2_b) {
		this.qu2_b = qu2_b;
	}

	public Float getQu2_c() {
		return qu2_c;
	}

	public void setQu2_c(Float qu2_c) {
		this.qu2_c = qu2_c;
	}

	public Float getQu3_a() {
		return qu3_a;
	}

	public void setQu3_a(Float qu3_a) {
		this.qu3_a = qu3_a;
	}

	public Float getQu3_b() {
		return qu3_b;
	}

	public void setQu3_b(Float qu3_b) {
		this.qu3_b = qu3_b;
	}

	public Float getQu3_c() {
		return qu3_c;
	}

	public void setQu3_c(Float qu3_c) {
		this.qu3_c = qu3_c;
	}


	public java.sql.Timestamp getUptime() {
		return uptime;
	}

	public void setUptime(java.sql.Timestamp uptime) {
		this.uptime = uptime;
	}

	public java.sql.Timestamp getManagetime() {
		return managetime;
	}

	public void setManagetime(java.sql.Timestamp managetime) {
		this.managetime = managetime;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getMstatus() {
		return mstatus;
	}

	public void setMstatus(String mstatus) {
		this.mstatus = mstatus;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", cid=" + cid + ", item=" + item + "]";
	}
	
	
}

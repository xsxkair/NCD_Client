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
	private String normal;
	private Float low;
	private Float high;
	private String danwei;
	private Integer xsd;
	private String t_l;
	private Integer waitt;
	private String c_l;
	private java.sql.Date outdate;			//过期时间
	private String fend;
	private String qu1_a;
	private String qu1_b;
	private String qu1_c;
	private String qu1_d;
	private String qu2_a;
	private String qu2_b;
	private String qu2_c;
	private String qu2_d;
	
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

	public String getNormal() {
		return normal;
	}

	public void setNormal(String normal) {
		this.normal = normal;
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

	public String getT_l() {
		return t_l;
	}

	public void setT_l(String t_l) {
		this.t_l = t_l;
	}

	public Integer getWaitt() {
		return waitt;
	}

	public void setWaitt(Integer waitt) {
		this.waitt = waitt;
	}

	public String getC_l() {
		return c_l;
	}

	public void setC_l(String c_l) {
		this.c_l = c_l;
	}

	public java.sql.Date getOutdate() {
		return outdate;
	}

	public void setOutdate(java.sql.Date outdate) {
		this.outdate = outdate;
	}

	public String getFend() {
		return fend;
	}

	public void setFend(String fend) {
		this.fend = fend;
	}

	public String getQu1_a() {
		return qu1_a;
	}

	public void setQu1_a(String qu1_a) {
		this.qu1_a = qu1_a;
	}

	public String getQu1_b() {
		return qu1_b;
	}

	public void setQu1_b(String qu1_b) {
		this.qu1_b = qu1_b;
	}

	public String getQu1_c() {
		return qu1_c;
	}

	public void setQu1_c(String qu1_c) {
		this.qu1_c = qu1_c;
	}

	public String getQu1_d() {
		return qu1_d;
	}

	public void setQu1_d(String qu1_d) {
		this.qu1_d = qu1_d;
	}

	public String getQu2_a() {
		return qu2_a;
	}

	public void setQu2_a(String qu2_a) {
		this.qu2_a = qu2_a;
	}

	public String getQu2_b() {
		return qu2_b;
	}

	public void setQu2_b(String qu2_b) {
		this.qu2_b = qu2_b;
	}

	public String getQu2_c() {
		return qu2_c;
	}

	public void setQu2_c(String qu2_c) {
		this.qu2_c = qu2_c;
	}

	public String getQu2_d() {
		return qu2_d;
	}

	public void setQu2_d(String qu2_d) {
		this.qu2_d = qu2_d;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", cid=" + cid + ", item=" + item + "]";
	}
	
	
}

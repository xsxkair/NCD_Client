package com.xsx.ncd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="LABTESTDATA")
@Entity
public class LabTestData {
	private Integer id;
	private String did;
	private String userid;
	private java.sql.Timestamp testtime;	//测试时间
	private String serie;					//测试曲线
	private String tindex;					//测试组内索引
	private String dsc;
	private Integer t_l;
	private Integer b_l;
	private Integer c_l;
	private Float t_c;
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserid() {
		return userid;
	}
	public java.sql.Timestamp getTesttime() {
		return testtime;
	}
	public void setTesttime(java.sql.Timestamp testtime) {
		this.testtime = testtime;
	}
	
	@Column(length=2000)
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getTindex() {
		return tindex;
	}
	public void setTindex(String tindex) {
		this.tindex = tindex;
	}
	public String getDsc() {
		return dsc;
	}
	public void setDsc(String dsc) {
		this.dsc = dsc;
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
	public Integer getC_l() {
		return c_l;
	}
	public void setC_l(Integer c_l) {
		this.c_l = c_l;
	}
	public Float getT_c() {
		return t_c;
	}
	public void setT_c(Float t_c) {
		this.t_c = t_c;
	}
}

package com.xsx.ncd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="CARDRECORD")
@Entity
public class CardRecord {
	private Integer id;
	private Integer cardid;
	private Integer num;					//数目，入库为正，出库为负
	private java.sql.Timestamp dotime;		//出入库时间
	private Integer userid;				//操作人
	private String name;					//出库领料人
	private Integer deviceid;					//出库设备
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCardid() {
		return cardid;
	}
	public void setCardid(Integer cardid) {
		this.cardid = cardid;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public java.sql.Timestamp getDotime() {
		return dotime;
	}
	public void setDotime(java.sql.Timestamp dotime) {
		this.dotime = dotime;
	}
	
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(Integer deviceid) {
		this.deviceid = deviceid;
	}

}

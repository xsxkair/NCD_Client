package com.xsx.ncd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="CARDRECORD")
@Entity
public class CardRecord {
	private Integer id;
	private Card card;
	private Integer num;					//数目，入库为正，出库为负
	private java.sql.Timestamp dotime;		//出入库时间
	private Manager manager;				//操作人
	private String name;					//出库领料人
	private Device device;					//出库设备
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@JoinColumn(name="Card_id")
	@ManyToOne
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
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
	
	@JoinColumn(name="Manager_id")
	@ManyToOne
	public Manager getManager() {
		return manager;
	}
	public void setManager(Manager manager) {
		this.manager = manager;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JoinColumn(name="Device_id")
	@ManyToOne
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
}

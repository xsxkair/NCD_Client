package com.xsx.ncd.define;

import java.sql.Timestamp;


public class LabDataTableItem {
	private Integer index;
	private String t_index;
	private String user;
	private String device;
	private java.sql.Timestamp testdate;
	private Float result;
	private String t_desc;
	
	private Integer dataindex;			//数据库中数据的主键

	public LabDataTableItem(Integer index, String t_index, String user, String device, Timestamp testdate,
			Float result, String t_desc, Integer dataindex) {
		super();
		this.index = index;
		this.t_index = t_index;
		this.user = user;
		this.device = device;
		this.testdate = testdate;
		this.result = result;
		this.t_desc = t_desc;
		this.dataindex = dataindex;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getT_index() {
		return t_index;
	}

	public void setT_index(String t_index) {
		this.t_index = t_index;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public java.sql.Timestamp getTestdate() {
		return testdate;
	}

	public void setTestdate(java.sql.Timestamp testdate) {
		this.testdate = testdate;
	}

	public Float getResult() {
		return result;
	}

	public void setResult(Float result) {
		this.result = result;
	}

	public String getT_desc() {
		return t_desc;
	}

	public void setT_desc(String t_desc) {
		this.t_desc = t_desc;
	}

	public Integer getDataindex() {
		return dataindex;
	}

	public void setDataindex(Integer dataindex) {
		this.dataindex = dataindex;
	}
	
	
}

package com.xsx.ncd.define;



import java.sql.Timestamp;

import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.entity.User;

public class CardTableItem {
	private Integer index;
	private String item;
	private String pihao;
	private java.sql.Timestamp update;
	private String maker;
	private java.sql.Timestamp managetime;
	private String manager;
	private String status;
	
	private Integer dataindex;			//数据库中数据的主键

	public CardTableItem(Integer index, String item, String pihao, Timestamp update, String maker, java.sql.Timestamp managetime,
			String manager, String status, Integer dataindex) {
		super();
		this.index = index;
		this.item = item;
		this.pihao = pihao;
		this.update = update;
		this.maker = maker;
		this.managetime = managetime;
		this.manager = manager;
		this.status = status;
		this.dataindex = dataindex;
	}

	public Integer getIndex() {
		return index;
	}

	public String getItem() {
		return item;
	}

	public String getPihao() {
		return pihao;
	}

	public java.sql.Timestamp getUpdate() {
		return update;
	}

	public String getMaker() {
		return maker;
	}


	public java.sql.Timestamp getManagetime() {
		return managetime;
	}


	public String getManager() {
		return manager;
	}

	public String getStatus() {
		return status;
	}
	public Integer getDataindex() {
		return dataindex;
	}
}

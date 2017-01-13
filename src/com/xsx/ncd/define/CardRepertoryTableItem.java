package com.xsx.ncd.define;

import com.xsx.ncd.entity.CardRecord;

public class CardRepertoryTableItem {
	
	private Integer index;
	
	private String time;
	private String item;
	private String action;
	private String acter;
	private String user;
	private String device;
	
	private CardRecord cardRecord;
	
	public CardRepertoryTableItem(CardRecord cardRecord) {
		this.cardRecord = cardRecord;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getTime() {
		return cardRecord.getDotime().toString();
	}

	public String getItem() {
		return cardRecord.getCard().getItem()+"("+ cardRecord.getCard().getCid() +")";
	}

	public void setItem(String item) {
		this.item = item;
	}

	
	public String getAction() {
		if(cardRecord.getNum().intValue() > 0)
			return "Èë¿â " + cardRecord.getNum();
		else
			return "³ö¿â " + Math.abs( cardRecord.getNum() );
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActer() {
		return cardRecord.getManager().getName();
	}

	public void setActer(String acter) {
		this.acter = acter;
	}

	public String getUser() {
		return cardRecord.getName();
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDevice() {
		return cardRecord.getDevice().getDid();
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public CardRecord getCardRecord() {
		return cardRecord;
	}

	public void setCardRecord(CardRecord cardRepertory) {
		this.cardRecord = cardRepertory;
	}
}

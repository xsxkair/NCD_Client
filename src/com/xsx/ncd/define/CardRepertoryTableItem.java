package com.xsx.ncd.define;

import com.xsx.ncd.entity.CardRepertory;

public class CardRepertoryTableItem {
	
	private Integer index;
	
	private String time;
	private String item;
	private String action;
	private String acter;
	private String user;
	private String device;
	
	private CardRepertory cardRepertory;
	
	public CardRepertoryTableItem(CardRepertory cardRepertory) {
		this.cardRepertory = cardRepertory;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getTime() {
		return cardRepertory.getDotime().toString();
	}

	public String getItem() {
		return cardRepertory.getCard().getItem()+"("+ cardRepertory.getCard().getCid() +")";
	}

	public void setItem(String item) {
		this.item = item;
	}

	
	public String getAction() {
		if(cardRepertory.getNum().intValue() > 0)
			return "Èë¿â " + cardRepertory.getNum();
		else
			return "³ö¿â " + Math.abs( cardRepertory.getNum() );
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActer() {
		return cardRepertory.getManager().getName();
	}

	public void setActer(String acter) {
		this.acter = acter;
	}

	public String getUser() {
		return cardRepertory.getName();
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDevice() {
		return cardRepertory.getDevice().getDid();
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public CardRepertory getCardRepertory() {
		return cardRepertory;
	}

	public void setCardRepertory(CardRepertory cardRepertory) {
		this.cardRepertory = cardRepertory;
	}
}

package com.xsx.ncd.define;

import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.CardRecord;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;

public class CardRecordTableItem {
	private String time;			//时间
	private String piHao;			//批号
	private String item;			//试剂卡项目
	private String inOrOutNum;		//数目
	private String managerName;		//操作人姓名
	private String userName;			//领料人姓名
	private String deviceid;			//领料设备
	
	private CardRecord cardRecord;
	private Device device;
	private Card card;
	private User user;
	
	public CardRecordTableItem(CardRecord cardRecord, Device device, Card card, User user) {
		this.cardRecord = cardRecord;
		this.device = device;
		this.card = card;
		this.user = user;
	}

	public String getTime() {
		return cardRecord.getDotime().toString();
	}

	public String getPiHao() {
		return this.card.getCid();
	}

	public String getItem() {
		return this.card.getItem();
	}

	public String getInOrOutNum() {
		if(cardRecord.getNum().intValue() > 0)
			return "入库 " + cardRecord.getNum();
		else
			return "出库 " + Math.abs( cardRecord.getNum() );
	}

	public String getManagerName() {
		return this.user.getName();
	}

	public String getUserName() {
		return cardRecord.getName();
	}

	public String getDeviceid() {
		if(this.device == null)
			return null;
		else
			return this.device.getDid();
	}
}

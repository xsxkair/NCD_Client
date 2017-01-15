package com.xsx.ncd.define;

import com.xsx.ncd.entity.CardRecord;

public class CardRecordTableItem {
	private String time;			//时间
	private String piHao;			//批号
	private String item;			//试剂卡项目
	private String inOrOutNum;		//数目
	private String managerName;		//操作人姓名
	private String userName;			//领料人姓名
	private String device;			//领料设备
	
	private CardRecord cardRecord;
	
	public CardRecordTableItem(CardRecord cardRecord) {
		this.cardRecord = cardRecord;
	}

	public String getTime() {
		return cardRecord.getDotime().toString();
	}

	public String getPiHao() {
		return cardRecord.getCard().getCid();
	}

	public String getItem() {
		return cardRecord.getCard().getItem();
	}

	public String getInOrOutNum() {
		if(cardRecord.getNum().intValue() > 0)
			return "入库 " + cardRecord.getNum();
		else
			return "出库 " + Math.abs( cardRecord.getNum() );
	}

	public String getManagerName() {
		return cardRecord.getManager().getName();
	}

	public String getUserName() {
		return cardRecord.getName();
	}

	public String getDevice() {
		if(cardRecord.getDevice() == null)
			return null;
		else
			return cardRecord.getDevice().getDid();
	}
}

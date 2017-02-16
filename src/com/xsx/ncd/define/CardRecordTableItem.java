package com.xsx.ncd.define;

import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.CardRecord;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;

public class CardRecordTableItem {
	private String time;			//ʱ��
	private String piHao;			//����
	private String item;			//�Լ�����Ŀ
	private String inOrOutNum;		//��Ŀ
	private String managerName;		//����������
	private String userName;			//����������
	private String deviceid;			//�����豸
	
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
			return "��� " + cardRecord.getNum();
		else
			return "���� " + Math.abs( cardRecord.getNum() );
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

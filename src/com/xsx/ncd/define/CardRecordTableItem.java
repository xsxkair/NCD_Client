package com.xsx.ncd.define;

import com.xsx.ncd.entity.CardRecord;

public class CardRecordTableItem {
	private String time;			//ʱ��
	private String piHao;			//����
	private String item;			//�Լ�����Ŀ
	private String inOrOutNum;		//��Ŀ
	private String managerName;		//����������
	private String userName;			//����������
	private String device;			//�����豸
	
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
			return "��� " + cardRecord.getNum();
		else
			return "���� " + Math.abs( cardRecord.getNum() );
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

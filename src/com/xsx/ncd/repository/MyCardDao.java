package com.xsx.ncd.repository;

public interface MyCardDao {
	public Object[] QueryCardList(String item, String pihao, java.sql.Date upDate, String maker, java.sql.Date manageDate, 
			String manager, String mstatus, int firstResultIndex, int maxResult);
}

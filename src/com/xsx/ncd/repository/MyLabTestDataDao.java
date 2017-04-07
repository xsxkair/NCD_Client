package com.xsx.ncd.repository;

public interface MyLabTestDataDao {
	public Object[] QueryLabDataList(String userid, String deviceid, java.sql.Timestamp startTime, java.sql.Timestamp endTime, 
			int firstResultIndex, int maxResult);
}

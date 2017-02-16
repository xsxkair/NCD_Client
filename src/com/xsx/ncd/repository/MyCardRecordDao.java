package com.xsx.ncd.repository;

import java.util.List;

public interface MyCardRecordDao {
	
	public Object[] queryCardRecord(List<String> userAccount, java.sql.Date startDate, java.sql.Date endDate
			, int firstResultIndex, int maxResult);
}

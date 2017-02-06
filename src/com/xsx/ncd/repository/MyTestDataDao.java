package com.xsx.ncd.repository;

import java.util.List;

public interface MyTestDataDao {
	
	List<Object[]> QueryReportSummyChartData(Integer year, Integer month, String groupType);
	
	Object[] QueryTodayReport(List<Integer> deviceids, int firstResultIndex, int maxResult);
}

package com.xsx.ncd.repository;

import java.util.List;

public interface MyTestDataDao {
	

	public Object[] QueryTodayReport(List<String> deviceids, int firstResultIndex, int maxResult);
	
	public List<Object[]> queryTodayReportGroupByResult(List<String> devices);
	
	public List<Object[]> queryTodayReportGroupByItem(List<String> devices);
	
	public List<Object[]> queryTodayReportGroupByDevice(List<String> devices);
	
	public List<Object[]> queryReportSummy(List<String> devices, String isItem, String dateType);
	
	public Object[] QueryReportList(String item, java.sql.Date testDate, String testerName, String deviceId, 
			List<String> devices, String sampleId, String result, int firstResultIndex, int maxResult);
}

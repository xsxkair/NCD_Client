package com.xsx.ncd.repository;

import java.util.List;

public interface MyTestDataDao {
	
	public List<Object[]> QueryReportSummyChartData(Integer year, Integer month, String groupType);
	
	public Object[] QueryTodayReport(List<Integer> deviceids, int firstResultIndex, int maxResult);
	
	public List<Object[]> queryTodayReportGroupByResult(List<Integer> devices);
	
	public List<Object[]> queryTodayReportGroupByItem(List<Integer> devices);
	
	public List<Object[]> queryTodayReportGroupByDevice(List<Integer> devices);
	
	public List<Object[]> queryReportSummy(List<Integer> devices, String isItem, String dateType);
	
	public Object[] QueryReportList(String item, java.sql.Date testDate, String testerName, Integer deviceId, 
			List<Integer> devices, String sampleId, String result, int firstResultIndex, int maxResult);
}

package com.xsx.ncd.repository;

import java.util.List;

import com.xsx.ncd.entity.Device;

public interface MyTestDataDao {
	
	List<Object[]> QueryReportSummyChartData(Integer year, Integer month, List<String> itemlist, List<Device> deviceidlist);
	
}

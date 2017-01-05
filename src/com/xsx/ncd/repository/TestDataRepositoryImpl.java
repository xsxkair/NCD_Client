package com.xsx.ncd.repository;

import java.util.List;

import com.xsx.ncd.entity.Device;

public class TestDataRepositoryImpl implements MyTestDataDao{

	@Override
	public List<Object[]> QueryReportSummyChartData(Integer year, Integer month, List<String> itemlist,
			List<Device> deviceidlist) {
		// TODO Auto-generated method stub
		System.out.println(itemlist);
		System.out.println(deviceidlist);
		return null;
	}

}

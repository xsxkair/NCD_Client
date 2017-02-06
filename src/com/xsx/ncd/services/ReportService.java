package com.xsx.ncd.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.xsx.ncd.repository.TestDataRepository;

public class ReportService {
	
	@Autowired
	private TestDataRepository testDataRepository;
	
	public Page<Object[]> QueryTodayReport(List<Integer> deviceids){
		return null;
	}
}

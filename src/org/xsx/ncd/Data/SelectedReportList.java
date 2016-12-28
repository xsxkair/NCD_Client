package org.xsx.ncd.Data;

import java.util.HashMap;
import java.util.Map;

import org.com.xsx.Domain.TestDataBean;


public class SelectedReportList {

	private static SelectedReportList S_SeletedReportList = null;
	
	private Map<String, TestDataBean> SelectedDataMap = new HashMap<>();
	
	private SelectedReportList() {
		
	}
	
	public static SelectedReportList GetInstance(){
		if(S_SeletedReportList == null)
			S_SeletedReportList = new SelectedReportList();
		
		return S_SeletedReportList;
	}

	public Map<String, TestDataBean> getSelectedDataMap() {
		return SelectedDataMap;
	}
}

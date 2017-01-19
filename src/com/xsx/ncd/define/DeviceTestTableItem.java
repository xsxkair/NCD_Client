package com.xsx.ncd.define;

import com.xsx.ncd.entity.LabTestData;

public class DeviceTestTableItem {
	
	private String testDesc;
	private Float tcValue;
	private LabTestData labTestData;
	
	public DeviceTestTableItem(LabTestData labTestData) {
		this.labTestData = labTestData;
	}
	
	public String getTestDesc() {
		return labTestData.getDsc();
	}
	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}
	public Float getTcValue() {
		return labTestData.getT_c();
	}
	public void setTcValue(Float tcValue) {
		this.tcValue = tcValue;
	}
	public LabTestData getLabTestData() {
		return labTestData;
	}
	public void setLabTestData(LabTestData labTestData) {
		this.labTestData = labTestData;
	}
}

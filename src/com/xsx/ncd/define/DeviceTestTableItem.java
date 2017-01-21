package com.xsx.ncd.define;

import com.xsx.ncd.entity.LabTestData;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeviceTestTableItem {
	
	private StringProperty testDesc;
	private FloatProperty tcValue;
	private LabTestData labTestData;
	
	public DeviceTestTableItem(LabTestData labTestData) {
		testDesc = new SimpleStringProperty();
		tcValue = new SimpleFloatProperty();
		this.labTestData = labTestData;
	}

	public LabTestData getLabTestData() {
		return labTestData;
	}
	public void setLabTestData(LabTestData labTestData) {
		this.labTestData = labTestData;
	}

	public StringProperty getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc.set(testDesc);;
	}

	public FloatProperty getTcValue() {
		return tcValue;
	}

	public void setTcValue(Float tcValue) {
		this.tcValue.set(tcValue);;
	}
	
}

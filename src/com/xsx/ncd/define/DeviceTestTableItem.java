package com.xsx.ncd.define;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

public class DeviceTestTableItem {
	
	private SimpleStringProperty testDesc;
	private SimpleFloatProperty tcValue;

	public DeviceTestTableItem() {
		this.testDesc = new SimpleStringProperty();
		this.tcValue = new SimpleFloatProperty();
	}

	public SimpleStringProperty testDescProperty() {
		return testDesc;
	}

	public SimpleFloatProperty tcValueProperty() {
		return tcValue;
	}
	
}

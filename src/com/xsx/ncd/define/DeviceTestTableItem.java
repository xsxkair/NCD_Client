package com.xsx.ncd.define;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DeviceTestTableItem {
	
	private SimpleStringProperty testDesc;
	private SimpleFloatProperty tcValue;
	
	private SimpleIntegerProperty tValue;
	private SimpleIntegerProperty cValue;
	private SimpleIntegerProperty bValue;

	public DeviceTestTableItem() {
		this.testDesc = new SimpleStringProperty();
		this.tcValue = new SimpleFloatProperty();
		this.tValue = new SimpleIntegerProperty(-1);
		this.cValue = new SimpleIntegerProperty(-1);
		this.bValue = new SimpleIntegerProperty(-1);
	}

	public SimpleStringProperty testDescProperty() {
		return testDesc;
	}

	public SimpleFloatProperty tcValueProperty() {
		return tcValue;
	}
	
	public void settcValue( float value) {
		tcValue.set(value);
	}
	public float gettcValue() {
		return tcValue.get();
	}

	public int gettValue() {
		return tValue.get();
	}

	public void settValue(int tValue) {
		this.tValue.set(tValue);
	}

	public int getcValue() {
		return cValue.get();
	}

	public void setcValue(int cValue) {
		this.cValue.set(cValue);
	}

	public int getbValue() {
		return bValue.get();
	}

	public void setbValue(int bValue) {
		this.bValue.set(bValue);
	}

	@Override
	public String toString() {
		return "DeviceTestTableItem [testDesc=" + testDesc.get() + ", tcValue=" + tcValue.get() + "]";
	}
	
	
}

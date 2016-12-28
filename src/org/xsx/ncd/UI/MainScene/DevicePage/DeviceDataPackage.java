package org.xsx.ncd.UI.MainScene.DevicePage;

import java.util.List;

import com.xsx.ncd.entity.Device;


public class DeviceDataPackage {
	private Device deviceBean;
	private List<Device> devicerlist;
	
	public DeviceDataPackage(Device deviceBean, List<Device> devicerlist) {
		this.deviceBean = deviceBean;
		this.devicerlist = devicerlist;
	}

	public Device getDeviceBean() {
		return deviceBean;
	}

	public void setDeviceBean(Device deviceBean) {
		this.deviceBean = deviceBean;
	}

	public List<Device> getDevicerlist() {
		return devicerlist;
	}

	public void setDevicerlist(List<Device> devicerlist) {
		this.devicerlist = devicerlist;
	}
}

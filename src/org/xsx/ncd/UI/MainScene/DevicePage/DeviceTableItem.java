package org.xsx.ncd.UI.MainScene.DevicePage;

import java.util.List;

import com.xsx.ncd.entity.Device;

import javafx.scene.image.Image;

public class DeviceTableItem {
	
	private Image deviceico;
	private String deviceid;
	private String devicemanagername;
	private String devicemanagerphone;
	private String deviceaddr;
	private String devicestatus;
	
	private DeviceThumnPane devicethumn;
	
	private Device devicebean;					//设备信息
	
	public DeviceTableItem(Device deviceBean){
		this.setDevicebean(deviceBean);
	}
	
	public Image getDeviceico() {
		return deviceico;
	}

	public void setDeviceico(Image deviceico) {
		this.deviceico = deviceico;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getDevicemanagername() {
		return devicemanagername;
	}

	public void setDevicemanagername(String devicemanagername) {
		this.devicemanagername = devicemanagername;
	}

	public String getDevicemanagerphone() {
		return devicemanagerphone;
	}

	public void setDevicemanagerphone(String devicemanagerphone) {
		this.devicemanagerphone = devicemanagerphone;
	}

	public String getDeviceaddr() {
		return deviceaddr;
	}

	public void setDeviceaddr(String deviceaddr) {
		this.deviceaddr = deviceaddr;
	}

	public String getDevicestatus() {
		return devicestatus;
	}

	public void setDevicestatus(String devicestatus) {
		this.devicestatus = devicestatus;
	}

	public DeviceThumnPane getDevicethumn() {
		return devicethumn;
	}

	public void setDevicethumn(DeviceThumnPane devicethumn) {
		this.devicethumn = devicethumn;
	}

	public Device getDevicebean() {
		return devicebean;
	}

	public void setDevicebean(Device devicebean) {
		this.devicebean = devicebean;
		
		long currenttime = System.currentTimeMillis();
		Long devicetime = devicebean.getTime();

		if((devicetime == null) || ((currenttime > devicetime) && (currenttime - devicetime > 120000))){
			this.setDeviceico(new Image(this.getClass().getResourceAsStream("/RES/deviceico_off.png")));
			this.setDevicestatus("离线");
		}
		else{
			this.setDeviceico(new Image(this.getClass().getResourceAsStream("/RES/deviceico_on.png")));
			if(devicebean.getStatus().equals("ok"))
				this.setDevicestatus("正常");
			else
				this.setDevicestatus("异常");
		}
		
		this.setDeviceid(devicebean.getDid());
		this.setDeviceaddr(devicebean.getAddr());
		this.setDevicemanagername(devicebean.getName());
		this.setDevicemanagerphone(devicebean.getPhone());
		
		this.setDevicethumn(new DeviceThumnPane(this.getDeviceico(), this.getDeviceid()));
	}

}

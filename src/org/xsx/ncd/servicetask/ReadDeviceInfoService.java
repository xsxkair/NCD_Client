package org.xsx.ncd.servicetask;

import java.util.List;

import org.com.xsx.Dao.DeviceInfoDao;
import org.com.xsx.Domain.DeviceBean;
import org.xsx.ncd.Data.SignedManager;
import org.xsx.ncd.UI.MainScene.DevicePage.DeviceDataPackage;
import org.xsx.ncd.UI.MainScene.DevicePage.DeviceTableItem;
import org.xsx.ncd.UI.MainScene.DevicePage.DeviceThumnPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class ReadDeviceInfoService extends ScheduledService<ObservableList<DeviceTableItem>>{
	
	@Override
	protected Task<ObservableList<DeviceTableItem>> createTask() {
		// TODO Auto-generated method stub
		return new ReadDeviceInfoTask();
	}
	
	class ReadDeviceInfoTask extends Task<ObservableList<DeviceTableItem>>{

		@Override
		protected ObservableList<DeviceTableItem> call(){
			// TODO Auto-generated method stub
			return ReadDeviceInfoFun();
		}
		
		private ObservableList<DeviceTableItem> ReadDeviceInfoFun(){
			ObservableList<DeviceTableItem> deviceTableItems = FXCollections.observableArrayList();
			
			List<DeviceBean> devices = DeviceInfoDao.QueryDeviceList(SignedManager.GetInstance().getGB_SignedAccount());

			for (DeviceBean deviceinfo : devices) {
				
				DeviceTableItem temp = new DeviceTableItem(deviceinfo);

				deviceTableItems.add(temp);
			}

			return deviceTableItems;
		}
	}
}

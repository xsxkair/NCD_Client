package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

@Component
public class DeviceHandler {
	
	private AnchorPane devicepane;
		
	@FXML ScrollPane DeviceICOShowRootPane;
	@FXML FlowPane DeviceThumbShowPane;
	@FXML StackPane GB_FreshPane;
	
	@Autowired
	private WorkPageSession workPageSession;
	
	@Autowired
	private UserRepository managerRepository;
	
	@Autowired
	private UserSession managerSession;
	
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private DeviceDetailHandler deviceDetailHandler;
	
	//更新设备状态任务
	private ReadDeviceInfoService S_ReadDeviceInfoService;
	
	@PostConstruct
	private void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/DevicePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/DevicePage.fxml");
        loader.setController(this);
        try {
        	devicepane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        S_ReadDeviceInfoService = new ReadDeviceInfoService();
        S_ReadDeviceInfoService.setPeriod(Duration.minutes(5));
        
        GB_FreshPane.visibleProperty().bind(S_ReadDeviceInfoService.stateProperty().isEqualTo(new SimpleObjectProperty<State>(State.RUNNING)));
        
        S_ReadDeviceInfoService.lastValueProperty().addListener(new ChangeListener<List<Device>>() {

			@Override
			public void changed(ObservableValue<? extends List<Device>> arg0, List<Device> arg1, List<Device> arg2) {
				// TODO Auto-generated method stub
				long currenttime = System.currentTimeMillis();
				Long devicetime = null;
				DeviceThumbShowPane.getChildren().clear();
				
				if(arg2 != null){

					for (Device device : arg2) {
						
						Image image = null;
						devicetime = device.getTime();

						if((devicetime == null) || ((currenttime > devicetime) && (currenttime - devicetime > 120000))){
							image = new Image(this.getClass().getResourceAsStream("/RES/deviceico_off.png"));
						}
						else if("OK".equals(device.getStatus())){
							image = new Image(this.getClass().getResourceAsStream("/RES/deviceico_ok.png"));
						}
						else {
							image = new Image(this.getClass().getResourceAsStream("/RES/deviceico_error.png"));
						}
						
						DeviceThumnPane temp = new DeviceThumnPane(image, device);
						
						temp.setCursor(Cursor.HAND);
						
				        temp.setOnMouseClicked(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent event) {
								// TODO Auto-generated method stub
								deviceDetailHandler.ShowDeviceDetail((Device) temp.getUserData());
							}
						});
				        
						DeviceThumbShowPane.getChildren().add(temp);
					}
				}
			}
		});
        
       workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(devicepane.equals(newValue)){
					S_ReadDeviceInfoService.restart();
				}
				
				if(devicepane.equals(oldValue)){
					S_ReadDeviceInfoService.cancel();
				}
			}
		});
        
        AnchorPane.setTopAnchor(devicepane, 0.0);
        AnchorPane.setBottomAnchor(devicepane, 0.0);
        AnchorPane.setLeftAnchor(devicepane, 0.0);
        AnchorPane.setRightAnchor(devicepane, 0.0);

	}
	
	public void showDeviceListPane(){
		workPageSession.setWorkPane(devicepane);
	}
	
	class ReadDeviceInfoService extends ScheduledService<List<Device>>{
		
		@Override
		protected Task<List<Device>> createTask() {
			// TODO Auto-generated method stub
			return new ReadDeviceInfoTask();
		}
		
		class ReadDeviceInfoTask extends Task<List<Device>>{

			@Override
			protected List<Device> call(){
				// TODO Auto-generated method stub
				return ReadDeviceInfoFun();
			}
			
			private List<Device> ReadDeviceInfoFun(){
				
				//管理员
				User admin;
				
				if(managerSession.getFatherAccount() == null)
					admin = managerRepository.findByAccount(managerSession.getAccount());
				else
					admin = managerRepository.findByAccount(managerSession.getFatherAccount());
				
				if(admin == null)
					return null;
				
				//查询管理员所管理的所有设备id
				List<Device> deviceList = deviceRepository.findByAccount(admin.getAccount());
				
				return deviceList;
			}
		}
	}
}

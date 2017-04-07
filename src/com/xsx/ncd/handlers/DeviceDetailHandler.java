package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXToggleNode;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.NcdSoft;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.NcdSoftRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

@Component
public class DeviceDetailHandler {
	
	private AnchorPane rootpane;
	@FXML StackPane rootStackPane;
	
	private Pane fatherPane;
	
	private Device S_Device = null;
	private User currentUser = null;
	private User tempUser = null;
	
	@FXML JFXDialog setManagerDialog;
	@FXML JFXComboBox<User> managerListComboBox;
	@FXML JFXPasswordField actionPasswordTextField;
	@FXML JFXButton cancelButton1;
	@FXML JFXButton acceptButton1;
	
	@FXML JFXDialog LogDialog;
	@FXML Label dialogInfo;
	@FXML JFXButton acceptButton2;
	
	@FXML HBox deviceInfoHBox;
	@FXML ImageView GB_DeviceImg;
	@FXML Label GB_DeviceIDLabel;
	@FXML Label GB_DeviceVersionLabel;
	@FXML Label GB_DevicerNameLabel;
	@FXML Label GB_DevicerPhoneLabel;
	@FXML Label GB_DevicerAddrLabel;
	
	@FXML VBox deviceInfoVBox;
	@FXML HBox managerHBox;
	@FXML Label GB_managerNameLabel;
	@FXML Label GB_managerPhoneLabel;
	@FXML JFXButton setManagerButton;
	
	@FXML ToggleGroup viewTypeToggleGroup;
	@FXML LineChart<String, Number> GB_DeviceLineChart;
	private Series<String, Number> chartseries;
	@FXML CategoryAxis GB_DeviceXaxis;
	@FXML NumberAxis GB_DeviceYaxis;
	@FXML VBox GB_FreshPane;
	
	private ContextMenu myContextMenu;
	private MenuItem myMenuItem1;
	private MenuItem myMenuItem2;
	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private DeviceRepository deviceRepository;
	@Autowired private NcdSoftRepository ncdSoftRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private UserSession userSession;
	
	private QueryDeviceActivenessService s_QueryDeviceActivenessService;
	
	@PostConstruct
	public void UI_Init(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/DeviceDetialPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/DeviceDetialPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootStackPane.getChildren().remove(LogDialog);
        rootStackPane.getChildren().remove(setManagerDialog);
        
        acceptButton1.disableProperty().bind(new BooleanBinding() {
			
        	{
        		bind(managerListComboBox.getSelectionModel().selectedItemProperty());
        		bind(actionPasswordTextField.lengthProperty());
        	}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				if((managerListComboBox.getSelectionModel().getSelectedItem() != null) && (actionPasswordTextField.getLength() > 0))
					return false;
				else
					return true;
			}
		});
        
        myMenuItem1 = new MenuItem("刷新");
        myMenuItem2 = new MenuItem("返回");
        myContextMenu = new ContextMenu(myMenuItem1, myMenuItem2);
        
        viewTypeToggleGroup.selectedToggleProperty().addListener((o, oldValue, newValue)->{
        	s_QueryDeviceActivenessService.restart();
        });
        
        chartseries = new Series<>();
       
        GB_DeviceLineChart.getData().add(chartseries);
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue) ->{
        	
        	if(rootpane.equals(newValue)){
        		fatherPane = oldValue;
        		
        		currentUser = userRepository.findByAccount(userSession.getAccount());
        		deviceInfoHBox.getChildren().remove(setManagerButton);
        		deviceInfoVBox.getChildren().remove(managerHBox);
        		if(currentUser.getType() <= 2){
        			deviceInfoHBox.getChildren().add(setManagerButton);
        			deviceInfoVBox.getChildren().add(3, managerHBox);
        		}
        		
        		UpDeviceInfo();
        		
        		viewTypeToggleGroup.selectToggle(viewTypeToggleGroup.getToggles().get(1));
			}
        	else if (rootpane.equals(oldValue)) {
        		s_QueryDeviceActivenessService.cancel();
				S_Device = null;
				tempUser = null;
				currentUser = null;
				chartseries.getData().clear();
			}
        });

        rootpane.getStylesheets().add(this.getClass().getResource("/com/xsx/ncd/views/devicedetial.css").toExternalForm());
        
        rootpane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if(event.getButton().equals(MouseButton.SECONDARY)){
					myContextMenu.show(rootpane, event.getScreenX(), event.getScreenY());
				}
				else
					myContextMenu.hide();
			}
		});
        
        //刷新
        myMenuItem1.setOnAction(new EventHandler<ActionEvent>() {

  			@Override
  			public void handle(ActionEvent event) {
  				// TODO Auto-generated method stub
  				UpDeviceInfo();
        		
  				s_QueryDeviceActivenessService.restart();
  			}
  		});
      		
      	//返回
        myMenuItem2.setOnAction(new EventHandler<ActionEvent>() {

  			@Override
  			public void handle(ActionEvent event) {
  				// TODO Auto-generated method stub
  				workPageSession.setWorkPane(fatherPane);
  			}
  		});	
        
        s_QueryDeviceActivenessService = new QueryDeviceActivenessService();
        GB_FreshPane.visibleProperty().bind(s_QueryDeviceActivenessService.runningProperty());
        s_QueryDeviceActivenessService.valueProperty().addListener((o, oldValue, newValue)->{
        	chartseries.getData().clear();
        	
        	if(newValue != null){
        		for (Object[] objects : newValue) {
        			String timelabel = (String) objects[0];
        			Long num = (Long) objects[1];
        			
        			if(timelabel != null){
        				Data<String, Number> point = new Data<String, Number>(timelabel, num.intValue());
        				StackPane pointui = new StackPane();
        				pointui.getStyleClass().add("chartpoint");
        				point.setNode(pointui);
        				
        				Label tiplabel = new Label("日期："+timelabel+"\n"+"数目："+num.intValue());
        				tiplabel.setFont(new Font("System", 16));
        				
        				Tooltip tooltip = new Tooltip();
        				tooltip.setGraphic(tiplabel);
        		        Tooltip.install(pointui, tooltip);

        				chartseries.getData().add(point);
        			}
        		}
        		
        		chartseries.getNode().getStyleClass().add("chartstyle");
        	}
        });
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
        
        loader = null;
        in = null;
	}
	
	public void ShowDeviceDetail(Device device){
		
		S_Device = device;
		
		workPageSession.setWorkPane(rootpane);
	}
	
	private void UpDeviceInfo() {
		
		Image image = null;
		
		Long devicetime = S_Device.getTime();
		long currenttime = System.currentTimeMillis();

		if((devicetime == null) || ((currenttime > devicetime) && (currenttime - devicetime > 120000))){
			image = new Image(this.getClass().getResourceAsStream("/RES/deviceico_off.png"));
		}
		else if("ok".equals(S_Device.getStatus())){
			image = new Image(this.getClass().getResourceAsStream("/RES/deviceico_ok.png"));
		}
		else {
			image = new Image(this.getClass().getResourceAsStream("/RES/deviceico_error.png"));
		}
		
		GB_DeviceImg.setImage(image);
		
		GB_DeviceIDLabel.setText(S_Device.getDid());
		
		Integer version = S_Device.getDversion();
		
		if(version != null){
			
			StringBuffer tempStr = new StringBuffer();
			tempStr.append(String.format("V%d.%d.%02d", version/1000, version%1000/100, version%100));
			
			//读取设备程序最新版本信息
			NcdSoft ncdSoft = ncdSoftRepository.findNcdSoftByName("Device");
			if((ncdSoft != null) && (ncdSoft.getVersion().intValue() > version.intValue()))
			{
				tempStr.append(String.format("( V%d.%d.%02d )", ncdSoft.getVersion()/1000, ncdSoft.getVersion()%1000/100, 
						ncdSoft.getVersion()%100));
			}

			GB_DeviceVersionLabel.setText(tempStr.toString());
			
			tempStr = null;
		}
		else
			GB_DeviceVersionLabel.setText("无");

		GB_DevicerNameLabel.setText(S_Device.getName());
		GB_DevicerPhoneLabel.setText(S_Device.getPhone());
		GB_DevicerAddrLabel.setText(S_Device.getAddr());

		GB_managerNameLabel.setText("无");
		GB_managerPhoneLabel.setText("无");
		if(S_Device.getAccount() != null){
			tempUser = userRepository.findByAccount(S_Device.getAccount());
			
			if(tempUser != null){
				GB_managerNameLabel.setText(tempUser.getName());
				GB_managerPhoneLabel.setText(tempUser.getPhone());
			}
		} 
	}
	
	@FXML
	public void setManagerAction() {
		
		managerListComboBox.getItems().setAll(userRepository.queryAllFatherManager());
		actionPasswordTextField.clear();
		
		setManagerDialog.show(rootStackPane);
	}
	
	@FXML
	public void saveManagergAction() {
		
		if(currentUser.getPassword().equals(actionPasswordTextField.getText())){
			S_Device.setAccount(managerListComboBox.getSelectionModel().getSelectedItem().getAccount());
			
			deviceRepository.save(S_Device);
			
			dialogInfo.setText("成功！");
			LogDialog.show(rootStackPane);
		}
		else {
			dialogInfo.setText("密码错误！");
			LogDialog.show(rootStackPane);
		}
	}

	@FXML
	public void closeDialogAction() {
		if(setManagerDialog.isVisible())
			setManagerDialog.close();
		
		if(LogDialog.isVisible())
			LogDialog.close();
	}
	
	//查询当前设备的活跃度
	class QueryDeviceActivenessService extends Service<List<Object[]>>{

		@Override
		protected Task<List<Object[]>> createTask() {
				// TODO Auto-generated method stub
			return new QueryReportTask();
		}
			
		class QueryReportTask extends Task<List<Object[]>>{
				
			@Override
			protected List<Object[]> call(){
					// TODO Auto-generated method stub

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JFXToggleNode tempToggleNode = (JFXToggleNode) viewTypeToggleGroup.getSelectedToggle();
				String viewType = ((Label)(tempToggleNode.getGraphic())).getText();
	        	if(viewType.equals("年")){
	        		return deviceRepository.queryDeviceActivenessByYear(S_Device.getDid());
	        	}
	        	else if(viewType.equals("月")){
	        		return deviceRepository.queryDeviceActivenessByMonth(S_Device.getDid());
	        	}
	        	else {
	        		return deviceRepository.queryDeviceActivenessByDay(S_Device.getDid());
	        	}
			}
		}
	}	
	
}

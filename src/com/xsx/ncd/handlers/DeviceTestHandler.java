package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.xsx.ncd.define.DeviceTestTableItem;
import com.xsx.ncd.define.ReportTableItem;
import com.xsx.ncd.entity.LabTestData;
import com.xsx.ncd.repository.LabTestDataRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.ClientSocket;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class DeviceTestHandler {
	
	private AnchorPane rootPane = null;
	
	@FXML StackPane rootStackPane;
	
	@FXML JFXDialog logDialog;
	@FXML Label dialogInfo;
	@FXML JFXButton acceptButton;
	
	@FXML TextField GB_TestCountFiled;
	@FXML TextField GB_TestRelayFiled;
	@FXML TextField GB_DeviceIPField;
	@FXML Button GB_StartTestButton;
	
	@FXML LineChart<Number, Number> GB_Chart;
	
	private ObservableList<Integer> tempSeriesDataList = null;
	private List<LabTestData> labTestDatas = null;
	private String deviceId = null;
	private Integer testCount = null;
	private Integer testDelayTime = null;

	private IntegerProperty testIndex = null;
	private Series<Number, Number> currentSeries = null;
	private Series<Number, Number> tempSeries = null;
	
	private JSONObject  jsonarray = null;
	private Map typeMap = new HashMap();
	
	@FXML TableView<DeviceTestTableItem> GB_TableView;
	@FXML TableColumn<DeviceTestTableItem, String> GB_TableColumn0;
	@FXML TableColumn<DeviceTestTableItem, Float> GB_TableColumn1;
	
	@FXML TextArea GB_LogTextArea;
	
	String LastSaveDirectory = null;
	private final Integer retryMaxNum = 10;
	
	private TestService S_TestService = null;
	
	@Autowired WorkPageSession workPageSession;
	@Autowired private LabTestDataRepository labTestDataRepository;
	@Autowired UserSession userSession;

	@PostConstruct
	private void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/DeviceTestPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/DeviceTestPage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		rootStackPane.getChildren().remove(logDialog);
		
		GB_TableColumn0.setCellValueFactory(new PropertyValueFactory<DeviceTestTableItem, String>("testDesc"));
		GB_TableColumn0.setCellFactory(TextFieldTableCell.forTableColumn());
		
        GB_TableColumn1.setCellValueFactory(new PropertyValueFactory<DeviceTestTableItem, Float>("tcValue"));
	
        labTestDatas = new ArrayList<>();
        
        testIndex = new SimpleIntegerProperty();
        testIndex.addListener((o, oldValue, newValue)->{
        	System.out.println(newValue);
        	if(newValue != null){
        		System.out.println(newValue.intValue());
        		currentSeries = GB_Chart.getData().get(newValue.intValue());
        	}
        });
        
        tempSeriesDataList = FXCollections.observableArrayList();

        tempSeriesDataList.addListener(new ListChangeListener<Integer>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Integer> c) {
				// TODO Auto-generated method stub
				if (c.next()) {
					if(c.wasAdded()){
						for (Integer additem : c.getAddedSubList()) {
							/*if(additem == 0xffff)
								currentSeries.getData().clear();
							else
								currentSeries.getData().add(new Data<Number, Number>(currentSeries.getData().size(), additem));*/
	                    }
					}
				}
				
					
			}
		});
        
        typeMap.put("data", int[].class);
    	typeMap.put("status", int.class);
        
        S_TestService = new TestService();
        S_TestService.messageProperty().addListener((o, oldValue, newValue)->{
        	GB_LogTextArea.appendText(newValue+"\n");
        });

        GB_StartTestButton.disableProperty().bind(S_TestService.runningProperty());
        GB_TestCountFiled.disableProperty().bind(S_TestService.runningProperty());
        
        GB_TestRelayFiled.setText("1");
        GB_TestRelayFiled.disableProperty().bind(S_TestService.runningProperty());
        GB_DeviceIPField.disableProperty().bind(S_TestService.runningProperty()); 
        
        acceptButton.setOnMouseClicked((e)->{
        	logDialog.close();
		});
        
        loader = null;
        in = null;
	}
	
	public void showDeviceTestPage(){
		workPageSession.setWorkPane(rootPane);
	}
	
	private void showLogsDialog(String logs) {
		dialogInfo.setText(logs);
		logDialog.show(rootStackPane);
	}
	
	public boolean readDeviceInfo(String iphost) {
		String devicestr = null;
		JSONObject jsonObject = null;
    	Map typeMap = new HashMap();
    	
		devicestr = ClientSocket.ComWithServer(iphost, "Read Device Info");
		
		if(devicestr != null){
			jsonObject = JSONObject.fromObject(devicestr);
			Map output1 = (Map)JSONObject.toBean(jsonObject, Map.class,typeMap);
			deviceId = (String)output1.get("deviceid");
			
			typeMap = null;
			jsonObject = null;
			return true;
		}
		typeMap = null;
		jsonObject = null;
		return false;
	}

	@FXML
	public void GB_SelectSeriesAction(MouseEvent e){
/*		MouseButton button = e.getButton();
		if(button.equals(MouseButton.SECONDARY)){
			ShowSeriesIndex = 0;
			 for (TestDataItem testDataItem : GB_TestData) {
				 testDataItem.ShowSeries();
			}
		}
		else if(button.equals(MouseButton.MIDDLE)){
			ShowSeriesIndex++;
			if(ShowSeriesIndex >= GB_TestData.size())
				ShowSeriesIndex = 0;
			for(int i=0; i< GB_TestData.size(); i++){
				if(i != ShowSeriesIndex)
					GB_TestData.get(i).HideSeries();
				else
					GB_TestData.get(i).ShowSeries();
			}
		}*/
	}
	
	@FXML
	public void GB_StartTestAction(ActionEvent e){
	
		testIndex.set((int) (Math.random()*100));
/*		rootPane.setCursor(Cursor.WAIT);
		
		LabTestData labTestData = null;
		
		try {
			testCount = Integer.valueOf(GB_TestCountFiled.getText());
			testDelayTime = Integer.valueOf(GB_TestRelayFiled.getText());
		} catch (Exception ex) {
			// TODO: handle exception
			testCount = null;
			testDelayTime = null;
		}
		
		if((testCount == null) || (testDelayTime == null)){
			rootPane.setCursor(Cursor.DEFAULT);
			
			showLogsDialog("格式不正确！");
			return;
		}
		
		if(!readDeviceInfo(GB_DeviceIPField.getText())){
			rootPane.setCursor(Cursor.DEFAULT);
			
			showLogsDialog("IP无法连接！");
			return;
		}
		
		labTestDatas.clear();
		GB_Chart.getData().clear();
		GB_TableView.getItems().clear();
		for (int i=0; i<testCount.intValue(); i++) {
			tempSeries = new Series<>();
			tempSeries.setName(String.format("%d", i+1));
			GB_Chart.getData().add(tempSeries);
			GB_TableView.getItems().add(new DeviceTestTableItem());
			
			labTestData = new LabTestData();
			labTestData.setDid(deviceId);
			labTestData.setUserid(userSession.getAccount());
			labTestData.setTindex(String.format("%d/%d", i+1, testCount));
			labTestDatas.add(labTestData);
		}
		
		S_TestService.restart();
		
		rootPane.setCursor(Cursor.DEFAULT);*/
	}
	
	class TestService extends Service<Void>{
		
		@Override
		protected Task<Void> createTask() {
			// TODO Auto-generated method stub
			return new TestTask();
		}
		
		class TestTask extends Task<Void>{

			@Override
			protected Void call(){
				// TODO Auto-generated method stub
				return TestFun();
			}
			
			private Void TestFun(){
				
				String str;
				int retryNum = 0;
				DeviceTestTableItem deviceTestTableItem = null;

				LabTestData labTestData = null;
				
	        	testIndex.set(0);

				while(true){

					labTestData = labTestDatas.get(testIndex.get());
					
					//启动测试
					retryNum = 0;
					while((retryNum++) < retryMaxNum){
						str = ClientSocket.ComWithServer(GB_DeviceIPField.getText(), "Start Test");
						
						if("OK".equals(str)){
							updateMessage("启动成功！");
							break;
						}
						else if("Startted".equals(str)){
							updateMessage("设备忙！");
							retryNum = 100;
						}
						else {
							updateMessage("无响应 -- "+retryNum);
						}
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(retryNum > retryMaxNum){
						return null;
					}
					
					//读曲线数据
					retryNum = 0;

					while(retryNum < retryMaxNum){
						str = ClientSocket.ComWithServer(GB_DeviceIPField.getText(), "Read Test Data");
						if(str != null){
							retryNum = 0;
							System.out.println(str);
							jsonarray = JSONObject.fromObject(str);
							Map output1 = (Map)JSONObject.toBean(jsonarray, Map.class, typeMap);

							List<Integer> data = (List<Integer>) output1.get("data");
							int status = (int) output1.get("status");
							
							
							try {
								tempSeriesDataList.addAll(data);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							
							
							data = null;
							jsonarray = null;
							
							if(status == 0){
								JSONArray jsonList = JSONArray.fromObject( currentSeries.getData().toArray() );
								labTestData.setSerie(jsonList.toString());
								
								try {
									labTestDataRepository.save(labTestData);
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
								break;
							}
						}
						else{
							updateMessage("无响应 -- "+retryNum);
							retryNum++;
						}
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(retryNum > retryMaxNum){
						return null;
					}
					
					testIndex.add(1);
					System.out.println(testIndex.get());
					System.out.println(testCount);
					
					if(testIndex.get() >= testCount)
						break;

					try {
						Thread.sleep(testDelayTime*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return null;
			}
		}
	}
}

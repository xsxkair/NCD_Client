package com.xsx.ncd.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.xsx.beans.DataPointUI;
import com.org.xsx.dao.TestDataDao;
import com.xsx.ncd.define.DeviceTestTableItem;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.handlers.DeviceHandler.ReadDeviceInfoService.ReadDeviceInfoTask;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.ClientSocket;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class DeviceTestHandler {
	
	private AnchorPane rootPane = null;
	@FXML LineChart<Number, Number> GB_Chart;
	
	@FXML TextField GB_TestCountFiled;
	@FXML TextField GB_TestRelayFiled;
	@FXML TextField GB_DeviceIPField;
	@FXML Button GB_StartTestButton;
	@FXML Button GB_SaveDataButton;
	private Integer testCount = null;
	private Integer testDelayTime = null;
	
	@FXML TableView<DeviceTestTableItem> GB_TableView;
	@FXML TableColumn<DeviceTestTableItem, String> GB_TableColumn0;
	@FXML TableColumn<DeviceTestTableItem, Float> GB_TableColumn1;
	
	@FXML TextArea GB_LogTextArea;
	
	String LastSaveDirectory = null;
	private final Integer retryMaxNum = 10;
	
	private TestService S_TestService = null;
	
	@Autowired
	WorkPageSession workPageSession;

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
		
		GB_TableColumn0.setCellValueFactory(new PropertyValueFactory<DeviceTestTableItem, String>("testDesc"));
		GB_TableColumn0.setCellFactory(TextFieldTableCell.forTableColumn());
		
        GB_TableColumn1.setCellValueFactory(new PropertyValueFactory<DeviceTestTableItem, Float>("tcValue"));
	
        S_TestService = new TestService();
        S_TestService.messageProperty().addListener((o, oldValue, newValue)->{
        	GB_LogTextArea.appendText(newValue+"\n");
        });
        GB_StartTestButton.disableProperty().bind(S_TestService.runningProperty());
        GB_SaveDataButton.disableProperty().bind(S_TestService.runningProperty());
        GB_TestCountFiled.disableProperty().bind(S_TestService.runningProperty());
        GB_TestRelayFiled.disableProperty().bind(S_TestService.runningProperty());
        GB_DeviceIPField.disableProperty().bind(S_TestService.runningProperty());
	}
	
	public void showDeviceTestPage(){
		workPageSession.setWorkPane(rootPane);
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
		try {
			testCount = Integer.valueOf(GB_TestCountFiled.getText());
		} catch (Exception exception) {
			// TODO: handle exception
			return;
		}
		
		if(testCount.intValue() <= 0)
			return;
		
		GB_Chart.getData().clear();
		for (int i=0; i<testCount.intValue(); i++) {
			GB_Chart.getData().add(new Series<>());
			
		}
		Series<Number,Number>[] series = new Series[testCount];
		
		try {
			testDelayTime = Integer.valueOf(GB_TestRelayFiled.getText());
		} catch (Exception exception) {
			// TODO: handle exception
			testDelayTime = 1;
		}
		
		if(testDelayTime <= 0)
			testDelayTime = 1;
		
		S_TestService.start();
	}
	
	@FXML
	public void GB_SaveDataAction(ActionEvent e){

	}
	
/*	public void SaveData_Fun(String filepath){

		ButtonType loginButtonType = new ButtonType("确定", ButtonData.OK_DONE);
		Dialog<String> dialog = new Dialog<>();
		
		dialog.getDialogPane().getButtonTypes().add(loginButtonType);
		
		
		FileInputStream myfileinput = null;
		try {
			myfileinput = new FileInputStream(filepath);	
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block

		}
		
		XSSFWorkbook wb = null;
		if(myfileinput != null){
			try {
				wb = new XSSFWorkbook(myfileinput);
			} catch (IOException e1) {
					// TODO Auto-generated catch block
				dialog.getDialogPane().setContentText("文件打开错误，请重试！");
				dialog.showAndWait();
				return;
			}
		}
		else{
			wb = new XSSFWorkbook();
		}

		XSSFSheet mysheet = null;
		
		if(wb.getNumberOfSheets() == 0){
			mysheet = wb.createSheet(S_TesterBean.get().getUsername());
		}
		else {
			mysheet = wb.getSheetAt(wb.getNumberOfSheets()-1);
		}
		
		XSSFRow myrow0 = mysheet.getRow(0);
		if(myrow0 == null)
			myrow0 = mysheet.createRow(0);
		int rownum = myrow0.getLastCellNum();
		if(rownum == -1)
			rownum += 1;
		
		for (int i = 0; i < GB_TestData.size(); i++) {
			//第0行
			XSSFRow myrow4 = mysheet.getRow(0);
			if(myrow4 == null)
				myrow4 = mysheet.createRow(0);
			
			XSSFCell Cell0 = myrow4.createCell(i+rownum);
			Cell0.setCellType(HSSFCell.CELL_TYPE_STRING);
			Cell0.setCellValue(GB_TestData.get(i).my_descProperty().get());
			
			//第一行
			XSSFRow myrow1 = mysheet.getRow(1);
			if(myrow1 == null)
				myrow1 = mysheet.createRow(1);
			
			XSSFCell Cell1 = myrow1.createCell(i+rownum);
			Cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
			Cell1.setCellValue(GB_TestData.get(i).getMy_seires().getName());

			//第二行
			XSSFRow myrow2 = mysheet.getRow(2);
			if(myrow2 == null)
				myrow2 = mysheet.createRow(2);
			
			XSSFCell Cell2 = myrow2.createCell(i+rownum);
			Cell2.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			Cell2.setCellValue(GB_TestData.get(i).my_T_C_biliProperty().doubleValue());
			
			//第三行
			XSSFRow myrow3 = mysheet.getRow(3);
			if(myrow3 == null)
				myrow3 = mysheet.createRow(3);
			
			XSSFCell Cell3 = myrow3.createCell(i+rownum);
			Cell3.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			Cell3.setCellValue(GB_TestData.get(i).my_T_TC_biliProperty().doubleValue());
			
			for (int j = 0; j < GB_TestData.get(i).getMy_seires().getData().size(); j++) {
				XSSFRow temprow = mysheet.getRow(j+4);
				if(temprow == null)
					temprow = mysheet.createRow(j+4);
				
				XSSFCell tempcell = temprow.createCell(i+rownum);
				tempcell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				tempcell.setCellValue(GB_TestData.get(i).getMy_seires().getData().get(j).getYValue().doubleValue());
			}
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream(filepath);
			try {
				wb.write(out);
				out.flush();
				out.close();
				LastSaveDirectory = filepath;
				
				dialog.getDialogPane().setContentText("保存成功！");
				dialog.showAndWait();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				dialog.getDialogPane().setContentText("保存失败！");
				dialog.showAndWait();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			dialog.getDialogPane().setContentText("保存失败！");
			dialog.showAndWait();
		}finally {
			try {
				wb.close();
				myfileinput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	*/
	
	class TestService extends ScheduledService<Void>{
		
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
				int testIndex = 0;
				
				JSONObject  jsonarray;
				Map typeMap = new HashMap();
				typeMap.put("data", int[].class);
	        	typeMap.put("status", int.class);
	        	ArrayList<Integer> tempdata = new ArrayList<>();
				
				while((testIndex++) < testCount){
					
					//启动测试
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
					}
					if(retryNum > retryMaxNum){
						this.cancel();
						return null;
					}
					
					//读曲线数据
					retryNum = 0;
					while((retryNum++) < retryMaxNum){
						str = ClientSocket.ComWithServer(GB_DeviceIPField.getText(), "Read Test Data99");
						if(str != null){
							jsonarray = JSONObject.fromObject(str);
							Map output1 = (Map)JSONObject.toBean(jsonarray, Map.class, typeMap);

							List<Integer> data = (List<Integer>) output1.get("data");
							int status = (int) output1.get("status");

							tempdata.addAll(data);
							Platform.runLater(() -> {
								int serieldatasize;
								Series<Number, Number> mySeries = GB_Chart.getData().get(testIndex);
								serieldatasize = mySeries.getData().size();
								for (int i=0; i<data.size(); i++) {
									if(data.get(i) == 0xffff){
										mySeries.getData().clear();
										serieldatasize = mySeries.getData().size();
										tempdata.clear();
									}
									else{
										Data datap = new Data<Number, Number>((serieldatasize+i), data.get(i));
										mySeries.getData().add(datap);
									}
								}
							});
							if(status == 0){
								JSONArray jsonList = JSONArray.fromObject( tempdata );  
								GB_TestData.get(GB_CurrentCount).getMy_TestDataBean().setTestdata(jsonList.toString());
								TestDataDao.SaveTestData(GB_TestData.get(GB_CurrentCount).getMy_TestDataBean());
								
								GB_CurrentCount++;

								if(GB_TestCount > GB_CurrentCount){

									GB_TestStatus.set(1);
									try {
										Thread.sleep(GB_TestRelay*1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								else{
									Platform.runLater(() -> {
										GB_TestStatus.set(0);
									});
								}
										
							}
						}
						else{
							GB_LogTextArea.appendText("无响应-----"+retrycount+"\n");
							retrycount++;
							if(retrycount >= 10){
								Platform.runLater(() -> {
									GB_TestStatus.set(0);
									GB_TestCountFiled.setText(null);
								});
							}
						}
					}
					if(retryNum > retryMaxNum){
						this.cancel();
						return null;
					}
				}
				
				return null;
			}
		}
	}
}

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

import com.xsx.ncd.define.DeviceTestTableItem;
import com.xsx.ncd.entity.LabTestData;
import com.xsx.ncd.repository.LabTestDataRepository;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.ClientSocket;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
	int testIndex = 0;
	
	@FXML TableView<DeviceTestTableItem> GB_TableView;
	@FXML TableColumn GB_TableColumn0;
	@FXML TableColumn GB_TableColumn1;
	
	@FXML TextArea GB_LogTextArea;
	
	String LastSaveDirectory = null;
	private final Integer retryMaxNum = 10;
	
	private TestService S_TestService = null;
	
	@Autowired WorkPageSession workPageSession;
	@Autowired private LabTestDataRepository labTestDataRepository;

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
		
		GB_TableColumn0.setCellValueFactory(new PropertyValueFactory("testDesc"));
		//GB_TableColumn0.setCellFactory(TextFieldTableCell.forTableColumn());
		
        GB_TableColumn1.setCellValueFactory(new PropertyValueFactory("tcValue"));
	
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
		GB_TableView.getItems().clear();
		for (int i=0; i<testCount.intValue(); i++) {
			GB_Chart.getData().add(new Series<>());
			GB_TableView.getItems().add(new DeviceTestTableItem(new LabTestData()));
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
		
		S_TestService.restart();
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
				
				JSONObject  jsonarray;
				Map typeMap = new HashMap();
				typeMap.put("data", int[].class);
	        	typeMap.put("status", int.class);
	        	ArrayList<Integer> tempdata = new ArrayList<>();
	        	DeviceTestTableItem labTestDataItem = null;
				
	        	testIndex = 0;

				while(true){

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
					tempdata.clear();
					while(retryNum < retryMaxNum){
						str = ClientSocket.ComWithServer(GB_DeviceIPField.getText(), "Read Test Data");
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
								labTestDataItem = GB_TableView.getItems().get(testIndex);
								labTestDataItem.setTestDesc(""+testIndex);
								labTestDataItem.setTcValue(new Float(testIndex));
								labTestDataItem.getLabTestData().setSerie(jsonList.toString());
								
								try {
									labTestDataRepository.save(labTestDataItem.getLabTestData());
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
					
					testIndex++;
					
					if(testIndex >= testCount)
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

package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXRadioButton;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.TestDataRepository;
import com.xsx.ncd.spring.ManagerSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

@Component
public class ReportOverViewPage {
	
	private AnchorPane rootpane;
	
	@FXML
	PieChart GB_ReportPieChart;
	private ObservableList<PieChart.Data> GB_ReportPieChartData;

	@FXML
	StackPane GB_FreshPane1;
	@FXML
	ProgressIndicator GB_RefreshBar1;
	
	@FXML
	PieChart GB_ItemPieChart;
	private ObservableList<PieChart.Data> GB_ItemPieChartData;

	@FXML
	StackPane GB_FreshPane2;
	@FXML
	ProgressIndicator GB_RefreshBar2;
	
	@FXML
	PieChart GB_DevicePieChart;
	private ObservableList<PieChart.Data> GB_DevicePieChartData;

	@FXML
	StackPane GB_FreshPane3;
	@FXML
	ProgressIndicator GB_RefreshBar3;
	
	@FXML
	TextField GB_YearTextField;
	@FXML
	ComboBox<Integer> GB_MonthComboBox;
	@FXML JFXRadioButton GB_GroupByItem;
	@FXML JFXRadioButton GB_GroupByDevice;
	@FXML ToggleGroup GB_GroupType;
	
	@FXML
	LineChart<String, Number> GB_ReportDetailChart;
	@FXML
	CategoryAxis GB_ReportDetailBarChartX;
	@FXML
	NumberAxis GB_ReportDetailBarChartY;

	@FXML
	StackPane GB_FreshPane4;
	@FXML
	ProgressIndicator GB_RefreshBar4;
	
	@Autowired
	private WorkPageSession workPageSession;
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private ManagerSession managerSession;
	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private TestDataRepository testDataRepository;

	@PostConstruct
	public void UI_Init(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/ReportOverViewPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/ReportOverViewPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        workPageSession.getWorkPane().addListener( (o, oldValue, newValue)->{
        	if(rootpane.equals(newValue)){
				
				UpTodayReportData();
				
				UpTodayItemData();
				
				UpTodayDeviceData();
				
				UpHistoryReportData();
			}
			else if(oldValue.equals(rootpane)){
				GB_ReportPieChartData.clear();
				GB_ItemPieChartData.clear();
				GB_DevicePieChartData.clear();
			}
        });

        
        //今日报告审核饼图
        GB_ReportPieChartData = FXCollections.observableArrayList();
        GB_ReportPieChart.setData(GB_ReportPieChartData);
        
        //今日测试项目情况饼图
        GB_ItemPieChartData = FXCollections.observableArrayList();
        GB_ItemPieChart.setData(GB_ItemPieChartData);
        
        //今日设备使用情况饼图
        GB_DevicePieChartData = FXCollections.observableArrayList();
        GB_DevicePieChart.setData(GB_DevicePieChartData);

        GB_MonthComboBox.getItems().addAll(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12});
        GB_MonthComboBox.getSelectionModel().select(-1);
        
        GB_GroupByItem.setUserData("项目分组");
        GB_GroupByDevice.setUserData("设备分组");
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public void ShowReportOverViewPage(){
		workPageSession.setWorkPane(rootpane);
	}
	
	//更新今日审核图
	private void UpTodayReportData() {
		List<Device> devices = deviceRepository.findByManagerAccount(managerSession.getAccount());
		List<Object[]> objects = testDataRepository.queryTodayReportGroupByResult( devices);
		
		for (Object[] objects2 : objects) {

			Data temp = new Data((String)objects2[0], ((Long)objects2[1]).intValue());
			GB_ReportPieChartData.add(temp);
			
			HBox tempbox = new HBox();
			tempbox.setAlignment(Pos.CENTER);

			Label label2 = new Label((String)objects2[0]);
			label2.getStyleClass().add("textstyle1");
			
			Label label5 = new Label(" : ");
			label5.setFont(new Font("System", 16));
			
			Label label4 = new Label((int)(temp.getPieValue())+"");
			label4.getStyleClass().add("textstyle2");
			
			Label label1 = new Label(" 例 ");
			label1.setFont(new Font("System", 16));
			
			tempbox.getChildren().addAll(label2, label5, label4, label1);
			tempbox.setSpacing(5);
			
			Tooltip tooltip = new Tooltip();
			tooltip.setGraphic(tempbox);
	        Tooltip.install(temp.getNode(), tooltip);
		}
	}
	
	//今日测试项目情况饼图
	private void UpTodayItemData() {
		List<Device> devices = deviceRepository.findByManagerAccount(managerSession.getAccount());
		List<Object[]> objects = testDataRepository.queryTodayReportGroupByItem( devices);
		
		for (Object[] objects2 : objects) {

			Data temp = new Data((String)objects2[0], ((Long)objects2[1]).intValue());
			GB_ItemPieChartData.add(temp);
			
			HBox tempbox = new HBox();
			tempbox.setAlignment(Pos.CENTER);

			Label label2 = new Label((String)objects2[0]);
			label2.getStyleClass().add("textstyle1");
			
			Label label5 = new Label(" : ");
			label5.setFont(new Font("System", 16));
			
			Label label4 = new Label((int)(temp.getPieValue())+"");
			label4.getStyleClass().add("textstyle2");
			
			Label label1 = new Label(" 例 ");
			label1.setFont(new Font("System", 16));
			
			tempbox.getChildren().addAll(label2, label5, label4, label1);
			tempbox.setSpacing(5);
			
			Tooltip tooltip = new Tooltip();
			tooltip.setGraphic(tempbox);
	        Tooltip.install(temp.getNode(), tooltip);
		}
	}
		
	//今日设备使用情况饼图
	private void UpTodayDeviceData() {
		List<Device> devices = deviceRepository.findByManagerAccount(managerSession.getAccount());
		List<Object[]> objects = testDataRepository.queryTodayReportGroupByDevice( devices);
		
		for (Object[] objects2 : objects) {

			Data temp = new Data((String)objects2[0], ((Long)objects2[1]).intValue());
			GB_DevicePieChartData.add(temp);
			
			HBox tempbox = new HBox();
			tempbox.setAlignment(Pos.CENTER);

			Label label2 = new Label((String)objects2[0]);
			label2.getStyleClass().add("textstyle1");
			
			Label label5 = new Label(" : ");
			label5.setFont(new Font("System", 16));
			
			Label label4 = new Label((int)(temp.getPieValue())+"");
			label4.getStyleClass().add("textstyle2");
			
			Label label1 = new Label(" 例 ");
			label1.setFont(new Font("System", 16));
			
			tempbox.getChildren().addAll(label2, label5, label4, label1);
			tempbox.setSpacing(5);
			
			Tooltip tooltip = new Tooltip();
			tooltip.setGraphic(tempbox);
	        Tooltip.install(temp.getNode(), tooltip);
		}
	}
		
	//历史数据
	private void UpHistoryReportData() {
		Integer year, month;
		String groupType = null;
		List<Object[]> dataList = null;

		String string = GB_YearTextField.getText();
		
		try {
			year = Integer.valueOf(string);
		} catch (Exception e) {
			// TODO: handle exception
			year = null;
		}
		
		month = GB_MonthComboBox.getSelectionModel().getSelectedItem();
		
		if(GB_GroupType.getSelectedToggle() == null)
			GB_GroupType.selectToggle(GB_GroupByItem);
		
		groupType = GB_GroupType.getSelectedToggle().getUserData().toString();
		
		List<Device> devices = deviceRepository.findByManagerAccount(managerSession.getAccount());
		
		if(groupType.equals("项目分组")){
			if(year == null)
				dataList = testDataRepository.queryReportSummyByItem(devices);
			else if (month == null) {
				dataList = testDataRepository.queryReportSummyByYearItem(devices);
			}
			else {
				dataList = testDataRepository.queryReportSummyByYearMonthItem(devices);
			}
		}
		else {
			
		}
		dataList = testDataRepository.queryReportSummyByYearItem(devices);
		
		for (Object[] objects : dataList) {
			System.out.println(objects[0]+"--"+ objects[1]);
		}
	}

	@FXML
	public void QueryReportSummyDataAction(){
		UpHistoryReportData();
	}
}

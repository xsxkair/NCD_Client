package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
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
	@FXML
	FlowPane GB_ItemFlowPane;
	@FXML
	FlowPane GB_DeviceFlowPane;
	
	@FXML
	BarChart<String, Number> GB_ReportDetailBarChart;
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
        
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootpane.equals(newValue)){
					
					UpDetailFilterUI();
					
					UpTodayReportData();
					
					UpTodayItemData();
					
					UpTodayDeviceData();
					
					UpHistoryReportData();
				}
			}
		});
        
        //���ձ�����˱�ͼ
        GB_ReportPieChartData = FXCollections.observableArrayList();
        GB_ReportPieChart.setData(GB_ReportPieChartData);
        
        //���ղ�����Ŀ�����ͼ
        GB_ItemPieChartData = FXCollections.observableArrayList();
        GB_ItemPieChart.setData(GB_ItemPieChartData);
        
        //�����豸ʹ�������ͼ
        GB_DevicePieChartData = FXCollections.observableArrayList();
        GB_DevicePieChart.setData(GB_DevicePieChartData);

        
        GB_MonthComboBox.getItems().addAll(new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12});
        GB_MonthComboBox.getSelectionModel().select(0);
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public void ShowReportOverViewPage(){
		workPageSession.setWorkPane(rootpane);
	}

	//����ɸѡ����
	private void UpDetailFilterUI() {
		
		List<Device> deviceidlist = deviceRepository.findByManagerAccount(managerSession.getAccount());
		
		GB_DeviceFlowPane.getChildren().clear();
		for (Device device : deviceidlist) {
			CheckBox checkBox = new CheckBox(device.getDid());
			checkBox.setUserData(device);
			GB_DeviceFlowPane.getChildren().add(checkBox);
		}
		
		List<String> itemlist = cardRepository.queryItem();
		GB_ItemFlowPane.getChildren().clear();

		for (String string : itemlist) {
			CheckBox checkBox2 = new CheckBox(string);
			checkBox2.setUserData(string);
			GB_ItemFlowPane.getChildren().add(checkBox2);
		}
	}
	
	//���½������ͼ
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
			
			Label label1 = new Label(" �� ");
			label1.setFont(new Font("System", 16));
			
			tempbox.getChildren().addAll(label2, label5, label4, label1);
			tempbox.setSpacing(5);
			
			Tooltip tooltip = new Tooltip();
			tooltip.setGraphic(tempbox);
	        Tooltip.install(temp.getNode(), tooltip);
		}
	}
	
	//���ղ�����Ŀ�����ͼ
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
			
			Label label1 = new Label(" �� ");
			label1.setFont(new Font("System", 16));
			
			tempbox.getChildren().addAll(label2, label5, label4, label1);
			tempbox.setSpacing(5);
			
			Tooltip tooltip = new Tooltip();
			tooltip.setGraphic(tempbox);
	        Tooltip.install(temp.getNode(), tooltip);
		}
	}
		
	//�����豸ʹ�������ͼ
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
			
			Label label1 = new Label(" �� ");
			label1.setFont(new Font("System", 16));
			
			tempbox.getChildren().addAll(label2, label5, label4, label1);
			tempbox.setSpacing(5);
			
			Tooltip tooltip = new Tooltip();
			tooltip.setGraphic(tempbox);
	        Tooltip.install(temp.getNode(), tooltip);
		}
	}
		
	//��ʷ����
	private void UpHistoryReportData() {
		
		List<String> itemlist = new ArrayList<>();
		List<Device> deviceidlist = new ArrayList<>();
		Integer year, month;
		
		for (Node node : GB_ItemFlowPane.getChildren()) {
			CheckBox checkBox = (CheckBox) node;
			
			if(checkBox.isSelected())
				itemlist.add((String) checkBox.getUserData());
		}
		
		for (Node node : GB_DeviceFlowPane.getChildren()) {
			CheckBox checkBox = (CheckBox) node;
			
			if(checkBox.isSelected())
				deviceidlist.add( (Device) checkBox.getUserData());
		}
		
		String string = GB_YearTextField.getText();
		
		try {
			year = Integer.valueOf(string);
		} catch (Exception e) {
			// TODO: handle exception
			year = null;
		}
		
		month = GB_MonthComboBox.getSelectionModel().getSelectedItem();
		
		List<Object[]> dataList = testDataRepository.QueryReportSummyChartData(year, month, itemlist, deviceidlist);
		
		//for (Object[] objects : dataList) {
		//	for
		//}
	}
	
	@FXML
	public void QueryReportSummyDataAction(){
		UpHistoryReportData();
	}
}

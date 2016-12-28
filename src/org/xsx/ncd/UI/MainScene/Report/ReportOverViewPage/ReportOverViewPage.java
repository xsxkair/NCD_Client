package org.xsx.ncd.UI.MainScene.Report.ReportOverViewPage;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.com.xsx.Dao.ManagerDao;
import org.com.xsx.Dao.ReportDao;
import org.com.xsx.Data.SignedManager;
import org.com.xsx.Data.UIMainPage;

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
import javafx.scene.chart.XYChart;
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

public class ReportOverViewPage {
	
	private static ReportOverViewPage S_ReportOverViewPage = null;
	
	private AnchorPane rootpane;
	
	@FXML
	PieChart GB_ReportPieChart;
	private ObservableList<PieChart.Data> GB_ReportPieChartData;
	private QueryReportInfoService S_QueryByResultService;
	@FXML
	StackPane GB_FreshPane1;
	@FXML
	ProgressIndicator GB_RefreshBar1;
	
	@FXML
	PieChart GB_ItemPieChart;
	private ObservableList<PieChart.Data> GB_ItemPieChartData;
	private QueryReportInfoService S_QueryByItemService;
	@FXML
	StackPane GB_FreshPane2;
	@FXML
	ProgressIndicator GB_RefreshBar2;
	
	@FXML
	PieChart GB_DevicePieChart;
	private ObservableList<PieChart.Data> GB_DevicePieChartData;
	private QueryReportInfoService S_QueryByDeviceService;
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
	private QueryReportInfoService S_QueryDetailService;
	@FXML
	StackPane GB_FreshPane4;
	@FXML
	ProgressIndicator GB_RefreshBar4;
	
	private Integer S_FilterYear = null;
	private Integer S_FilterMonth = null;
	private List<String> S_FilterItemList = new ArrayList<>();
	private List<String> S_FilterDeviceList = new ArrayList<>();
	
	private ReportOverViewPage(){
		
	}
	
	public static ReportOverViewPage GetInstance() {
		if(S_ReportOverViewPage == null)
			S_ReportOverViewPage = new ReportOverViewPage();
		
		return S_ReportOverViewPage;
	}
	
	public void UI_Init(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("ReportOverViewPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("ReportOverViewPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        UIMainPage.GetInstance().getGB_Page().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootpane.equals(newValue)){
					
					//清除条件
					S_FilterYear = null;
					S_FilterMonth = null;
					S_FilterItemList.clear();
					S_FilterDeviceList.clear();
					
					UpDetailFilterUI();
					
					S_QueryByResultService.restart();
					S_QueryByItemService.restart();
					S_QueryByDeviceService.restart();
					
					S_QueryDetailService.setParm(new Object[]{S_FilterYear, S_FilterMonth, S_FilterItemList, S_FilterDeviceList});
					S_QueryDetailService.restart();
				}
			}
		});
        
        //今日报告审核饼图
        GB_ReportPieChartData = FXCollections.observableArrayList();
        GB_ReportPieChart.setData(GB_ReportPieChartData);
        S_QueryByResultService = new QueryReportInfoService("QueryResultCount");
        GB_FreshPane1.visibleProperty().bind(S_QueryByResultService.runningProperty());
        GB_RefreshBar1.progressProperty().bind(S_QueryByResultService.progressProperty());
        S_QueryByResultService.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				if(newValue == null){
					GB_ReportPieChartData.clear();
				}
				else{
					Map<String, Integer> data = (Map<String, Integer>) newValue;

					Set<String> keyset = data.keySet();
					for (String string : keyset) {
						Data temp = new Data(string, data.get(string));
						GB_ReportPieChartData.add(temp);
						
						HBox tempbox = new HBox();
						tempbox.setAlignment(Pos.CENTER);
	
						Label label2 = new Label(string);
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
			}
		});
        
        //今日测试项目情况饼图
        GB_ItemPieChartData = FXCollections.observableArrayList();
        GB_ItemPieChart.setData(GB_ItemPieChartData);
        S_QueryByItemService = new QueryReportInfoService("QueryItemCount");
        GB_FreshPane2.visibleProperty().bind(S_QueryByItemService.runningProperty());
        GB_RefreshBar2.progressProperty().bind(S_QueryByItemService.progressProperty());
        S_QueryByItemService.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				if(newValue == null){
					GB_ItemPieChartData.clear();
				}
				else{
					Map<String, Integer> data = (Map<String, Integer>) newValue;

					Set<String> keyset = data.keySet();
					for (String string : keyset) {
						Data temp = new Data(string, data.get(string));
						GB_ItemPieChartData.add(temp);
						
						HBox tempbox = new HBox();
						tempbox.setAlignment(Pos.CENTER);
	
						Label label2 = new Label(string);
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
			}
		});
        
        //今日设备使用情况饼图
        GB_DevicePieChartData = FXCollections.observableArrayList();
        GB_DevicePieChart.setData(GB_DevicePieChartData);
        S_QueryByDeviceService = new QueryReportInfoService("QueryDeviceCount");
        GB_FreshPane3.visibleProperty().bind(S_QueryByDeviceService.runningProperty());
        GB_RefreshBar3.progressProperty().bind(S_QueryByDeviceService.progressProperty());
        S_QueryByDeviceService.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				if(newValue == null){
					GB_DevicePieChartData.clear();
				}
				else{
					Map<String, Integer> data = (Map<String, Integer>) newValue;

					Set<String> keyset = data.keySet();
					for (String string : keyset) {
						Data temp = new Data(string, data.get(string));
						GB_DevicePieChartData.add(temp);
						
						HBox tempbox = new HBox();
						tempbox.setAlignment(Pos.CENTER);
						Label label1 = new Label("设备");
						label1.setFont(new Font("System", 16));
						
						Label label2 = new Label(string);
						label2.getStyleClass().add("textstyle1");
						
						Label label3 = new Label("今天已进行");
						label3.setFont(new Font("System", 16));
						
						Label label4 = new Label((int)(temp.getPieValue())+"");
						label4.getStyleClass().add("textstyle2");
						
						Label label5 = new Label("次测试");
						label5.setFont(new Font("System", 16));
						tempbox.getChildren().addAll(label1,label2, label3, label4, label5);
						tempbox.setSpacing(5);
						
						Tooltip tooltip = new Tooltip();
						tooltip.setGraphic(tempbox);
				        Tooltip.install(temp.getNode(), tooltip);
					}
				}
			}
		});
        
        GB_MonthComboBox.getItems().addAll(new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12});
        GB_MonthComboBox.getSelectionModel().select(0);
        
        S_QueryDetailService = new QueryReportInfoService("QueryDetailCount");
        GB_FreshPane4.visibleProperty().bind(S_QueryDetailService.runningProperty());
        GB_RefreshBar4.progressProperty().bind(S_QueryDetailService.progressProperty());
        S_QueryDetailService.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				if(newValue == null){
					GB_ReportDetailBarChart.getData().clear();
				}
				else{
					List<Object[]> result = (List<Object[]>) newValue;

					//创建柱状图的数据线
					XYChart.Series<String,Number>[] series = new XYChart.Series[S_FilterItemList.size()];
					for (int i=0; i<S_FilterItemList.size(); i++) {
						
						series[i] = new XYChart.Series<String,Number>();

				        series[i].setName(S_FilterItemList.get(i));
				        
				        GB_ReportDetailBarChart.getData().add(series[i]);
					}
					
					Map<String, Map<String, Integer>> chartdatamap = new HashMap<>();
					List<String> datelist = new ArrayList<>();
					
					Map<String, Integer> map = null;
					for (Object[] objects : result) {

						if(chartdatamap.get(objects[0]) == null){
							map = new HashMap<>();
						}
						map.put((String)objects[1], ((BigInteger)objects[2]).intValue());
						chartdatamap.put((String)objects[0], map);
						
						if(datelist.contains((String)objects[0]) != true)
							datelist.add((String)objects[0]);
					}
					
					for (String string : datelist) {

						Map<String, Integer> map2 = chartdatamap.get(string);
						
						for (XYChart.Series<String,Number> serie : series) {
							Integer num = map2.get(serie.getName());
							if(num != null){
								
								XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(string, num);
								serie.getData().add(data);

								Label label2 = new Label(data.getYValue().intValue()+" 例");
								label2.getStyleClass().add("textstyle2");
								
								Tooltip tooltip = new Tooltip();
								tooltip.setGraphic(label2);
						        Tooltip.install(data.getNode(), tooltip);
							}
								
						}
					}
				}
			}
		});

        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public AnchorPane GetPane() {
		return rootpane;
	}

	//更新总图
	private void UpDetailFilterUI() {
		
		List<String> deviceidlist = ManagerDao.QueryDeviceList(SignedManager.GetInstance().getGB_SignedAccount());
		
		GB_DeviceFlowPane.getChildren().clear();
		S_FilterDeviceList.clear();
		for (String string : deviceidlist) {
			CheckBox checkBox = new CheckBox(string);
			GB_DeviceFlowPane.getChildren().add(checkBox);
			S_FilterDeviceList.add(string);
		}
		
		List<String> itemlist = ReportDao.QueryAllItem(deviceidlist);
		GB_ItemFlowPane.getChildren().clear();
		S_FilterItemList.clear();
		for (String string : itemlist) {
			CheckBox checkBox2 = new CheckBox(string);
			GB_ItemFlowPane.getChildren().add(checkBox2);
			S_FilterItemList.add(string);
		}

	}
	
	@FXML
	public void GB_QueryReportSummyAction(){
		
		//读取年
		String text = GB_YearTextField.getText();
		
		try {
			S_FilterYear = Integer.valueOf(text);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(S_FilterYear == null)
			GB_YearTextField.setText(null);
		else
			GB_YearTextField.setText(S_FilterYear.toString());
		
		//读取月
		int month = GB_MonthComboBox.getSelectionModel().getSelectedItem();
				
		if(month == 0)
			S_FilterMonth = null;
		else
			S_FilterMonth = new Integer(month);
		
		//读项目
		S_FilterItemList.clear();
		for (Node node : GB_ItemFlowPane.getChildren()) {
			CheckBox checkBox = (CheckBox) node;
			if(checkBox.isSelected())
				S_FilterItemList.add(checkBox.getText());
		}
		if(S_FilterItemList.size() == 0){
			for (Node node : GB_ItemFlowPane.getChildren()) {
				CheckBox checkBox = (CheckBox) node;
				S_FilterItemList.add(checkBox.getText());
			}
		}
			
		
		//读设备
		S_FilterDeviceList.clear();
		for (Node node : GB_DeviceFlowPane.getChildren()) {
			CheckBox checkBox = (CheckBox) node;
			if(checkBox.isSelected())
				S_FilterDeviceList.add(checkBox.getText());
		}
		if(S_FilterDeviceList.size() == 0){
			for (Node node : GB_DeviceFlowPane.getChildren()) {
				CheckBox checkBox = (CheckBox) node;
				S_FilterDeviceList.add(checkBox.getText());
			}
		}
		
		GB_ReportDetailBarChart.getData().clear();
		
		if((S_FilterItemList.size() == 0) || (S_FilterDeviceList.size() == 0) )
			return;
		else{
			S_QueryDetailService.setParm(new Object[]{S_FilterYear, S_FilterMonth, S_FilterItemList, S_FilterDeviceList});
			S_QueryDetailService.restart();
		}
			
	}
}

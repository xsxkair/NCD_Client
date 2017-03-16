package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXToggleNode;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.TestDataRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

@Component
public class ReportOverViewPage {
	
	private AnchorPane rootpane;
	
	ContextMenu myContextMenu;
	MenuItem myMenuItem1;
	
	private List<String> deviceIds = null;
	private User admin = null;
	
	@FXML PieChart GB_ReportPieChart;
	private ObservableList<PieChart.Data> GB_ReportPieChartData = null;
	@FXML VBox GB_FreshPane1;
	private QueryTodayReportNumByStatusService s_QueryTodayReportNumByStatusService = null;
	
	@FXML PieChart GB_ItemPieChart;
	private ObservableList<PieChart.Data> GB_ItemPieChartData = null;
	@FXML VBox GB_FreshPane2;
	private QueryTodayReportNumByItemService s_QueryTodayReportNumByItemService = null;
	
	@FXML PieChart GB_DevicePieChart;
	private ObservableList<PieChart.Data> GB_DevicePieChartData = null;
	@FXML VBox GB_FreshPane3;
	private QueryTodayReportNumByDeviceService s_QueryTodayReportNumByDeviceService = null;
	
	@FXML HBox GB_DateGroupHbox;
	ToggleGroup GB_ViewTimeToggleGroup;
	@FXML HBox GB_GroupTypeHbox;
	ToggleGroup GB_GroupTypeToggleGroup;
	
	@FXML LineChart<String, Number> GB_ReportDetailChart;
	@FXML CategoryAxis GB_ReportDetailBarChartX;
	@FXML NumberAxis GB_ReportDetailBarChartY;
	@FXML VBox GB_FreshPane4;
	private QueryReportSummyService s_QueryReportSummyService = null;
	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private DeviceRepository deviceRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private UserSession userSession;
	@Autowired private CardRepository cardRepository;
	@Autowired private TestDataRepository testDataRepository;

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
				
        		refreshDeviceIDs();
        		
				s_QueryReportSummyService.restart();
				
				s_QueryTodayReportNumByStatusService.restart();
				
				s_QueryTodayReportNumByItemService.restart();
				
				s_QueryTodayReportNumByDeviceService.restart();
			}
			else{
				
				deviceIds = null;
				admin = null;
				GB_ReportPieChartData.clear();
				GB_ItemPieChartData.clear();
				GB_DevicePieChartData.clear();
				GB_ReportDetailChart.getData().clear();
				
				s_QueryReportSummyService.cancel();
				
				s_QueryTodayReportNumByStatusService.cancel();
				
				s_QueryTodayReportNumByItemService.cancel();
				
				s_QueryTodayReportNumByDeviceService.cancel();
			}
        });

        
        //今日报告审核饼图
        GB_ReportPieChartData = FXCollections.observableArrayList();
        GB_ReportPieChart.setData(GB_ReportPieChartData);
        s_QueryTodayReportNumByStatusService = new QueryTodayReportNumByStatusService();
        GB_FreshPane1.visibleProperty().bind(s_QueryTodayReportNumByStatusService.runningProperty());
        s_QueryTodayReportNumByStatusService.valueProperty().addListener((o, oldValue, newValue)->{
        	GB_ReportPieChartData.clear();
        	if(newValue != null){
        		for (Object[] objects2 : newValue) {

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
        });
        
        //今日测试项目情况饼图
        GB_ItemPieChartData = FXCollections.observableArrayList();
        GB_ItemPieChart.setData(GB_ItemPieChartData);
        s_QueryTodayReportNumByItemService = new QueryTodayReportNumByItemService();
        GB_FreshPane2.visibleProperty().bind(s_QueryTodayReportNumByItemService.runningProperty());
        s_QueryTodayReportNumByItemService.valueProperty().addListener((o, oldValue, newValue)->{
        	
        	GB_ItemPieChartData.clear();
        	
        	if(newValue != null){
        		for (Object[] objects2 : newValue) {

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
        });
        
        //今日设备使用情况饼图
        GB_DevicePieChartData = FXCollections.observableArrayList();
        GB_DevicePieChart.setData(GB_DevicePieChartData);
        s_QueryTodayReportNumByDeviceService = new QueryTodayReportNumByDeviceService();
        GB_FreshPane3.visibleProperty().bind(s_QueryTodayReportNumByDeviceService.runningProperty());
        s_QueryTodayReportNumByDeviceService.valueProperty().addListener((o, oldValue, newValue)->{
        	
        	GB_DevicePieChartData.clear();
        	
        	if(newValue != null){
        		for (Object[] objects2 : newValue) {

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
        });

        //时间视图选择
        GB_ViewTimeToggleGroup = new ToggleGroup();
        JFXToggleNode node1 = new JFXToggleNode();		
		Label label1 = new Label("年");
		node1.setGraphic(label1);
		node1.setToggleGroup(GB_ViewTimeToggleGroup);
		node1.setUserData("年");
		node1.setSelected(true);
		
		JFXToggleNode node2 = new JFXToggleNode();		
		Label label2 = new Label("月");
		node2.setGraphic(label2);
		node2.setToggleGroup(GB_ViewTimeToggleGroup);
		node2.setUserData("月");
		
		
		JFXToggleNode node3 = new JFXToggleNode();		
		Label label3 = new Label("日");
		node3.setGraphic(label3);
		node3.setToggleGroup(GB_ViewTimeToggleGroup);
		node3.setUserData("日");
		
		GB_DateGroupHbox.getChildren().addAll(node1, node2, node3);

		//分组选择
		GB_GroupTypeToggleGroup = new ToggleGroup();
        JFXToggleNode node4 = new JFXToggleNode();		
		Label label4 = new Label("项目分组");
		node4.setGraphic(label4);
		node4.setToggleGroup(GB_GroupTypeToggleGroup);
		node4.setUserData("项目分组");
		node4.setSelected(true);
		
		JFXToggleNode node5 = new JFXToggleNode();		
		Label label5 = new Label("设备分组");
		node5.setGraphic(label5);
		node5.setToggleGroup(GB_GroupTypeToggleGroup);
		node5.setUserData("设备分组");
		
		GB_GroupTypeHbox.getChildren().addAll(node4, node5);
		
		s_QueryReportSummyService = new QueryReportSummyService();
        GB_FreshPane4.visibleProperty().bind(s_QueryReportSummyService.runningProperty());
        s_QueryReportSummyService.valueProperty().addListener((o, oldValue, newValue)->{
        	
        	GB_ReportDetailChart.getData().clear();
        	
        	if(newValue != null){
        		
        		Map<String, Series<String, Number>> map = new HashMap<>();
        		for (Object[] objects : newValue) {
        			String key = (String) objects[0];
        			Series<String, Number> series = null;
        			
        			series = map.get(key);
        			if(series == null){
        				series = new Series<>();
        				series.setName(key);
        			}
        			
        			series.getData().add(new XYChart.Data<String, Number>(objects[1].toString(), ((Long)objects[2]).intValue()));
        			
        			map.put(key, series);
        			
        			series = null;
        		}
        		
        		Set<String> keySet = map.keySet();
        		for (String string : keySet) {
        			Series<String, Number> series = map.get(string);
        			GB_ReportDetailChart.getData().add(series);
        		}
        	}
        });
        
        myMenuItem1 = new MenuItem("刷新");
        myContextMenu = new ContextMenu(myMenuItem1);
        myMenuItem1.setOnAction(e->{
        	
        	refreshDeviceIDs();
        	
        	s_QueryReportSummyService.restart();
			
			s_QueryTodayReportNumByStatusService.restart();
			
			s_QueryTodayReportNumByItemService.restart();
			
			s_QueryTodayReportNumByDeviceService.restart();
        });
        
        rootpane.setOnMouseClicked(e->{
        	if(e.getButton().equals(MouseButton.SECONDARY))
        		myContextMenu.show(rootpane, e.getScreenX(), e.getScreenY());
        });
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
        
        loader = null;
        in = null;
	}
	
	public void ShowReportOverViewPage(){
		workPageSession.setWorkPane(rootpane);
	}
	
	private void refreshDeviceIDs(){
		
		if(userSession.getFatherAccount() == null)
			admin = userRepository.findByAccount(userSession.getAccount());
		else{
			admin = userRepository.findByAccount(userSession.getFatherAccount());
		}
		
		if(admin == null)
			return;
		
		//查询管理员所管理的所有设备id
		if(admin.getType() < 3)
			deviceIds = deviceRepository.quaryAllDeviceId();
		else
			deviceIds = deviceRepository.queryDidByAccount(admin.getAccount());
		
		admin = null;
	}

	@FXML
	public void QueryReportSummyDataAction(){
		s_QueryReportSummyService.restart();
	}
	
	//根据审核状态查询今日报告数目
	class QueryTodayReportNumByStatusService extends Service<List<Object[]>>{

		@Override
		protected Task<List<Object[]>> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<List<Object[]>>{

			@Override
			protected List<Object[]> call(){
				// TODO Auto-generated method stub
				List<Object[]> datas = null;
				
				if(deviceIds == null)
					return null;
				
				if(deviceIds.size() > 0)
					datas = testDataRepository.queryTodayReportGroupByResult(deviceIds);
				
				return datas;
			}
		}
	}
	//根据测试项目查询今日报告数目
	class QueryTodayReportNumByItemService extends Service<List<Object[]>>{

		@Override
		protected Task<List<Object[]>> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<List<Object[]>>{

			@Override
			protected List<Object[]> call(){
				// TODO Auto-generated method stub
				List<Object[]> datas = null;
				
				if(deviceIds == null)
					return null;
				
				if(deviceIds.size() > 0)
					datas = testDataRepository.queryTodayReportGroupByItem(deviceIds);
				
				return datas;
			}
		}
	}
	
	//根据设备查询今日报告数目
	class QueryTodayReportNumByDeviceService extends Service<List<Object[]>>{

		@Override
		protected Task<List<Object[]>> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<List<Object[]>>{

			@Override
			protected List<Object[]> call(){
				// TODO Auto-generated method stub
				List<Object[]> datas = null;
				
				if(deviceIds == null)
					return null;
				
				if(deviceIds.size() > 0)
					datas = testDataRepository.queryTodayReportGroupByDevice( deviceIds);
				
				return datas;
			}
		}
	}
	
	//查询所有报告数目
	class QueryReportSummyService extends Service<List<Object[]>>{

		@Override
		protected Task<List<Object[]>> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<List<Object[]>>{

			@Override
			protected List<Object[]> call(){
				// TODO Auto-generated method stub
				List<Object[]> datas = null;
				String viewTimeType = null;
				String groupType = null;
				
				viewTimeType = GB_ViewTimeToggleGroup.getSelectedToggle().getUserData().toString();
				
				groupType = GB_GroupTypeToggleGroup.getSelectedToggle().getUserData().toString();
				
				if(deviceIds == null)
					return null;
				
				if(deviceIds.size() > 0)
					datas = testDataRepository.queryReportSummy(deviceIds, groupType, viewTimeType);
				
				return datas;
			}
		}
	}
	
}

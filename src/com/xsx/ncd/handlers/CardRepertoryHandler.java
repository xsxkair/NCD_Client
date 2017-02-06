package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import com.xsx.ncd.repository.CardRecordRepository;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

@Component
public class CardRepertoryHandler {
	
	private AnchorPane rootpane;

	//右键菜单
	ContextMenu myContextMenu;
	MenuItem myMenuItem1;
	
		//总库存
		@FXML PieChart GB_CardRepertoryPieChart;
		private QueryCardSummyRepertoryService s_QueryCardSummyRepertoryService;
		//显示加载等待动画
		@FXML VBox GB_FreshPane1;
		
		//设备库存图
		@FXML FlowPane GB_DeviceListPane;
		private ToggleGroup GB_DeviceListToggeGroup;
		@FXML BarChart<String, Number> GB_CardDeviceChart;
		private QueryCardDetailRepertoryService s_QueryCardDetailRepertoryService;
		//显示加载等待动画
		@FXML VBox GB_FreshPane2;
	
	@Autowired
	private WorkPageSession workPageSession;
	@Autowired
	private CardRecordRepository cardRecordRepository;
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private UserSession managerSession;
	@Autowired
	private UserRepository managerRepository;
	
	final static String[] colors = {
		"#41a9c9", "#57b757", "#fba71b", "#f3622d", "#888888", "#c84164", "#9a42c8", "#4258c9"
	};

	@PostConstruct
	private void UI_Init() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/CardRepertoryPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/CardRepertoryPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        GB_DeviceListToggeGroup = new ToggleGroup();
        GB_DeviceListToggeGroup.selectedToggleProperty().addListener((o, oldValue, newValue)->{
			s_QueryCardDetailRepertoryService.restart();
        });
        
        rootpane.getStylesheets().add(this.getClass().getResource("/com/xsx/ncd/views/cardrecordpage.css").toExternalForm());
        
        //查询总库存
        s_QueryCardSummyRepertoryService = new QueryCardSummyRepertoryService();
        GB_FreshPane1.visibleProperty().bind(s_QueryCardSummyRepertoryService.runningProperty());
        s_QueryCardSummyRepertoryService.valueProperty().addListener((o, oldValue, newValue)->{
        	
        	GB_CardRepertoryPieChart.getData().clear();
        	
        	if(newValue != null){
        		for (Object[] objects2 : newValue) {
        			Data temp = new Data(objects2[0].toString(), ((Long) objects2[1]).intValue());
        			GB_CardRepertoryPieChart.getData().add(temp);
        			
        			HBox tempbox = new HBox();
        			tempbox.setAlignment(Pos.CENTER);

        			Label label2 = new Label(objects2[0].toString());
        			label2.getStyleClass().add("textstyle1");
        			
        			Label label5 = new Label(" : 库存  ");
        			label5.setFont(new Font("System", 16));
        			
        			Label label4 = new Label((int)(temp.getPieValue())+"");
        			label4.getStyleClass().add("textstyle2");
        			
        			Label label1 = new Label(" 人份 ");
        			label1.setFont(new Font("System", 16));
        			
        			tempbox.getChildren().addAll(label2, label5, label4, label1);
        			tempbox.setSpacing(5);
        			
        			Tooltip tooltip = new Tooltip();
        			tooltip.setGraphic(tempbox);
        	        Tooltip.install(temp.getNode(), tooltip);
        		}
        	}
        });
        
        //设备库存曲线图
        s_QueryCardDetailRepertoryService = new QueryCardDetailRepertoryService();
        GB_FreshPane2.visibleProperty().bind(s_QueryCardDetailRepertoryService.runningProperty());
        s_QueryCardDetailRepertoryService.valueProperty().addListener((o, oldValue, newValue)->{
        	
        	GB_CardDeviceChart.setTitle(null);
        	GB_CardDeviceChart.getData().clear();
        	
        	if(newValue != null){
        		
        		GB_CardDeviceChart.setTitle(GB_DeviceListToggeGroup.getSelectedToggle().getUserData().toString());
        		XYChart.Series<String, Number> series = new XYChart.Series<>();
        		GB_CardDeviceChart.getData().add(series);
        		
        		for (Object[] objects : newValue) {
        			XYChart.Data<String, Number> temp = new XYChart.Data<String, Number>(objects[0].toString(), ((Integer) objects[1]).intValue());
        			series.getData().add(temp);
        	        
        	        temp.getNode().setStyle("-fx-background-color: "+colors[newValue.indexOf(objects)%8]);
        	        StackPane sp = (StackPane) temp.getNode();
                    Label l = new Label(temp.getYValue().toString());
                    l.setStyle("-fx-font-size: 10;");
                    l.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
                    sp.getChildren().add(l);
                    sp.toFront();
                    sp.setCursor(Cursor.TEXT);
				}
        		
        		System.out.println(GB_CardDeviceChart.getXAxis());
        	}
        });
        
        //右键菜单
        myMenuItem1 = new MenuItem("刷新");
        myContextMenu = new ContextMenu(myMenuItem1);
        //刷新
        myMenuItem1.setOnAction(new EventHandler<ActionEvent>() {

        	@Override
        	public void handle(ActionEvent event) {
      				// TODO Auto-generated method stub
        		UpDateDeviceListUI();
				
				s_QueryCardSummyRepertoryService.restart();
				s_QueryCardDetailRepertoryService.restart();
        	}
        });

        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootpane.equals(newValue)){
					
					UpDateDeviceListUI();
					
					s_QueryCardSummyRepertoryService.restart();
					s_QueryCardDetailRepertoryService.restart();
				}
				else{
					GB_CardRepertoryPieChart.getData().clear();
					s_QueryCardSummyRepertoryService.cancel();
					
					GB_CardDeviceChart.getData().clear();
					s_QueryCardDetailRepertoryService.cancel();
				}
			}
		});
        
        rootpane.setOnMouseClicked(e->{
        	if(e.getButton().equals(MouseButton.SECONDARY))
        		myContextMenu.show(rootpane, e.getScreenX(), e.getScreenY());
        });
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public void ShowCardRepertoryPage(){
		workPageSession.setWorkPane(rootpane);
	}
	
	private void UpDateDeviceListUI(){
		List<Device> devices = null;
		
		User admin = null;
		
		if(managerSession.getFatherAccount() == null)
			admin = managerRepository.findByAccount(managerSession.getAccount());
		else
			admin = managerRepository.findByAccount(managerSession.getFatherAccount());
		
		devices = deviceRepository.findByUserid(admin.getId());
		
		GB_DeviceListPane.getChildren().clear();
		for (Device device : devices) {
			JFXToggleNode node = new JFXToggleNode();		
			Label label = new Label(device.getDid());
			node.setGraphic(label);
			node.setToggleGroup(GB_DeviceListToggeGroup);
			node.setUserData(device);
			
			GB_DeviceListPane.getChildren().add(node);
		}
		
		GB_DeviceListToggeGroup.selectToggle((Toggle) GB_DeviceListPane.getChildren().get(0));
	}

	//总库存图
	class QueryCardSummyRepertoryService extends Service<List<Object[]>>{

		@Override
		protected Task<List<Object[]>> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<List<Object[]>>{

			@Override
			protected List<Object[]> call(){
				List<Integer> userids = new ArrayList<>();
				
				if(managerSession.getFatherAccount() == null){
					userids.add(managerSession.getUser().getId());
					userids.addAll(managerRepository.queryChildIdList(managerSession.getAccount()));
				}
				else{
					userids.add(managerRepository.findByAccount(managerSession.getFatherAccount()).getId());
					userids.addAll(managerRepository.queryChildIdList(managerSession.getFatherAccount()));
				}

				//List<Object[]> objects = cardRecordRepository.QueryCardRepertoryNumByItem(userids);
				
				//return objects;
				return null;
			}
		}
	}
	//库存曲线
	class QueryCardDetailRepertoryService extends Service<List<Object[]>>{

		@Override
		protected Task<List<Object[]>> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<List<Object[]>>{

			@Override
			protected List<Object[]> call(){
				// TODO Auto-generated method stub
				/*List<Object[]> objectList = null;
				Map<String, Integer> sumMap = new HashMap<>();
				Map<String, Integer> useMap = new HashMap<>();
				Map<String, Integer> surplusMap = new HashMap<>();
				
				objectList = cardRecordRepository.QueryCardRepertoryGroupByDeviceAndItem((Device) GB_DeviceListToggeGroup.getSelectedToggle().getUserData());
				System.out.println(objectList.size());
				for (Object[] objects : objectList) {
					System.out.println(objects[0]+"-"+objects[1]+"-"+objects[2]);//+"-"+objects[3]+"-"+objects[4]+"-"+objects[5]);
					sumMap.put(objects[1].toString(), ((Long)objects[2]).intValue());
					surplusMap.put(objects[1].toString(), 0);
				}
				
				System.out.println("xsx");
				objectList = cardRecordRepository.QueryCardUseNumGroupByDeviceAndItem((Device) GB_DeviceListToggeGroup.getSelectedToggle().getUserData());
				System.out.println(objectList.size());
				for (Object[] objects : objectList) {
					System.out.println(objects[0]+"-"+objects[1]+"-"+objects[2]);//+"-"+objects[3]+"-"+objects[4]+"-"+objects[5]);
					useMap.put(objects[1].toString(), ((Long)objects[2]).intValue());
					surplusMap.put(objects[1].toString(), 0);
				}
				
				Set<String> keySet = surplusMap.keySet();
				objectList = new ArrayList<>();
				
				for (String string : keySet) {
					Integer sum = sumMap.get(string);
					if(sum == null)
						sum = 0;
					
					Integer use = useMap.get(string);
					if(use == null)
						use = 0;
					
					surplusMap.put(string, sum-use);
					objectList.add(new Object[]{string, sum-use});
				}
				
				return objectList;
				*/
				return null;
			}
		}
	}

}

package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXRadioButton;
import com.xsx.ncd.define.CardRepertoryTableItem;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.repository.CardRecordRepository;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.ManagerRepository;
import com.xsx.ncd.spring.ManagerSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

@Component
public class CardRepertoryHandler {
	
	private AnchorPane rootpane;
	
	//显示图表
	@FXML
	AnchorPane GB_MainPane;
	
		//总库存
		@FXML
		PieChart GB_CardRepertoryPieChart;
		//右键菜单
		ContextMenu myContextMenu;
		MenuItem myMenuItem1 = new MenuItem("刷新");
		
		//显示加载等待动画
		@FXML
		StackPane GB_FreshPane;
		@FXML
		ProgressIndicator GB_RefreshBar;
		
		//设备库存图
		@FXML
		VBox GB_DeviceVBox;

		@FXML
		FlowPane GB_DeviceFlowPane;
		ToggleGroup GB_DeviceToggleGroup = new ToggleGroup();
		
		@FXML
		BarChart<String, Number> GB_CardDeviceChart;
		@FXML
		CategoryAxis GB_CardXAxis;
		@FXML
		NumberAxis GB_CardYAxis;
		//右键菜单
		ContextMenu myContextMenu1;
		MenuItem myMenuItem11 = new MenuItem("刷新");
		
		//显示加载等待动画
		@FXML
		StackPane GB_FreshPane1;
		@FXML
		ProgressIndicator GB_RefreshBar1;
	
	//显示具体出入库记录
	@FXML
	StackPane GB_CardDetailPane;
	@FXML
	TableView<CardRepertoryTableItem> GB_CardTableView;
	@FXML
	TableColumn<CardRepertoryTableItem, Integer> GB_TableColumn0;
	@FXML
	TableColumn<CardRepertoryTableItem, String> GB_TableColumn1;
	@FXML
	TableColumn<CardRepertoryTableItem, String> GB_TableColumn2;
	@FXML
	TableColumn<CardRepertoryTableItem, String> GB_TableColumn3;
	@FXML
	TableColumn<CardRepertoryTableItem, String> GB_TableColumn4;
	@FXML
	TableColumn<CardRepertoryTableItem, String> GB_TableColumn5;
	@FXML
	TableColumn<CardRepertoryTableItem, String> GB_TableColumn6;
	
	@FXML
	Pagination GB_Pagination;
	
	//显示加载等待动画
	@FXML
	StackPane GB_FreshPane2;
	@FXML
	ProgressIndicator GB_RefreshBar2;
	
	//查询总库存
	private ObservableList<PieChart.Data> GB_CardSummyChartData;
	
	@Autowired
	private WorkPageSession workPageSession;
	@Autowired
	private CardRecordRepository cardRecordRepository;
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private ManagerSession managerSession;
	@Autowired
	private ManagerRepository managerRepository;

	@PostConstruct
	private void UI_Init() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/CardRecordPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/CardRecordPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootpane.getStylesheets().add(this.getClass().getResource("/com/xsx/ncd/views/cardrecordpage.css").toExternalForm());
        
        //查询总库存
        GB_CardSummyChartData = FXCollections.observableArrayList();
        GB_CardRepertoryPieChart.setData(GB_CardSummyChartData);
        
        //右键菜单
        myContextMenu = new ContextMenu(myMenuItem1);
        //刷新
        myMenuItem1.setOnAction(new EventHandler<ActionEvent>() {

        	@Override
        	public void handle(ActionEvent event) {
      				// TODO Auto-generated method stub
        		
        	}
        });
        
        //右键菜单
        myContextMenu1 = new ContextMenu(myMenuItem11);
        //刷新
        myMenuItem11.setOnAction(new EventHandler<ActionEvent>() {

        	@Override
        	public void handle(ActionEvent event) {
      				// TODO Auto-generated method stub

        	}
        });
        
        GB_DeviceToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				
				if(newValue != null){

				}
			}
		});
        
        //详细出入库记录
        GB_TableColumn0.setCellValueFactory(new PropertyValueFactory<CardRepertoryTableItem, Integer>("index"));
        
        GB_TableColumn1.setCellValueFactory(new PropertyValueFactory<CardRepertoryTableItem, String>("time"));
        
        GB_TableColumn2.setCellValueFactory(new PropertyValueFactory<CardRepertoryTableItem, String>("item"));
        
        GB_TableColumn3.setCellValueFactory(new PropertyValueFactory<CardRepertoryTableItem, String>("action"));
        
        GB_TableColumn4.setCellValueFactory(new PropertyValueFactory<CardRepertoryTableItem, String>("acter"));
        
        GB_TableColumn5.setCellValueFactory(new PropertyValueFactory<CardRepertoryTableItem, String>("user"));
        
        GB_TableColumn6.setCellValueFactory(new PropertyValueFactory<CardRepertoryTableItem, String>("device"));
        
        GB_Pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub

			}
		});
   
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootpane.equals(newValue)){
					
					GB_CardDetailPane.setVisible(false);
					
					GB_MainPane.setEffect(null);
					GB_MainPane.getStyleClass().remove("backeffict");
					
					UpdateDeviceListUI();
					
					UpSummyRepositoryPieChart();
					
					UpDeviceRepositoryLineChart();
				}
			}
		});
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public void ShowCardRecordPage(){
		workPageSession.setWorkPane(rootpane);
	}
	
	@FXML
	public void GB_CloseDetailPaneAction(){
		GB_CardDetailPane.setVisible(false);
		
		GB_MainPane.setEffect(null);
	}
	
	private void UpdateDeviceListUI() {
		
		List<Device> devices;
		
		Manager manager = managerRepository.findManagerByAccount(managerSession.getAccount());
		if(manager.getFatheraccount() == null)
			devices = deviceRepository.findByManagerAccount(managerSession.getAccount());
		else
			devices = deviceRepository.findByManagerAccount(manager.getFatheraccount());
			
		GB_DeviceFlowPane.getChildren().clear();
		for (Device device : devices) {
			JFXRadioButton rb = new JFXRadioButton(device.getDid());
			rb.setUserData(device);
			rb.setToggleGroup(GB_DeviceToggleGroup);
			GB_DeviceFlowPane.getChildren().add(rb);
		}
		
		if(devices.size() == 0)
			GB_DeviceToggleGroup.selectToggle(null);
		else
			GB_DeviceToggleGroup.selectToggle((Toggle) GB_DeviceFlowPane.getChildren().get(0));
	}
	
	private void UpSummyRepositoryPieChart() {
		
		List<Manager> managers = new ArrayList<>();
		
		Manager manager = managerRepository.findManagerByAccount(managerSession.getAccount());
		if(manager.getFatheraccount() == null){
			managers.add(manager);
			managers.addAll(managerRepository.queryChildAccountList(manager.getAccount()));
		}
		else{
			managers.add(managerRepository.findManagerByAccount(manager.getFatheraccount()));
			managers.addAll(managerRepository.queryChildAccountList(manager.getFatheraccount()));
		}

		List<Object[]> objects = cardRecordRepository.QueryCardRepertoryNumByItem(managers);
		
		GB_CardSummyChartData.clear();
		for (Object[] objects2 : objects) {
			Data temp = new Data(objects2[0].toString(), ((Long) objects2[1]).intValue());
			GB_CardSummyChartData.add(temp);
			
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
	        
	        temp.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					if(event.getButton().equals(MouseButton.SECONDARY)){
						myContextMenu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
					}
					else if (event.getButton().equals(MouseButton.PRIMARY)) {
						GB_CardDetailPane.setVisible(true);
		        		
		        		GB_MainPane.setEffect(new GaussianBlur(10));

					}
				}

			});
		}
	}
	
	private void UpDeviceRepositoryLineChart() {
		
		Device device = (Device) GB_DeviceToggleGroup.getSelectedToggle().getUserData();
		
		if(device == null)
			return;
		
		/*List<Object[]> objects = cardRecordRepository.QueryCardRepertoryByDevice(device);
		
		for (Object[] objects2 : objects) {
			System.out.println(objects2[0].toString()+" -- "+ objects2[1].toString()+ " -- -" + objects2[2]);
		}*/
	}

}

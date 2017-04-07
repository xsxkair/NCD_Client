package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.define.CardTableItem;
import com.xsx.ncd.define.LabDataTableItem;
import com.xsx.ncd.handlers.CardListPage.QueryCardService;
import com.xsx.ncd.handlers.CardListPage.TableColumnModel;
import com.xsx.ncd.handlers.CardListPage.QueryCardService.QueryReportTask;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.LabTestDataRepository;
import com.xsx.ncd.spring.SystemSetData;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.DateTimePicker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

@Component
public class LabDataListHandler implements HandlerTemplet{
	
	private AnchorPane rootPane = null;
	
	@FXML TextField GB_TesterTextfield;
	@FXML TextField GB_DeviceTextfield;
	@FXML HBox GB_StartTimeHBox;
	@FXML HBox GB_EndTimeHBox;
	private DateTimePicker GB_StartTestDatePicker;
	private DateTimePicker GB_EndTestDatePicker;
	
	@FXML TableView<LabDataTableItem> GB_TableView;
	@FXML TableColumn<LabDataTableItem, Integer> TableColumn1;
	@FXML TableColumn<LabDataTableItem, String> TableColumn2;
	@FXML TableColumn<LabDataTableItem, String> TableColumn3;
	@FXML TableColumn<LabDataTableItem, String> TableColumn4;
	@FXML TableColumn<LabDataTableItem, java.sql.Timestamp> TableColumn5;
	@FXML TableColumn<LabDataTableItem, Float> TableColumn6;
	@FXML TableColumn<LabDataTableItem, String> TableColumn7;
	
	@FXML Pagination GB_Pagination;
	
	@FXML VBox GB_FreshPane;
	
	ContextMenu myContextMenu;
	MenuItem myFreshMenuItem;
	MenuItem mySeeMenuItem;
	
	private QueryLabDataService S_QueryLabDataService = null;
	private List<Object[]> datas = null;
	private List<Integer> labDataIds = null;
	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private SystemSetData systemSetData;
	@Autowired private LabTestDataRepository labTestDataRepository;
	@Autowired private LabDataSeeHandler labDataSeeHandler;
	
	@PostConstruct
	@Override
	public void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/LabDataListPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/LabDataListPage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
        GB_TableView.setOnMouseClicked((e)->{
        	if(e.getButton().equals(MouseButton.SECONDARY)){
				myContextMenu.show(rootPane, e.getScreenX(), e.getScreenY());
			}
        });
        
        TableColumn1.setCellValueFactory(new PropertyValueFactory<LabDataTableItem, Integer>("index"));
        TableColumn2.setCellValueFactory(new PropertyValueFactory<LabDataTableItem, String>("t_index"));
        TableColumn3.setCellValueFactory(new PropertyValueFactory<LabDataTableItem, String>("user"));
        TableColumn4.setCellValueFactory(new PropertyValueFactory<LabDataTableItem, String>("device"));
        TableColumn5.setCellValueFactory(new PropertyValueFactory<LabDataTableItem, java.sql.Timestamp>("testdate"));
        TableColumn6.setCellValueFactory(new PropertyValueFactory<LabDataTableItem, Float>("result"));
        TableColumn7.setCellValueFactory(new PropertyValueFactory<LabDataTableItem, String>("t_desc"));

        GB_StartTestDatePicker = new DateTimePicker();
        GB_StartTestDatePicker.setDateTimeValue(LocalDateTime.now().minusDays(1));
        GB_StartTestDatePicker.setPrefWidth(180);
        GB_StartTimeHBox.getChildren().add(GB_StartTestDatePicker);
        
        GB_EndTestDatePicker = new DateTimePicker();
        GB_EndTestDatePicker.setPrefWidth(180);
        GB_EndTimeHBox.getChildren().add(GB_EndTestDatePicker);
        
        S_QueryLabDataService = new QueryLabDataService();
        
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootPane.equals(newValue)){
					labDataIds = new ArrayList<>();
					StartCardService();
				}
				else {
					labDataIds = null;
					GB_TableView.getItems().clear();
				}
			}
		});

        GB_FreshPane.visibleProperty().bind(S_QueryLabDataService.runningProperty());
        
        //过滤测试项目
        GB_TesterTextfield.textProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
		//过滤批号
		GB_DeviceTextfield.textProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
		//过滤提交时间
		GB_StartTestDatePicker.dateTimeValueProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
		//过滤提交人
		GB_EndTestDatePicker.dateTimeValueProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
	
		GB_Pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if(newValue != null){
					S_QueryLabDataService.restart();
				}
			}
		});
		
		S_QueryLabDataService.valueProperty().addListener(new ChangeListener<Object[]>() {

			@Override
			public void changed(ObservableValue<? extends Object[]> arg0, Object[] arg1, Object[] arg2) {
				// TODO Auto-generated method stub
				GB_TableView.getItems().clear();
				if(arg2 != null){
					int i, lSize;
					Object[] object = null;
					
					GB_Pagination.setPageCount(((Long) arg2[0]).intValue());
					
					datas = (List<Object[]>) arg2[1];
					lSize = datas.size();
					
					labDataIds.clear();
					for (i = 0; i < lSize; i++) {
						object = datas.get(i);
						labDataIds.add((Integer)object[0]);
						GB_TableView.getItems().add(new LabDataTableItem(i+1+GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), 
								(String)object[1], (String)object[2], (String)object[3], (java.sql.Timestamp)object[4], (Float)object[5], 
								(String)object[6], (Integer)object[0]));
					}
					datas = null;
				}
			}
			
		});
		myFreshMenuItem = new MenuItem("刷新");
		myFreshMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				StartCardService();
			}
		});

		mySeeMenuItem = new MenuItem("查看");
		mySeeMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				labDataSeeHandler.showLabDataPane(labDataIds);
			}
		});

        myContextMenu = new ContextMenu();
        myContextMenu.getItems().setAll(myFreshMenuItem, mySeeMenuItem);
        
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}
	
	@Override
	public void showPane() {
		workPageSession.setWorkPane(rootPane);
	}
	
	private void StartCardService(){
		if(GB_Pagination.getCurrentPageIndex() != 0)
			GB_Pagination.setCurrentPageIndex(0);
		else
			S_QueryLabDataService.restart();
	}

	class QueryLabDataService extends Service<Object[]>{
		
		@Override
		protected Task<Object[]> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<Object[]>{

			@Override
			protected Object[] call(){
				// TODO Auto-generated method stub
				return ReadCardListFun();
			}
			
			private Object[] ReadCardListFun(){

				java.sql.Timestamp startDate = null;
				java.sql.Timestamp endDate = null;
				try {
					startDate = java.sql.Timestamp.valueOf(GB_StartTestDatePicker.getDateTimeValue());
				} catch (Exception e2) {
					// TODO: handle exception
					startDate = null;
				}
				
				try {
					endDate = java.sql.Timestamp.valueOf(GB_EndTestDatePicker.getDateTimeValue());
				} catch (Exception e2) {
					// TODO: handle exception
					endDate = null;
				}
				
				Object[] data = labTestDataRepository.QueryLabDataList(GB_TesterTextfield.getText(), GB_DeviceTextfield.getText(), 
						startDate, endDate, GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), systemSetData.getPageSize());

				startDate = null;
				endDate = null;
				
				return data;
			}
		}
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

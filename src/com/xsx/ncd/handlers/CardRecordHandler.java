package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.xsx.ncd.define.CardRecordTableItem;
import com.xsx.ncd.define.ReportTableItem;
import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.CardRecord;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.handlers.ReportListHandler.TableColumnModel;
import com.xsx.ncd.repository.CardRecordRepository;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.SystemSetData;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

@Component
public class CardRecordHandler {
	
	private AnchorPane rootpane;
	
	//Èë¿â
	@FXML JFXDatePicker GB_StartDateField;
	@FXML JFXDatePicker GB_EndDateField;
	@FXML JFXButton GB_QueryRecordAction;
	
	@FXML
	TableView<CardRecordTableItem> GB_CardTableView;
	@FXML
	TableColumn<CardRecordTableItem, String> GB_TableColumn1;
	@FXML
	TableColumn<CardRecordTableItem, String> GB_TableColumn2;
	@FXML
	TableColumn<CardRecordTableItem, String> GB_TableColumn3;
	@FXML
	TableColumn<CardRecordTableItem, String> GB_TableColumn4;
	@FXML
	TableColumn<CardRecordTableItem, String> GB_TableColumn5;
	@FXML
	TableColumn<CardRecordTableItem, String> GB_TableColumn6;
	@FXML
	TableColumn<CardRecordTableItem, String> GB_TableColumn7;
	
	@FXML
	Pagination GB_Pagination;
	
	@FXML VBox GB_FreshPane;
	
	private QueryCardRecordService s_QueryCardRecordService;
	
	@Autowired
	private WorkPageSession workPageSession;
	@Autowired
	private CardRecordRepository cardRecordRepository;
	@Autowired
	private UserSession managerSession;
	@Autowired
	private UserRepository managerRepository;
	@Autowired
	private SystemSetData systemSetData;
	
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
 
        GB_TableColumn1.setCellValueFactory(new PropertyValueFactory<CardRecordTableItem, String>("time"));
        GB_TableColumn1.setCellFactory(new TableColumnModel<CardRecordTableItem, String>());
        GB_TableColumn2.setCellValueFactory(new PropertyValueFactory<CardRecordTableItem, String>("piHao"));
        GB_TableColumn2.setCellFactory(new TableColumnModel<CardRecordTableItem, String>());
        GB_TableColumn3.setCellValueFactory(new PropertyValueFactory<CardRecordTableItem, String>("item"));
        GB_TableColumn3.setCellFactory(new TableColumnModel<CardRecordTableItem, String>());
        GB_TableColumn4.setCellValueFactory(new PropertyValueFactory<CardRecordTableItem, String>("inOrOutNum"));
        GB_TableColumn4.setCellFactory(new TableColumnModel<CardRecordTableItem, String>());
        GB_TableColumn5.setCellValueFactory(new PropertyValueFactory<CardRecordTableItem, String>("managerName"));
        GB_TableColumn5.setCellFactory(new TableColumnModel<CardRecordTableItem, String>());
        GB_TableColumn6.setCellValueFactory(new PropertyValueFactory<CardRecordTableItem, String>("userName"));
        GB_TableColumn6.setCellFactory(new TableColumnModel<CardRecordTableItem, String>());
        GB_TableColumn7.setCellValueFactory(new PropertyValueFactory<CardRecordTableItem, String>("deviceid"));
        GB_TableColumn7.setCellFactory(new TableColumnModel<CardRecordTableItem, String>());
        
        
        s_QueryCardRecordService = new QueryCardRecordService();
        GB_FreshPane.visibleProperty().bind(s_QueryCardRecordService.runningProperty());
        s_QueryCardRecordService.valueProperty().addListener((o, oldValue, newValue)->{
        	
        	GB_CardTableView.getItems().clear();
        	
        	if(newValue != null){
				if(newValue[0] != null)
					GB_Pagination.setPageCount(((Long) newValue[0]).intValue());
				else
					GB_Pagination.setPageCount(0);
				
				if(newValue[1] != null){
					List<Object[]> datas = (List<Object[]>) newValue[1];
					List<CardRecordTableItem> cardRecordTableItems = new ArrayList<>();

					for (Object[] object : datas) {
						cardRecordTableItems.add(new CardRecordTableItem((CardRecord)object[0], (Device)object[1], (Card)object[2], (User)object[3]));
					}
					
					GB_CardTableView.getItems().addAll(cardRecordTableItems);
				}
        	}
        });
        
        GB_Pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if(newValue != null){
					s_QueryCardRecordService.restart();
				}
			}
		});
        
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootpane.equals(newValue)){
					s_QueryCardRecordService.restart();
				}
				else{

				}
			}
		});
        
        
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
        
        loader = null;
        in = null;
	}
	
	public void ShowCardRecordPage(){
		workPageSession.setWorkPane(rootpane);
	}
	
	@FXML
	public void GB_QueryRecordAction(){
		if(GB_Pagination.getCurrentPageIndex() != 0)
			GB_Pagination.setCurrentPageIndex(0);
		else
			s_QueryCardRecordService.restart();
	}
	
	class TableColumnModel<T,S> implements Callback<TableColumn<T, S>, TableCell<T, S>> {
		
	    @Override
	    public TableCell<T, S> call(TableColumn<T, S> param) {
	    	TextFieldTableCell<T, S> cell = new TextFieldTableCell<>();

	    	cell.setAlignment(Pos.CENTER);

	        return cell;
	    }
	}

	class QueryCardRecordService extends Service<Object[]>{
		
		@Override
		protected Task<Object[]> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<Object[]>{

			@Override
			protected Object[] call(){
				// TODO Auto-generated method stub
				return ReadDeviceInfoFun();
			}
			
			private Object[] ReadDeviceInfoFun(){
				
				List<String> userids = new ArrayList<>();
				java.sql.Date startTime = null;
				java.sql.Date endTime = null;
				
				if(managerSession.getFatherAccount() == null){
					userids.add(managerSession.getAccount());
					userids.addAll(managerRepository.queryChildAccountList(managerSession.getAccount()));
				}
				else{
					userids.add(managerSession.getFatherAccount());
					userids.addAll(managerRepository.queryChildAccountList(managerSession.getFatherAccount()));
				}
				
				try {
					endTime = java.sql.Date.valueOf(GB_EndDateField.getValue());
				} catch (Exception e) {
					// TODO: handle exception
					endTime = null;
				}
				
				try {
					startTime = java.sql.Date.valueOf(GB_StartDateField.getValue());
				} catch (Exception e) {
					// TODO: handle exception
					startTime = null;
				}
				
				Object[] data = cardRecordRepository.queryCardRecord(userids, startTime, endTime, 
						GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), systemSetData.getPageSize());
				
				return data;	
			}
		}
	}
}

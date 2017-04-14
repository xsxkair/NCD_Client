package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.define.CardTableItem;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.spring.SystemSetData;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

@Component
public class CardListPage implements HandlerTemplet{
	
	private AnchorPane rootPane = null;
	
	@FXML TextField GB_ItemFilterTextfield;
	@FXML TextField GB_PihaoFilterTextfield;
	@FXML DatePicker GB_upTimeFilterDateChoose;
	@FXML TextField GB_MakerFilterTextfield;
	@FXML DatePicker GB_ManageTimeFilterDateChoose;
	@FXML TextField GB_ManagerFilterTextfield;
	@FXML ComboBox<String> GB_StatusFilterCombox;
	
	@FXML TableView<CardTableItem> GB_TableView;
	@FXML TableColumn<CardTableItem, Integer> TableColumn1;
	@FXML TableColumn<CardTableItem, String> TableColumn2;
	@FXML TableColumn<CardTableItem, String> TableColumn3;
	@FXML TableColumn<CardTableItem, java.sql.Timestamp> TableColumn4;
	@FXML TableColumn<CardTableItem, String> TableColumn5;
	@FXML TableColumn<CardTableItem, java.sql.Timestamp> TableColumn6;
	@FXML TableColumn<CardTableItem, String> TableColumn7;
	@FXML TableColumn<CardTableItem, String> TableColumn8;
	
	@FXML Pagination GB_Pagination;
	
	@FXML VBox GB_FreshPane;
	ContextMenu myContextMenu;
	MenuItem myFreshMenuItem;
	MenuItem myEditMenuItem;
	MenuItem myDeleteMenuItem;
	private int selectItem = -1;
	private CardTableItem tempCardTableItem = null;
	
	private QueryCardService S_QueryCardService = null;
	private List<Object[]> datas;
	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private CardRepository cardRepository;
	@Autowired private SystemSetData systemSetData;
	@Autowired private CardInfoHandler cardInfoHandler;
	@Autowired private UserSession userSession;
	@Autowired private QRCodeHandler qrCodeHandler;
	
	@PostConstruct
	@Override
	public void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/CardListPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/CardListPage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
        TableColumn1.setCellValueFactory(new PropertyValueFactory<CardTableItem, Integer>("index"));
        TableColumn1.setCellFactory(new TableColumnModel<CardTableItem, Integer>());
        
        TableColumn2.setCellValueFactory(new PropertyValueFactory<CardTableItem, String>("item"));
        TableColumn2.setCellFactory(new TableColumnModel<CardTableItem, String>());
        
        TableColumn3.setCellValueFactory(new PropertyValueFactory<CardTableItem, String>("pihao"));
        TableColumn3.setCellFactory(new TableColumnModel<CardTableItem, String>());
        
        TableColumn4.setCellValueFactory(new PropertyValueFactory<CardTableItem, java.sql.Timestamp>("update"));
        TableColumn4.setCellFactory(new TableColumnModel<CardTableItem, java.sql.Timestamp>());
        
        TableColumn5.setCellValueFactory(new PropertyValueFactory<CardTableItem, String>("maker"));
        TableColumn5.setCellFactory(new TableColumnModel<CardTableItem, String>());
        
        TableColumn6.setCellValueFactory(new PropertyValueFactory<CardTableItem, java.sql.Timestamp>("managetime"));
        TableColumn6.setCellFactory(new TableColumnModel<CardTableItem, java.sql.Timestamp>());
        
        TableColumn7.setCellValueFactory(new PropertyValueFactory<CardTableItem, String>("manager"));
        TableColumn7.setCellFactory(new TableColumnModel<CardTableItem, String>());
        
        TableColumn8.setCellValueFactory(new PropertyValueFactory<CardTableItem, String>("status"));
        TableColumn8.setCellFactory(new TableColumnModel<CardTableItem, String>());

        GB_StatusFilterCombox.getItems().setAll(null, "未审核", "合格", "不合格");
        
        S_QueryCardService = new QueryCardService();
        
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootPane.equals(newValue)){
					if(userSession.getFatherAccount() == null){
			        	myContextMenu.getItems().setAll(myFreshMenuItem, myDeleteMenuItem);
			        }
			        else {
			        	myContextMenu.getItems().setAll(myFreshMenuItem, myEditMenuItem);
					}
					StartCardService();
				}
				else {
					GB_TableView.getItems().clear();
				}
			}
		});

        GB_FreshPane.visibleProperty().bind(S_QueryCardService.runningProperty());
        
        //过滤测试项目
		GB_ItemFilterTextfield.textProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
		//过滤批号
		GB_PihaoFilterTextfield.textProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
		//过滤提交时间
		GB_upTimeFilterDateChoose.valueProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
		//过滤提交人
		GB_MakerFilterTextfield.textProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
		//过滤审核时间
		GB_ManageTimeFilterDateChoose.valueProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
		//过滤审核人
		GB_ManagerFilterTextfield.textProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});		
		
		//过滤审核状态
		GB_StatusFilterCombox.valueProperty().addListener((o, oldVal, newVal) -> {
			StartCardService();
		});
		
		GB_Pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if(newValue != null){
					S_QueryCardService.restart();
				}
			}
		});
		
		S_QueryCardService.valueProperty().addListener(new ChangeListener<Object[]>() {

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
					
					for (i = 0; i < lSize; i++) {
						object = datas.get(i);
						GB_TableView.getItems().add(new CardTableItem(i+1+GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), 
								(String)object[1], (String)object[2], (java.sql.Timestamp)object[3], (String)object[4], (java.sql.Timestamp)object[5], 
								(String)object[6], (String)object[7], (Integer)object[0]));
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

		myEditMenuItem = new MenuItem("编辑");
		myEditMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				qrCodeHandler.showPane(GB_TableView.getItems().get(selectItem).getDataindex());
			}
		});
		myDeleteMenuItem = new MenuItem("删除");
		myDeleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				tempCardTableItem = GB_TableView.getItems().get(selectItem);
				if("合格".equals(tempCardTableItem.getStatus())){
					
				}
				else{
					cardRepository.delete(tempCardTableItem.getDataindex());
					StartCardService();
				}
			}
		});

        myContextMenu = new ContextMenu();
        
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
			S_QueryCardService.restart();
	}
	
	class TableColumnModel<T,S> implements Callback<TableColumn<T, S>, TableCell<T, S>> {
		
	    @Override
	    public TableCell<T, S> call(TableColumn<T, S> param) {
	    	TextFieldTableCell<T, S> cell = new TextFieldTableCell<>();

	    	cell.setAlignment(Pos.CENTER);
	    	cell.setEditable(false);

	    	cell.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					TableRow<T> row = cell.getTableRow();
					
					if((row != null)&&(row.getIndex() < GB_TableView.getItems().size())){
						if(event.getClickCount() == 2){
							cardInfoHandler.showPane(GB_TableView.getItems().get(row.getIndex()).getDataindex());
						}
						else if(event.getButton().equals(MouseButton.SECONDARY)){
							selectItem = row.getIndex();
							myContextMenu.show(cell, event.getScreenX(), event.getScreenY());
						}
					}
					
					row = null;
				}
			});
	    	
	        return cell;
	    }
	}

	class QueryCardService extends Service<Object[]>{
		
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

				java.sql.Date update = null;
				java.sql.Date mdate = null;
				try {
					update = java.sql.Date.valueOf(GB_upTimeFilterDateChoose.getValue());
				} catch (Exception e2) {
					// TODO: handle exception
					update = null;
				}
				
				try {
					mdate = java.sql.Date.valueOf(GB_ManageTimeFilterDateChoose.getValue());
				} catch (Exception e2) {
					// TODO: handle exception
					mdate = null;
				}
				
				Object[] data = cardRepository.QueryCardList(GB_ItemFilterTextfield.getText(), GB_PihaoFilterTextfield.getText(), 
						update, GB_MakerFilterTextfield.getText(), mdate, GB_ManagerFilterTextfield.getText(), 
						GB_StatusFilterCombox.getValue(), GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), systemSetData.getPageSize());

				update = null;
				mdate = null;
				
				return data;
			}
		}
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

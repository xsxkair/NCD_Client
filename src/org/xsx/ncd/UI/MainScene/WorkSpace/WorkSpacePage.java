package org.xsx.ncd.UI.MainScene.WorkSpace;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.com.xsx.Dao.ManagerDao;
import org.com.xsx.Data.QueryReportFilterData;
import org.com.xsx.Data.SignedManager;
import org.com.xsx.Data.UIMainPage;
import org.com.xsx.Data.UIScence;
import org.com.xsx.Define.ReportTableItem;
import org.com.xsx.Domain.ManagerBean;
import org.com.xsx.Service.QueryReportService;
import org.xsx.ncd.UI.MainScene.Report.ReportDetailPage.ReportDetailPage;
import org.xsx.ncd.UI.MainScene.Report.ReportListPage.ReportTipInfo;
import org.xsx.ncd.UI.MainScene.Report.ReportOverViewPage.ReportOverViewPage;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class WorkSpacePage {

	private static WorkSpacePage S_WorkSpacePage = null;
	
	private AnchorPane rootpane;
	
	@FXML
	TabPane GB_TabPane;

	//今日待审核报告
	@FXML
	TableView<ReportTableItem> GB_TableView;
	@FXML
	TableColumn<ReportTableItem, Integer> TableColumn1;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn2;
	@FXML
	TableColumn<ReportTableItem, java.sql.Timestamp> TableColumn3;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn4;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn5;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn6;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn7;
	
	@FXML
	Pagination GB_Pagination;
	
	@FXML
	StackPane GB_FreshPane;
	@FXML
	ProgressIndicator GB_RefreshBar;
	
	//已审核报告
	@FXML
	TableView<ReportTableItem> GB_TableView1;
	@FXML
	TableColumn<ReportTableItem, Integer> TableColumn11;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn21;
	@FXML
	TableColumn<ReportTableItem, java.sql.Timestamp> TableColumn31;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn41;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn51;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn61;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn71;
	@FXML
	TableColumn<ReportTableItem, String> TableColumn81;
	
	@FXML
	Pagination GB_Pagination1;
	
	@FXML
	StackPane GB_FreshPane1;
	@FXML
	ProgressIndicator GB_RefreshBar1;
	
	ContextMenu myContextMenu;
	MenuItem myMenuItem1 = new MenuItem("刷新");
	MenuItem myMenuItem2 = new MenuItem("查看报告");
	MenuItem myMenuItem3 = new MenuItem("导出PDF");
	MenuItem myMenuItem4 = new MenuItem("打印报告");


	//数据
	
	//今日数据
	private QueryReportService S_QueryTodayReportService;
	private QueryReportFilterData S_QueryTodayReportFilterData;
	
	private WorkSpacePage() {
		
	}
	
	public static WorkSpacePage GetInstance() {
		if(S_WorkSpacePage == null){
			S_WorkSpacePage = new WorkSpacePage();
			
			S_WorkSpacePage.S_QueryTodayReportFilterData = new QueryReportFilterData(null, new java.sql.Date(System.currentTimeMillis()), null, null, null, null, "未审核", true, 0);
			S_WorkSpacePage.S_QueryTodayReportService = new QueryReportService(S_WorkSpacePage.S_QueryTodayReportFilterData);
			
		}
		
		return S_WorkSpacePage;
	}
	
	public void UI_Init() {
		AnchorPane root = null;
			
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("WorkSpacePage.fxml"));
		InputStream in = this.getClass().getResourceAsStream("WorkSpacePage.fxml");
		loader.setController(this);
		try {
			rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//未处理
		TableColumn1.setCellValueFactory(new PropertyValueFactory<ReportTableItem, Integer>("index"));
        TableColumn1.setCellFactory(new TableColumnModel<ReportTableItem, Integer>());
        
        TableColumn2.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("testitem"));
        TableColumn2.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        TableColumn3.setCellValueFactory(new PropertyValueFactory<ReportTableItem, java.sql.Timestamp>("testdate"));
        TableColumn3.setCellFactory(new TableColumnModel<ReportTableItem, java.sql.Timestamp>());
        
        TableColumn4.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("testresult"));
        TableColumn4.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        TableColumn5.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("tester"));
        TableColumn5.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        TableColumn6.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("deviceid"));
        TableColumn6.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        TableColumn7.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("simpleid"));
        TableColumn7.setCellFactory(new TableColumnModel<ReportTableItem, String>());

        GB_FreshPane.visibleProperty().bind(S_QueryTodayReportService.runningProperty());
        GB_RefreshBar.progressProperty().bind(S_QueryTodayReportService.progressProperty());
        
      //已审核
        TableColumn11.setCellValueFactory(new PropertyValueFactory<ReportTableItem, Integer>("index"));
        TableColumn11.setCellFactory(new TableColumnModel<ReportTableItem, Integer>());
        
        TableColumn21.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("testitem"));
        TableColumn21.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        TableColumn31.setCellValueFactory(new PropertyValueFactory<ReportTableItem, java.sql.Timestamp>("testdate"));
        TableColumn31.setCellFactory(new TableColumnModel<ReportTableItem, java.sql.Timestamp>());
        
        TableColumn41.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("testresult"));
        TableColumn41.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        TableColumn51.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("tester"));
        TableColumn51.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        TableColumn61.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("deviceid"));
        TableColumn61.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        TableColumn71.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("simpleid"));
        TableColumn71.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        TableColumn81.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("reportresult"));
        TableColumn81.setCellFactory(new TableColumnModel<ReportTableItem, String>());

        GB_FreshPane1.visibleProperty().bind(S_QueryTodayReportService.runningProperty());
        GB_RefreshBar1.progressProperty().bind(S_QueryTodayReportService.progressProperty());
        
        myContextMenu = new ContextMenu(myMenuItem1, myMenuItem2, myMenuItem3, myMenuItem4);
        
		UIMainPage.GetInstance().getGB_Page().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(newValue == rootpane){
	
					S_QueryTodayReportFilterData.setFilterisnew(true);
					
					if(GB_Pagination.getCurrentPageIndex() != 0)
						GB_Pagination.setCurrentPageIndex(0);
					else
						S_QueryTodayReportService.restart();
				}
			}
		});
		
		GB_TabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				
				if(newValue.intValue() == 0)
					S_QueryTodayReportFilterData.setReportresult("未审核");
				else if(newValue.intValue() == 1)
					S_QueryTodayReportFilterData.setReportresult("已审核");
				
				S_QueryTodayReportFilterData.setFilterisnew(true);
				
				if(GB_Pagination.getCurrentPageIndex() != 0)
					GB_Pagination.setCurrentPageIndex(0);
				else
					S_QueryTodayReportService.restart();
			}
		});

		GB_Pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				S_QueryTodayReportFilterData.setPageindex(newValue.intValue());
				S_QueryTodayReportService.restart();
			}
		});
		
		GB_Pagination1.currentPageIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				S_QueryTodayReportFilterData.setPageindex(newValue.intValue());
				S_QueryTodayReportService.restart();
			}
		});
		
		S_QueryTodayReportService.valueProperty().addListener(new ChangeListener<Object[]>() {

			@Override
			public void changed(ObservableValue<? extends Object[]> observable, Object[] oldValue, Object[] newValue) {
				// TODO Auto-generated method stub
				if(newValue != null){
					
					//更新页数
					BigInteger totalnum = (BigInteger) newValue[1];
					
					if(totalnum != null){

						int pagesize = S_QueryTodayReportFilterData.getPagesize();

						if(S_QueryTodayReportFilterData.getReportresult().equals("未审核")){
							GB_Pagination.setPageCount((int) ((totalnum.longValue()%pagesize == 0)?(totalnum.longValue()/pagesize):(totalnum.longValue()/pagesize+1)));
							
							GB_Pagination.setCurrentPageIndex(0);
						}
						else if(S_QueryTodayReportFilterData.getReportresult().equals("已审核")){
							GB_Pagination1.setPageCount((int) ((totalnum.longValue()%pagesize == 0)?(totalnum.longValue()/pagesize):(totalnum.longValue()/pagesize+1)));
							
							GB_Pagination1.setCurrentPageIndex(0);
						}
						
					}

					//更新数据
					if(S_QueryTodayReportFilterData.getReportresult().equals("未审核")){
						GB_TableView.getItems().clear();
						GB_TableView.getItems().addAll((ObservableList<ReportTableItem>) newValue[0]);
					}
					else if(S_QueryTodayReportFilterData.getReportresult().equals("已审核")){
						GB_TableView1.getItems().clear();
						GB_TableView1.getItems().addAll((ObservableList<ReportTableItem>) newValue[0]);
					}
					
					S_QueryTodayReportFilterData.setFilterisnew(false);
				}
			}
		});
		
		//刷新
		myMenuItem1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				S_QueryTodayReportFilterData.setFilterisnew(true);
				
				if(GB_Pagination.getCurrentPageIndex() != 0)
					GB_Pagination.setCurrentPageIndex(0);
				else
					S_QueryTodayReportService.restart();
			}
		});
		
		//查看报告
		myMenuItem2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				TableCell cell = (TableCell) myContextMenu.getOwnerNode();
				TableRow<ReportTableItem> row = cell.getTableRow();
				TableView<ReportTableItem> tableView = (TableView<ReportTableItem>) cell.getTableView();
				
				ReportDetailPage.GetInstance().setS_ReportData(tableView.getItems().get(row.getIndex()).getTestdata());
				UIMainPage.GetInstance().setGB_Page(ReportDetailPage.GetInstance().getPane());
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
	
	class TableColumnModel<T,S> implements Callback<TableColumn<T, S>, TableCell<T, S>> {
		
	    @Override
	    public TableCell<T, S> call(TableColumn<T, S> param) {
	    	TextFieldTableCell<T, S> cell = new TextFieldTableCell<>();
	    	
	    	Tooltip tooltip = new Tooltip();
	    	
	    	cell.setAlignment(Pos.CENTER);
	    	cell.setEditable(false);
	    	
	    	cell.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					TableRow<T> row = cell.getTableRow();
					TableView<ReportTableItem> tableView = (TableView<ReportTableItem>) cell.getTableView();
					
					if((row != null)&&(row.getIndex() < tableView.getItems().size())){
						
						if(!row.getStyleClass().contains("tablerow"))
							row.getStyleClass().add("tablerow");
						
						tooltip.setGraphic(new ReportTipInfo(tableView.getItems().get(row.getIndex()).getTestdata()));
						Tooltip.install(cell, tooltip);	
					}
					else
						Tooltip.uninstall(cell, tooltip);
				}
			});

	    	cell.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					TableRow<T> row = cell.getTableRow();
					TableView<ReportTableItem> tableView = (TableView<ReportTableItem>) cell.getTableView();
					
					if((row != null)&&(row.getIndex() < tableView.getItems().size())){
						if(event.getClickCount() == 2){
							ReportDetailPage.GetInstance().setS_ReportData(tableView.getItems().get(row.getIndex()).getTestdata());
							UIMainPage.GetInstance().setGB_Page(ReportDetailPage.GetInstance().getPane());
						}
						else if(event.getButton().equals(MouseButton.SECONDARY)){
							myContextMenu.show(cell, event.getScreenX(), event.getScreenY());
						}
					}
				}
			});
	    	
	        return cell;
	    }
	}
}

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

import com.xsx.ncd.define.ReportTableItem;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.ManagerRepository;
import com.xsx.ncd.repository.TestDataRepository;
import com.xsx.ncd.spring.ManagerSession;
import com.xsx.ncd.spring.SystemSetData;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

@Component
public class TodayWorkHandler {

	private AnchorPane rootpane;

	//今日待审核报告
	@FXML TableView<ReportTableItem> GB_TableView;
	@FXML TableColumn<ReportTableItem, Integer> TableColumn1;
	@FXML TableColumn<ReportTableItem, String> TableColumn2;
	@FXML TableColumn<ReportTableItem, java.sql.Timestamp> TableColumn3;
	@FXML TableColumn<ReportTableItem, String> TableColumn4;
	@FXML TableColumn<ReportTableItem, String> TableColumn5;
	@FXML TableColumn<ReportTableItem, String> TableColumn6;
	@FXML TableColumn<ReportTableItem, String> TableColumn7;
	
	@FXML Pagination GB_Pagination;
	
	@FXML StackPane GB_FreshPane;
	
	ContextMenu myContextMenu;
	MenuItem myMenuItem1 = new MenuItem("刷新");
	MenuItem myMenuItem2 = new MenuItem("查看报告");
	MenuItem myMenuItem3 = new MenuItem("导出PDF");
	MenuItem myMenuItem4 = new MenuItem("打印报告");
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ManagerSession managerSession;
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	@Autowired
	private TestDataRepository testDataRepository;
	
	@Autowired
	private SystemSetData systemSetData;
	@Autowired
	private WorkPageSession workPageSession;
	@Autowired
	private ReportDetailHandler reportDetailHandler;
	
	private QueryReportService queryReportService;

	@PostConstruct
	public void UI_Init() {
			
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/TodayWorkPage.fxml"));
		InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/TodayWorkPage.fxml");
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

        myContextMenu = new ContextMenu(myMenuItem1, myMenuItem2, myMenuItem3, myMenuItem4);
        
        queryReportService = new QueryReportService();
        queryReportService.valueProperty().addListener(new ChangeListener<Page<TestData>>() {

			@Override
			public void changed(ObservableValue<? extends Page<TestData>> arg0, Page<TestData> arg1,
					Page<TestData> arg2) {
				// TODO Auto-generated method stub
				GB_Pagination.setPageCount(arg2.getTotalPages());

				List<TestData> datas = arg2.getContent();
				
				List<ReportTableItem> reportTableItems = new ArrayList<>();
				for (TestData testData : datas) {
					reportTableItems.add(new ReportTableItem(datas.indexOf(testData)+1+GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), testData));
				}
				
				GB_TableView.getItems().clear();
				GB_TableView.getItems().addAll(reportTableItems);
			}
		});
        
        GB_FreshPane.visibleProperty().bind(queryReportService.runningProperty());

        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(newValue != null && newValue.equals(rootpane)){
					if(GB_Pagination.getCurrentPageIndex() != 0)
						GB_Pagination.setCurrentPageIndex(0);
					else
						queryReportService.restart();
				}
			}
		});

		GB_Pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if(newValue != null){
					queryReportService.restart();
				}
			}
		});
		
		//刷新
		myMenuItem1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(GB_Pagination.getCurrentPageIndex() != 0)
					GB_Pagination.setCurrentPageIndex(0);
				else
					queryReportService.restart();
			}
		});
		
		//查看报告
		myMenuItem2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

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
							reportDetailHandler.startReportDetailActivity(tableView.getItems().get(row.getIndex()).getTestdata());
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
	
	
	class QueryReportService extends Service<Page<TestData>>{

		@Override
		protected Task<Page<TestData>> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<Page<TestData>>{

			@Override
			protected Page<TestData> call(){
				// TODO Auto-generated method stub
				return QueryTodayNotDoReport();
			}
			
			public Page<TestData> QueryTodayNotDoReport(){
				
				//查询当前审核人
				Manager manager = managerRepository.findManagerByAccount(managerSession.getAccount());
				if(manager == null)
					return null;
				
				//管理员
				Manager admin;
				
				if(manager.getFatheraccount() == null)
					admin = manager;
				else
					admin = managerRepository.findManagerByAccount(manager.getFatheraccount());
				
				if(admin == null)
					return null;
				
				//查询管理员所管理的所有设备id
				List<Device> deviceList = deviceRepository.findByManagerAccount(admin.getAccount());

				//查询数据
				
				//分页条件
				Order order = new Order(Direction.ASC, "uptime");
				Sort sort = new Sort(order);
				PageRequest pageable = new PageRequest(GB_Pagination.getCurrentPageIndex(), systemSetData.getPageSize(), sort);
				
				//通常使用 Specification 的匿名内部类
				Specification<TestData> specification = new Specification<TestData>() {
						/**
						 * @param *root: 代表查询的实体类. 
						 * @param query: 可以从中可到 Root 对象, 即告知 JPA Criteria 查询要查询哪一个实体类. 还可以
						 * 来添加查询条件, 还可以结合 EntityManager 对象得到最终查询的 TypedQuery 对象. 
						 * @param *cb: CriteriaBuilder 对象. 用于创建 Criteria 相关对象的工厂. 当然可以从中获取到 Predicate 对象
						 * @return: *Predicate 类型, 代表一个查询条件. 
						 */
						@Override
						public Predicate toPredicate(Root<TestData> root,
								CriteriaQuery<?> query, CriteriaBuilder cb) {
							Predicate predicate;

							//未处理
							Path<String> path1 = root.get("result");
							predicate = cb.equal(path1, "未审核");

							//设备id
							Path<String> path3 = root.get("device");
							predicate = cb.and(path3.in(deviceList), predicate);
							return predicate;
						}
					};
					
				Page<TestData> page = testDataRepository.findAll(specification, pageable);

				return page;
			}
		}
	}
}

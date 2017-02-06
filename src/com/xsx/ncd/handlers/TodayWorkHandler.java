package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xsx.ncd.define.ReportTableItem;
import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.repository.TestDataRepository;
import com.xsx.ncd.spring.UserSession;
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
import javafx.scene.layout.VBox;
import javafx.util.Callback;

@Component
public class TodayWorkHandler {

	private AnchorPane rootpane;

	//���մ���˱���
	@FXML TableView<ReportTableItem> GB_TableView;
	@FXML TableColumn<ReportTableItem, Integer> TableColumn1;
	@FXML TableColumn<ReportTableItem, String> TableColumn2;
	@FXML TableColumn<ReportTableItem, java.sql.Timestamp> TableColumn3;
	@FXML TableColumn<ReportTableItem, String> TableColumn4;
	@FXML TableColumn<ReportTableItem, String> TableColumn5;
	@FXML TableColumn<ReportTableItem, String> TableColumn6;
	@FXML TableColumn<ReportTableItem, String> TableColumn7;
	
	@FXML Pagination GB_Pagination;
	
	@FXML VBox GB_FreshPane;
	
	ContextMenu myContextMenu;
	MenuItem myMenuItem1 = new MenuItem("ˢ��");
	MenuItem myMenuItem2 = new MenuItem("�鿴����");
	MenuItem myMenuItem3 = new MenuItem("����PDF");
	MenuItem myMenuItem4 = new MenuItem("��ӡ����");
	
	@Autowired
	private UserRepository managerRepository;
	
	@Autowired
	private UserSession managerSession;
	
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
	@Autowired
	private CardRepository cardRepository;
	
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

		//δ����
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
        queryReportService.valueProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
				GB_Pagination.setPageCount(((Long) newValue[0]).intValue());

				List<Object[]> datas = (List<Object[]>) newValue[1];
				List<ReportTableItem> reportTableItems = new ArrayList<>();

				for (Object[] object : datas) {
					reportTableItems.add(new ReportTableItem(datas.indexOf(object)+1+GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), (TestData)object[0], (Device)object[1], (Card)object[2], null));
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
				else{
					if(queryReportService.isRunning())
						queryReportService.cancel();
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
		
		//ˢ��
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
		
		//�鿴����
		myMenuItem2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

			}
		});
			
		rootpane.getStylesheets().add(this.getClass().getResource("/com/xsx/ncd/views/reportpage.css").toExternalForm());
        
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
						
						tooltip.setGraphic(new ReportTipPaneHandler(GB_TableView.getItems().get(row.getIndex()).getTestdata()));
						
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
							//reportDetailHandler.startReportDetailActivity(tableView.getItems().get(row.getIndex()).getTestdata());
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
	
	
	class QueryReportService extends Service<Object[]>{

		@Override
		protected Task<Object[]> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<Object[]>{

			@Override
			protected Object[] call(){
				// TODO Auto-generated method stub
				return QueryTodayNotDoReport();
			}
			
			public Object[] QueryTodayNotDoReport(){
				Object[] results = new Object[5];
				//����Ա
				User admin;
				
				//��ѯ��ǰ�����
				if(managerSession.getFatherAccount() == null)
					admin = managerRepository.findByAccount(managerSession.getAccount());
				else
					admin = managerRepository.findByAccount(managerSession.getFatherAccount());
				
				if(admin == null)
					return null;
				
				//��ѯ����Ա������������豸id
				List<Integer> deviceList = deviceRepository.queryDeviceIdByUserid(admin.getId());
/*
				//��ѯ����
				
				//��ҳ����
				Order order = new Order(Direction.ASC, "uptime");
				Sort sort = new Sort(order);
				PageRequest pageable = new PageRequest(GB_Pagination.getCurrentPageIndex(), systemSetData.getPageSize(), sort);
				
				//ͨ��ʹ�� Specification �������ڲ���
				Specification<TestData> specification = new Specification<TestData>() {

						@Override
						public Predicate toPredicate(Root<TestData> root,
								CriteriaQuery<?> query, CriteriaBuilder cb) {
							Predicate predicate;

							//δ����
							Path<String> path1 = root.get("result");
							predicate = cb.equal(path1, "δ���");

							//�豸id
							Path<Integer> path3 = root.get("deviceid");
							predicate = cb.and(path3.in(deviceList), predicate);
							return predicate;
						}
					};
					
				Page<TestData> page = testDataRepository.findAll(specification, pageable);
				
				results[0] = page.getTotalPages();

				List<TestData> datas = page.getContent();
				
				List<Object[]> dataSet = new ArrayList();
				
				for (TestData testData : datas) {
					Card card = cardRepository.findOne(testData.getCardid());
					Device device = deviceRepository.findOne(testData.getDeviceid());
					
					dataSet.add(new Object[]{testData,card,device});
					
				}
				
				results[1] = dataSet;
				*/
				
				return testDataRepository.QueryTodayReport(deviceList, GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), systemSetData.getPageSize());
			}
		}
	}
}

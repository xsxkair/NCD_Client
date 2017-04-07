
package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.sun.javafx.scene.control.skin.PaginationSkin;
import com.xsx.ncd.define.ReportTableItem;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.repository.TestDataRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.SystemSetData;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

@Component
public class ReportListHandler implements HandlerTemplet{
	
	private AnchorPane reportpane;
	
	@FXML FlowPane GB_FilterFlowPane;
	@FXML TextField GB_TestItemFilterTextfield;
	@FXML DatePicker GB_TestTimeFilterDateChoose;
	@FXML TextField GB_TesterFilterTextfield;
	@FXML ComboBox<String> GB_TestDeviceFilterCombox;
	@FXML TextField GB_TestSampleFilterTextField;
	@FXML ComboBox<String> GB_ReportResultFilterCombox;
	@FXML StackPane refreshToggleNode;
	private SVGGlyph svgGlyph = null;
	
	@FXML TableView<ReportTableItem>	GB_TableView;
	@FXML TableColumn<ReportTableItem, Integer> TableColumn1;
	@FXML TableColumn<ReportTableItem, String> TableColumn2;
	@FXML TableColumn<ReportTableItem, java.sql.Timestamp> TableColumn3;
	@FXML TableColumn<ReportTableItem, String> TableColumn4;
	@FXML TableColumn<ReportTableItem, String> TableColumn5;
	@FXML TableColumn<ReportTableItem, String> TableColumn6;
	@FXML TableColumn<ReportTableItem, String> TableColumn7;
	@FXML TableColumn<ReportTableItem, String> TableColumn8;
	
	@FXML Pagination GB_Pagination;
	
	@FXML VBox GB_FreshPane;
	
	ContextMenu myContextMenu;
	MenuItem myMenuItem1;
	
	//查询线程
	private QueryReportService S_QueryReportService;
	
	private User admin = null;
	private List<Device> deviceList = null;
	private List<String> deviceIdList = null;
	private String deviceId = null;
	
	private List<Object[]> datas = null;
	private List<ReportTableItem> reportTableItems = null;

	private TableRow row = null;
	
	@Autowired private TestDataRepository testDataRepository;
	@Autowired private UserRepository managerRepository;
	@Autowired private DeviceRepository deviceRepository;
	@Autowired private UserSession managerSession;
	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private SystemSetData systemSetData;
	@Autowired private ReportDetailHandler reportDetailHandler;
	
	@PostConstruct
	@Override
	public void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/ReportListPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/ReportListPage.fxml");
        loader.setController(this);
        try {
        	reportpane = loader.load(in);
        	
        	SVGGlyphLoader.clear();

			SVGGlyphLoader.loadGlyphsFont(this.getClass().getResourceAsStream("/RES/icomoon.svg"), "icomoon.svg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        svgGlyph = SVGGlyphLoader.getGlyph("icomoon.svg.refresh");
        svgGlyph.setPrefSize(25, 25);
        svgGlyph.setFill(Color.GREY);
        
        refreshToggleNode.getChildren().setAll(svgGlyph);

        refreshToggleNode.setOnMouseEntered(e->{
        	svgGlyph.setFill(Color.DEEPSKYBLUE);
        });
        refreshToggleNode.setOnMouseExited(e->{
        	svgGlyph.setFill(Color.GREY);
        });
        refreshToggleNode.setOnMouseClicked(e->{
        	refreshPage();
			
			StartReportService();
        });
     
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
        
        TableColumn8.setCellValueFactory(new PropertyValueFactory<ReportTableItem, String>("reportresult"));
        TableColumn8.setCellFactory(new TableColumnModel<ReportTableItem, String>());
        
        GB_ReportResultFilterCombox.getItems().add(null);
        GB_ReportResultFilterCombox.getItems().addAll("未审核", "合格", "不合格");
        
        S_QueryReportService = new QueryReportService();
        
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(reportpane.equals(newValue)){	
					
					refreshPage();
					
					StartReportService();
				}
				else {
					GB_TestDeviceFilterCombox.getItems().clear();
					GB_TableView.getItems().clear();
					
					datas = null;
					reportTableItems = null;
				}
			}
		});

        GB_FreshPane.visibleProperty().bind(S_QueryReportService.runningProperty());
        
        //过滤测试项目
		GB_TestItemFilterTextfield.textProperty().addListener((o, oldVal, newVal) -> {
			StartReportService();
		});
		
		//过滤测试时间
		GB_TestTimeFilterDateChoose.valueProperty().addListener((o, oldVal, newVal) -> {
			StartReportService();
		});

		//过滤测试人
		GB_TesterFilterTextfield.textProperty().addListener((o, oldVal, newVal) -> {
			StartReportService();
		});
		
		//过滤测试设备
		GB_TestDeviceFilterCombox.valueProperty().addListener((o, oldVal, newVal) -> {
			StartReportService();
		});
		
		//过滤样品编号
		GB_TestSampleFilterTextField.textProperty().addListener((o, oldVal, newVal) -> {
			StartReportService();
		});
		
		//过滤报告结果
		GB_ReportResultFilterCombox.valueProperty().addListener((o, oldVal, newVal) -> {
			StartReportService();
		});
		
		GB_Pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if(newValue != null){
					S_QueryReportService.restart();
				}
			}
		});
		
		S_QueryReportService.valueProperty().addListener(new ChangeListener<Object[]>() {

			@Override
			public void changed(ObservableValue<? extends Object[]> arg0, Object[] arg1, Object[] arg2) {
				// TODO Auto-generated method stub
				
				if(arg2 != null){
					int i, lSize;
					Object[] object = null;
					
					GB_Pagination.setPageCount(((Long) arg2[0]).intValue());
					
					datas = (List<Object[]>) arg2[1];
					lSize = datas.size();
					
					reportTableItems = new ArrayList<>(lSize);
					
					for (i = 0; i < lSize; i++) {
						object = datas.get(i);
						reportTableItems.add(new ReportTableItem(i+1+GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), 
								(String)object[5], (java.sql.Timestamp)object[1], (Float)object[2], (String)object[6], (String)object[3], 
								(String)object[7], (String)object[4], (String)object[8], (Integer)object[0]));
					}
					
					GB_TableView.getItems().setAll(reportTableItems);
					
					datas = null;
					reportTableItems = null;
				}
			}
			
		});
		
        reportpane.getStylesheets().add(this.getClass().getResource("/com/xsx/ncd/views/reportpage.css").toExternalForm());
        
        GB_Pagination.setSkin(new coutompanition(GB_Pagination));
        GB_Pagination.setStyle("-fx-page-information-visible: false");
        
        myMenuItem1 = new MenuItem("删除");
        myContextMenu = new ContextMenu();
        myContextMenu.getItems().add(myMenuItem1);
        
        AnchorPane.setTopAnchor(reportpane, 0.0);
        AnchorPane.setBottomAnchor(reportpane, 0.0);
        AnchorPane.setLeftAnchor(reportpane, 0.0);
        AnchorPane.setRightAnchor(reportpane, 0.0);
        
        loader = null;
        in = null;
	}

	@Override
	public void showPane() {
		workPageSession.setWorkPane(reportpane);
	}
	
	private void refreshPage() {
		//管理员						
		if(managerSession.getFatherAccount() != null)
			admin = managerRepository.findByAccount(managerSession.getFatherAccount());
		else
			admin = managerRepository.findByAccount(managerSession.getAccount());
			
		if(admin == null)
			return;
			
		//查询管理员所管理的所有设备id
		if(admin.getType() < 3)
			deviceIdList = deviceRepository.quaryAllDeviceId();
		else
			deviceIdList = deviceRepository.queryDidByAccount(admin.getAccount());
		
		GB_TestDeviceFilterCombox.getItems().clear();
		GB_TestDeviceFilterCombox.getItems().add("ALL");
		GB_TestDeviceFilterCombox.getItems().addAll(deviceIdList);
		GB_TestDeviceFilterCombox.getSelectionModel().select(null);
		
		deviceIdList = null;
		admin = null;
	}
	
	private void StartReportService(){
		
		if(GB_Pagination.getCurrentPageIndex() != 0)
			GB_Pagination.setCurrentPageIndex(0);
		else
			S_QueryReportService.restart();
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
					row = cell.getTableRow();
					
					if((row != null)&&(row.getIndex() < GB_TableView.getItems().size())){
						if(event.getClickCount() == 2){
							reportDetailHandler.startReportDetailActivity(GB_TableView.getItems().get(row.getIndex()).getDataIndex());
						}
						else if(event.getButton().equals(MouseButton.SECONDARY)){
							myContextMenu.show(cell, event.getScreenX(), event.getScreenY());
						}
					}
					
					row = null;
				}
			});
	    	
	        return cell;
	    }
	}
	
	class coutompanition extends PaginationSkin {

	    private HBox controlBox;
	    private Button prev;
	    private Button next;
	    private Button first;
	    private Button last;
	    private TextField pageindexinput;
	    private Label pagecountlabel;

	    private void patchNavigation() {
	        Pagination pagination = getSkinnable();

	        Node control = pagination.lookup(".control-box");
	        if (!(control instanceof HBox))
	            return;
	        controlBox = (HBox) control;
	        
	        prev = (Button) controlBox.getChildren().get(0);
	        next = (Button) controlBox.getChildren().get(controlBox.getChildren().size() - 1);
	        
	        first = new Button("首页");
	        first.setAlignment(Pos.CENTER);
	        first.setOnAction(e -> {
	            pagination.setCurrentPageIndex(0);
	        });
	        first.disableProperty().bind(
	                pagination.currentPageIndexProperty().isEqualTo(0));

	        last = new Button("尾页");
	        last.setAlignment(Pos.CENTER);
	        last.setOnAction(e -> {
	            pagination.setCurrentPageIndex(pagination.getPageCount());
	        });
	        last.disableProperty().bind(
	                pagination.currentPageIndexProperty().isEqualTo(
	                        pagination.getPageCount() - 1));
	        
	        pageindexinput = new TextField();
	        pageindexinput.prefColumnCountProperty().bind(pageindexinput.textProperty().length());
	        pageindexinput.setStyle("-fx-font-size: 15; -fx-text-fill: dodgerblue  ;");
	        pageindexinput.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					// TODO Auto-generated method stub
					if(event.getCode() == KeyCode.ENTER){
						
						if(pageindexinput.getText().length() > 0){

							Integer temp = null;
							
							try {
								temp = Integer.valueOf(pageindexinput.getText());
							} catch (Exception e2) {
								// TODO: handle exception
								temp = null;
							}
							
							if(temp != null){
								if(temp > 0)
									temp -= 1;
								if(temp < pagination.getPageCount())
									pagination.setCurrentPageIndex(temp);

							}
							
							temp = null;
						}
					}
				}
			});
	        
	        StringBinding indexlabel = new StringBinding() {
	        	{
	        		bind(pagination.currentPageIndexProperty());
	        	}
				@Override
				protected String computeValue() {
					// TODO Auto-generated method stub
					int index = pagination.getCurrentPageIndex();
					
					return index+1+"";
				}
			};

			pageindexinput.textProperty().bind(indexlabel);
	        pageindexinput.focusedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					// TODO Auto-generated method stub
					if(newValue){
						pageindexinput.textProperty().unbind();
					}	
					else{
						pageindexinput.textProperty().bind(indexlabel);
					}
				}
			});
	        
	        pagecountlabel = new Label("");
	        pagecountlabel.setStyle("-fx-font-size: 15; -fx-text-fill: dodgerblue  ;");
	        pagecountlabel.textProperty().bind(new StringBinding() {
	        	{
	        		bind(pagination.pageCountProperty());
	        	}
				@Override
				protected String computeValue() {
					// TODO Auto-generated method stub
					return "/ "+pagination.getPageCount();
				}
			});
	        
	        ListChangeListener childrenListener = c -> {
	            while (c.next()) {
	                // implementation detail: when nextButton is added, the setup is complete
	                if (c.wasAdded() && !c.wasRemoved() // real addition
	                        && c.getAddedSize() == 1 // single addition
	                        && c.getAddedSubList().get(0) == next) { 
	                    addCustomNodes();
	                }
	            }
	        };
	        controlBox.getChildren().addListener(childrenListener);
	        
	        addCustomNodes();
	    }

	    protected void addCustomNodes() {
	        // guarding against duplicate child exception 
	        // (some weird internals that I don't fully understand...)
	        if (first.getParent() == controlBox) return;
	        controlBox.getChildren().add(0, first);
	        controlBox.getChildren().add(last);
	        controlBox.getChildren().add(controlBox.getChildren().size()/2+1, pageindexinput);
	        controlBox.setMargin(pageindexinput, new Insets(0, 10, 0, 30));
	        
	        controlBox.getChildren().add(controlBox.getChildren().indexOf(pageindexinput)+1, pagecountlabel);
	        controlBox.setMargin(pagecountlabel, new Insets(0, 30, 0, 0));
	    }

	    /**
	     * @param pagination
	     */
	    public coutompanition(Pagination pagination) {
	        super(pagination);
	        patchNavigation();
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
				return ReadDeviceInfoFun();
			}
			
			private Object[] ReadDeviceInfoFun(){
				
				String selectDeviceId = null;
				
				try {
					deviceIdList = GB_TestDeviceFilterCombox.getItems().subList(1, GB_TestDeviceFilterCombox.getItems().size()-1);
				} catch (Exception e) {
					// TODO: handle exception
					return null;
				}
				
				java.sql.Date tempdate = null;
				try {
					tempdate = java.sql.Date.valueOf(GB_TestTimeFilterDateChoose.getValue());
				} catch (Exception e2) {
					// TODO: handle exception
					tempdate = null;
				}

				selectDeviceId = GB_TestDeviceFilterCombox.getSelectionModel().getSelectedItem();
				
				if(selectDeviceId == null)
					deviceId = null;
				else if(selectDeviceId.equals("ALL"))
					deviceId = null;
				else
					deviceId = GB_TestDeviceFilterCombox.getSelectionModel().getSelectedItem();
				
				Object[] data = testDataRepository.QueryReportList(GB_TestItemFilterTextfield.getText(), tempdate, GB_TesterFilterTextfield.getText(),
						deviceId, deviceIdList, GB_TestSampleFilterTextField.getText(), 
						GB_ReportResultFilterCombox.getValue(), GB_Pagination.getCurrentPageIndex()*systemSetData.getPageSize(), systemSetData.getPageSize());
				
				admin = null;
				deviceIdList = null;
				tempdate = null;
				
				return data;
			}
		}
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

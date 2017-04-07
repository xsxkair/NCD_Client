package com.xsx.ncd.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.controlsfx.dialog.CommandLinksDialog;
import org.controlsfx.dialog.CommandLinksDialog.CommandLinksButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.xsx.ncd.define.DeviceTestTableItem;
import com.xsx.ncd.entity.LabTestData;
import com.xsx.ncd.repository.LabTestDataRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.ClientSocket;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Path;
import javafx.util.Callback;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class DeviceTestHandler implements HandlerTemplet{
	
	private AnchorPane rootPane = null;
	
	@FXML StackPane rootStackPane;
	
	@FXML JFXDialog logDialog;
	@FXML Label dialogInfo;
	@FXML JFXButton acceptButton;
	
	@FXML TextField GB_TestCountFiled;
	@FXML TextField GB_TestRelayFiled;
	@FXML TextField GB_DeviceIPField;
	@FXML Button GB_StartTestButton;
	
	@FXML LineChart<Number, Number> GB_Chart;
	private Pane lineChartContentPane = null;
	private LabTestData tempLabTestData = null;
	private double firstPointX = 0;
	
	@FXML TableView<DeviceTestTableItem> GB_TableView;
	@FXML TableColumn<DeviceTestTableItem, String> GB_TableColumn0;
	@FXML TableColumn<DeviceTestTableItem, Float> GB_TableColumn1;
	
	private List<LabTestData> GB_LabTestDatas = null;
	private List<Integer> tempSeriesList = null;
	private String deviceId = null;
	private Integer testCount = null;
	private Integer testDelayTime = null;
	private Integer testIndex = null;
	private Series<Number, Number> currentSeries = null;
	private Series<Number, Number> tempSeries = null;
	private DeviceTestTableItem tempDeviceTestTableItem = null;
	private Image tImage = null;
	private Image bImage = null;
	private Image cImage = null;
	private int ShowSeriesIndex = 0;
	
	private JSONObject  jsonarray = null;
	private Map typeMap = new HashMap();

	@FXML TextArea GB_LogTextArea;
	
	String LastSaveDirectory = null;
	private final Integer retryMaxNum = 10;
	
	private TestService S_TestService = null;
	
	@Autowired WorkPageSession workPageSession;
	@Autowired private LabTestDataRepository labTestDataRepository;
	@Autowired UserSession userSession;

	@PostConstruct
	@Override
	public void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/DeviceTestPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/DeviceTestPage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		tImage = new Image(this.getClass().getResourceAsStream("/RES/star.png"));
		bImage = new Image(this.getClass().getResourceAsStream("/RES/sun.png"));
		cImage = new Image(this.getClass().getResourceAsStream("/RES/moon.png"));
		
		rootPane.getStylesheets().add(this.getClass().getResource("/com/xsx/ncd/views/DeviceTestPage.css").toExternalForm());
		rootStackPane.getChildren().remove(logDialog);
		
		Callback<TableColumn<DeviceTestTableItem, String>, TableCell<DeviceTestTableItem, String>> cellFactory =
                new Callback<TableColumn<DeviceTestTableItem, String>, TableCell<DeviceTestTableItem, String>>() {
				public TableCell<DeviceTestTableItem, String> call(TableColumn<DeviceTestTableItem, String> p) {
                        return new EditingCell();
                    }
                };
		GB_TableColumn0.setCellValueFactory(new PropertyValueFactory<DeviceTestTableItem, String>("testDesc"));
		GB_TableColumn0.setCellFactory(cellFactory);
		GB_TableColumn0.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DeviceTestTableItem,String>>() {

			@Override
			public void handle(CellEditEvent<DeviceTestTableItem, String> arg0) {
				// TODO Auto-generated method stub
				 ((DeviceTestTableItem) arg0.getTableView().getItems().get(
						 arg0.getTablePosition().getRow())).testDescProperty().set(arg0.getNewValue());
				 
				 GB_LabTestDatas.get(arg0.getTablePosition().getRow()).setDsc(arg0.getNewValue());
				 labTestDataRepository.save(GB_LabTestDatas.get(arg0.getTablePosition().getRow()));
			}
		});

        GB_TableColumn1.setCellValueFactory(new PropertyValueFactory<DeviceTestTableItem, Float>("tcValue"));
        
        S_TestService = new TestService();
        S_TestService.messageProperty().addListener((o, oldValue, newValue)->{
        	GB_LogTextArea.appendText(newValue+"\n");
        });
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue)->{
        	if(rootPane.equals(newValue)){
        		GB_LabTestDatas = new ArrayList<>();
        		
        		tempSeriesList = new ArrayList<>();
        	}
        	else if (rootPane.equals(oldValue)) {
        		
        		S_TestService.cancel();
        		
        		GB_LabTestDatas = null;
        		
        		tempSeriesList = null;
        		
        		GB_Chart.getData().clear();
        		
        		GB_TableView.getItems().clear();
			}
        });
        
        typeMap.put("data", int[].class);
    	typeMap.put("status", int.class);

        GB_StartTestButton.disableProperty().bind(S_TestService.runningProperty());
        GB_TestCountFiled.disableProperty().bind(S_TestService.runningProperty());

        GB_TestRelayFiled.setText("1");
        GB_TestRelayFiled.disableProperty().bind(S_TestService.runningProperty());
        GB_DeviceIPField.disableProperty().bind(S_TestService.runningProperty()); 
        
        acceptButton.setOnMouseClicked((e)->{
        	logDialog.close();
		});
        
        GB_Chart.setOnMouseClicked((e)->{
        	int i = 0;

        	MouseButton button = e.getButton();
    		if(button.equals(MouseButton.SECONDARY)){
    			ShowSeriesIndex = 0;
    			for (i=0; i<GB_Chart.getData().size();i++) {
    				 ShowSeries(GB_Chart.getData().get(i));
    			}
    		}
    		else if(button.equals(MouseButton.MIDDLE)){
    			ShowSeriesIndex++;
    			if(ShowSeriesIndex >= GB_Chart.getData().size())
    				ShowSeriesIndex = 0;
    			for(i=0; i< GB_Chart.getData().size(); i++){
    				if(i != ShowSeriesIndex)
    					HideSeries(GB_Chart.getData().get(i));
    				else
    					ShowSeries(GB_Chart.getData().get(i));
    			}
    		}
        });
        GB_Chart.setOnMouseMoved((e)->{
        	int seriesSize = GB_Chart.getData().size();
        	int dataSize = 0;
        	double min = 65535;
        	int minPoint = 0;
        	double tempjuli = 0;
        	StackPane tempNode = null;
        	
        	firstPointX = GB_Chart.getYAxis().getWidth() +GB_Chart.getYAxis().getLayoutX() + GB_Chart.getChildrenUnmodifiable().get(1).getLayoutX() ;
        	
        	for (int i=0; i<seriesSize; i++) {
				tempSeries = GB_Chart.getData().get(i);
				dataSize = tempSeries.getData().size();
				min = 65535;
				minPoint = -1;
				for (int j=0; j<dataSize; j++ ) {
					tempNode = (StackPane) tempSeries.getData().get(j).getNode();
					 tempjuli = tempNode.getLayoutX()+firstPointX;

					 if(tempjuli >= e.getX())
						 tempjuli = tempjuli - e.getX();
					 else
						 tempjuli = e.getX() - tempjuli;
					 
					 if(min > tempjuli){
						 min = tempjuli;
						 minPoint = j;
					 }
					 
					 if(tempNode.getStyleClass().contains("BigDataPoint"))
						 tempNode.getStyleClass().remove("BigDataPoint");
				}
				
				if((minPoint >= 0) && (!tempNode.getStyleClass().contains("SelectDataPoint"))){
					tempNode = (StackPane) tempSeries.getData().get(minPoint).getNode();
					tempNode.getStyleClass().add("BigDataPoint");
				}
			}
        });
        
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}
	
	@Override
	public void showPane(){
		workPageSession.setWorkPane(rootPane);
	}
	
	private void showLogsDialog(String logs) {
		dialogInfo.setText(logs);
		logDialog.show(rootStackPane);
	}
	
	public boolean readDeviceInfo(String iphost) {
		String devicestr = null;
		JSONObject jsonObject = null;
    	Map typeMap = new HashMap();
    	
		devicestr = ClientSocket.ComWithServer(iphost, "Read Device Info");
		
		if(devicestr != null){
			jsonObject = JSONObject.fromObject(devicestr);
			Map output1 = (Map)JSONObject.toBean(jsonObject, Map.class,typeMap);
			deviceId = (String)output1.get("deviceid");
			
			typeMap = null;
			jsonObject = null;
			return true;
		}
		typeMap = null;
		jsonObject = null;
		return false;
	}
	
	@FXML
	public void GB_StartTestAction(ActionEvent e){
	
		rootPane.setCursor(Cursor.WAIT);
		
		try {
			testCount = Integer.valueOf(GB_TestCountFiled.getText());
			testDelayTime = Integer.valueOf(GB_TestRelayFiled.getText());
		} catch (Exception ex) {
			// TODO: handle exception
			testCount = null;
			testDelayTime = null;
		}
		
		if((testCount == null) || (testDelayTime == null)){
			rootPane.setCursor(Cursor.DEFAULT);
			
			showLogsDialog("格式不正确！");
			return;
		}
		
		if(!readDeviceInfo(GB_DeviceIPField.getText())){
			rootPane.setCursor(Cursor.DEFAULT);
			
			showLogsDialog("IP无法连接！");
			return;
		}
		
		GB_LabTestDatas.clear();
		GB_Chart.getData().clear();
		GB_TableView.getItems().clear();
		for (int i=0; i<testCount.intValue(); i++) {
			tempLabTestData = new LabTestData();
			tempLabTestData.setDid(deviceId);
			tempLabTestData.setUserid(userSession.getAccount());
			tempLabTestData.setTindex(String.format("%d/%d", i+1, testCount));
			GB_LabTestDatas.add(tempLabTestData);
			
			tempSeries = new Series<>();
			tempSeries.setName(String.format("%d", i+1));
			GB_Chart.getData().add(tempSeries);

			tempDeviceTestTableItem = new DeviceTestTableItem();
			GB_TableView.getItems().add(tempDeviceTestTableItem);
		}

		S_TestService.restart();
		
		rootPane.setCursor(Cursor.DEFAULT);
	}
	
	private void DataPointAction(Data<Number, Number> datap, Series<Number, Number> series, int sindex) {
		datap.getNode().getStyleClass().add("DataPointPane");
		datap.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				
				String comname = null;
				ArrayList<String> names = new ArrayList<>();
				ArrayList<CommandLinksButtonType> links = new ArrayList<>();
				int y=0,index;
				int max,min,temp;

				names.add("C");
				names.add("T");
							
				for (y=0; y < names.size(); y++ ) {
					links.add(new CommandLinksButtonType(names.get(y), true));
				}

				CommandLinksDialog dlg = new CommandLinksDialog(links);

				dlg.setTitle("请选择操作：");    
				        
				Optional<ButtonType> result = dlg.showAndWait();
				if(result.isPresent()){	
					if(result.get().getButtonData() == ButtonData.OK_DONE)
						comname = result.get().getText();
					}
					if(comname != null){
						if(comname.equals("C")){		
							//说明已经选择C线
							temp = GB_TableView.getItems().get(sindex).getcValue();
							if(temp >= 0)
							{
								series.getData().get(temp).getNode().getStyleClass().remove("SelectDataPoint");
								((StackPane)(series.getData().get(temp).getNode())).getChildren().clear();
							}
							
							if(datap.getXValue().intValue() > 25)
								y = datap.getXValue().intValue() - 25;
							else
								y = 0;
							
							max = 0;
							index = y;
							for(; y<(datap.getXValue().intValue() + 25); y++){
								temp = series.getData().get(y).getYValue().intValue();
								if(max < temp)
								{
									max = temp;
									index = y;
								}
							}
							
							if(series.getData().get(index).getNode().getStyleClass().contains("BigDataPoint"))
								series.getData().get(index).getNode().getStyleClass().remove("BigDataPoint");
							GB_TableView.getItems().get(sindex).setcValue(index);
							series.getData().get(index).getNode().getStyleClass().add("SelectDataPoint");
							((StackPane)(series.getData().get(index).getNode())).getChildren().add(new ImageView(cImage));	
						}
						else if(comname.equals("T")){
							//说明已经选择C线
							temp = GB_TableView.getItems().get(sindex).gettValue();
							if(temp >= 0)
							{
								series.getData().get(temp).getNode().getStyleClass().remove("SelectDataPoint");
								((StackPane)(series.getData().get(temp).getNode())).getChildren().clear();
							}
							
							if(datap.getXValue().intValue() > 25)
								y = datap.getXValue().intValue() - 25;
							else
								y = 0;
							
							max = 0;
							index = y;
							for(; y<(datap.getXValue().intValue() + 25); y++){
								temp = series.getData().get(y).getYValue().intValue();
								if(max < temp)
								{
									max = temp;
									index = y;
								}
							}
							if(series.getData().get(index).getNode().getStyleClass().contains("BigDataPoint"))
								series.getData().get(index).getNode().getStyleClass().remove("BigDataPoint");
							GB_TableView.getItems().get(sindex).settValue(index);
							series.getData().get(index).getNode().getStyleClass().add("SelectDataPoint");
							((StackPane)(series.getData().get(index).getNode())).getChildren().add(new ImageView(tImage));
							
						}
						
						//如果t线有选择，则自动找基线
						if((GB_TableView.getItems().get(sindex).gettValue() >= 0) && 
								(GB_TableView.getItems().get(sindex).gettValue() < GB_TableView.getItems().get(sindex).getcValue())){
							
							temp = GB_TableView.getItems().get(sindex).getbValue();
							if(temp >= 0)
							{
								series.getData().get(temp).getNode().getStyleClass().remove("SelectDataPoint");
								((StackPane)(series.getData().get(temp).getNode())).getChildren().clear();
							}
							
							y = GB_TableView.getItems().get(sindex).gettValue();
							
							min = 65000;
							index = y;
							for(; y<GB_TableView.getItems().get(sindex).getcValue(); y++){
								temp = series.getData().get(y).getYValue().intValue();
								if(min > temp)
								{
									min = temp;
									index = y;
								}
							}
							if(series.getData().get(index).getNode().getStyleClass().contains("BigDataPoint"))
								series.getData().get(index).getNode().getStyleClass().remove("BigDataPoint");
							GB_TableView.getItems().get(sindex).setbValue(index);
							series.getData().get(index).getNode().getStyleClass().add("SelectDataPoint");
							((StackPane)(series.getData().get(index).getNode())).getChildren().add(new ImageView(bImage));
							
							GB_TableView.getItems().get(sindex).settcValue(CalResult(series.getData().get(GB_TableView.getItems().get(sindex).gettValue()).getYValue().doubleValue(), 
									series.getData().get(GB_TableView.getItems().get(sindex).getbValue()).getYValue().doubleValue(), 
									series.getData().get(GB_TableView.getItems().get(sindex).getcValue()).getYValue().doubleValue()));
							GB_LabTestDatas.get(sindex).setT_l(GB_TableView.getItems().get(sindex).gettValue());
							GB_LabTestDatas.get(sindex).setB_l(GB_TableView.getItems().get(sindex).getbValue());
							GB_LabTestDatas.get(sindex).setC_l(GB_TableView.getItems().get(sindex).getcValue());
							GB_LabTestDatas.get(sindex).setT_c(GB_TableView.getItems().get(sindex).gettcValue());
							labTestDataRepository.save(GB_LabTestDatas.get(sindex));
						}
					}
				}
			});
	}
	
	private float CalResult(double t, double b, double c){
		if((t<b)||(c<=b)){
			return 0;
		}
		else{
			return (float) ((t-b)/(c-b));
		}
	}
	
	public void HideSeries(Series<Number, Number> series){
		Path aa = (Path) series.getNode();//charts.lookup(".chart-series-line.series"+index);
		aa.setVisible(false);
		 
		for (int i=0; i<series.getData().size(); i++ ) {
			 StackPane bb = (StackPane) series.getData().get(i).getNode();
			 bb.setVisible(false);
		}
	}
	
	public void ShowSeries(Series<Number, Number> series){
		Path aa = (Path) series.getNode();//charts.lookup(".chart-series-line.series"+index);
		aa.setVisible(true);
		 
		 for (int i=0; i<series.getData().size(); i++ ) {
			 StackPane bb = (StackPane) series.getData().get(i).getNode();
			 bb.setVisible(true);
		}
	}
	
	class TestService extends Service<Void>{
		
		@Override
		protected Task<Void> createTask() {
			// TODO Auto-generated method stub
			return new TestTask();
		}
		
		class TestTask extends Task<Void>{

			@Override
			protected Void call(){
				// TODO Auto-generated method stub
				return TestFun();
			}
			
			private Void TestFun(){
				
				String str;
				int retryNum = 0;

				LabTestData labTestData = null;
				
	        	testIndex = 0;

				while(true){

					labTestData = GB_LabTestDatas.get(testIndex);
					
					//启动测试
					retryNum = 0;
					while((retryNum++) < retryMaxNum){
						str = ClientSocket.ComWithServer(GB_DeviceIPField.getText(), "Start Test");
						
						if("OK".equals(str)){
							updateMessage("启动成功！");
							tempSeriesList.clear();
							break;
						}
						else if("Startted".equals(str)){
							updateMessage("设备忙！");
							retryNum = 100;
						}
						else {
							updateMessage("无响应 -- "+retryNum);
						}
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(retryNum > retryMaxNum){
						return null;
					}
					
					//读曲线数据
					retryNum = 0;

					while(retryNum < retryMaxNum){
						str = ClientSocket.ComWithServer(GB_DeviceIPField.getText(), "Read Test Data");
						if(str != null){
							retryNum = 0;

							jsonarray = JSONObject.fromObject(str);
							Map output1 = (Map)JSONObject.toBean(jsonarray, Map.class, typeMap);

							List<Integer> data = (List<Integer>) output1.get("data");
							int status = (int) output1.get("status");
							
							Platform.runLater(() -> {
								int serieldatasize;
								currentSeries = GB_Chart.getData().get(testIndex);
								serieldatasize = currentSeries.getData().size();
								
								for (int i=0; i<data.size(); i++) {
									if(data.get(i) == 0xffff){
										currentSeries.getData().clear();
										serieldatasize = 0;
										tempSeriesList.clear();
									}
									else{
										tempSeriesList.add(data.get(i));
										Data datap = new Data<Number, Number>((serieldatasize), data.get(i));
										currentSeries.getData().add(datap);
										DataPointAction(datap, currentSeries, testIndex.intValue());
										serieldatasize++;
									}
								}
							});
							jsonarray = null;
							
							if(status == 0){
								try {
									
									JSONArray jsonList = JSONArray.fromObject(tempSeriesList);
									labTestData.setSerie(jsonList.toString());
									labTestData.setTesttime(new Timestamp(System.currentTimeMillis()));
									labTestDataRepository.save(labTestData);
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
								break;
							}
						}
						else{
							updateMessage("无响应 -- "+retryNum);
							retryNum++;
						}
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(retryNum > retryMaxNum){
						return null;
					}
					
					testIndex++;
					
					if(testIndex >= testCount)
						break;

					try {
						Thread.sleep(testDelayTime*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return null;
			}
		}
	}
	
	public static class EditingCell extends TableCell<DeviceTestTableItem, String> {

        private TextField textField;

        public EditingCell() {

        }

        @Override 
        public void startEdit() {
            super.startEdit();
            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
            textField.requestFocus();
        }

        @Override 
        public void cancelEdit() {
            super.cancelEdit();
            setText((String) getItem());
            setGraphic(null);
            textField.clear();
        }

        @Override 
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                	if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {

            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {                
                @Override 
                public void handle(KeyEvent t) {

                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

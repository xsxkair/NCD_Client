package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.repository.TestDataRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.SystemSetData;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

@Component
public class ReportDetailHandler {

	private AnchorPane rootpane;
	private Pane S_FatherPane = null;
	
	private TestData testData = null;;
	private Card card = null;;
	private Device device = null;;
	private User user = null;
	
	private StringBuffer tempStringBuffer = null;
	private JSONArray jsonArray = null;
	private List<Integer> seriesdata = null;
	
	@FXML StackPane GB_RootStackPane;
	@FXML HBox GB_BackFatherPane;
	//测试信息
	@FXML Label GB_ChartPointLabel;
	@FXML LineChart<Number, Number> GB_TestLineChart;
	@FXML private Label GB_TestTimeLabel;
	@FXML private Label GB_EnvironmentTemperatureLabel;
	@FXML private Label GB_CardTemperatureLabel;
	@FXML private Label GB_DeviceLabel;
	@FXML private Label GB_CardLabel;
	@FXML private Label GB_SampleIDLabel;
	@FXML private Label GB_TesterLabel;
	@FXML private Label GB_RealWaittimeLabel;
	@FXML private Label GB_TestResultLabel;

	//报告信息
	@FXML Label GB_ManagerNameLabel;
	@FXML Label GB_ManagTimeLabel;
	@FXML Label GB_ReportStatusLabel;
	@FXML HBox GB_ReportStatusToggleHBox;
	@FXML JFXRadioButton GB_ReportOKToggleButton;
	@FXML JFXRadioButton GB_ReportErrorToggleButton;
	@FXML ToggleGroup S_ReportToogleGroup;
	@FXML TextField GB_ReportDescTextField;
	@FXML HBox GB_ReportControlHBox;
	@FXML JFXButton GB_EditReportButton;
	@FXML JFXButton GB_CommitReportButton;
	
	//权限认证对话框
	@FXML JFXDialog AccessDialog;
	@FXML PasswordField AccessPasswordTextField;
	@FXML JFXButton cancelButton0;
	@FXML JFXButton acceptButton0;
	
	//信息提示对话框
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHeadLabel;
	@FXML Label LogDialogContentLabel;
	@FXML JFXButton acceptButton1;

	private Series<Number, Number> series = new Series<>();
	private double firstPointX = 0;
	private int minPoint = -1;
	private StackPane tempNode = null;
	private User tempUser = null;
	
	@Autowired private TestDataRepository testDataRepository;
	@Autowired private CardRepository cardRepository;
	@Autowired private DeviceRepository deviceRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private UserSession userSession;
	@Autowired private WorkPageSession workPageSession;
	
	@PostConstruct
	public void UI_Init(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/ReportDetailPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/ReportDetailPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        GB_RootStackPane.getChildren().removeAll(AccessDialog, LogDialog);
        
        workPageSession.getWorkPane().addListener((o, oldVal, newVal) -> {
        	if(rootpane.equals(newVal)){
        		S_FatherPane = oldVal;
        		minPoint = -1;
        		tempStringBuffer = new StringBuffer();
        		Integer testDataId = (Integer) rootpane.getUserData();

        		testData = testDataRepository.findOne(testDataId);
        		card = cardRepository.findCardByCid(testData.getCid());
        		device = deviceRepository.findDeviceByDid(testData.getDid());
        		user = userRepository.findByAccount(testData.getAccount());
        		
        		//清空曲线
    			series.getData().clear();
    			
    			tempStringBuffer.setLength(0);
    			if(device != null){
    				tempStringBuffer.append(device.getDid());
        			tempStringBuffer.append(" (");
        			tempStringBuffer.append(device.getAddr());
        			tempStringBuffer.append(")");
    			}
    			else
    				tempStringBuffer.append('-');
    			
    			GB_DeviceLabel.setText(tempStringBuffer.toString());
    			
    			tempStringBuffer.setLength(0);
    			if(card != null){
        			tempStringBuffer.append(card.getCid());
        			tempStringBuffer.append('-');
        			tempStringBuffer.append(testData.getCnum());
        			tempStringBuffer.append(" (");
        			tempStringBuffer.append(card.getItem());
        			tempStringBuffer.append(')');
    			}
    			else
    				tempStringBuffer.append('-');
    			GB_CardLabel.setText(tempStringBuffer.toString());
    			
    			tempStringBuffer.setLength(0);
    			if(testData != null){
    				GB_SampleIDLabel.setText(testData.getSid());
    				GB_TesterLabel.setText(testData.getT_name());
    				
    				tempStringBuffer.setLength(0);
    				tempStringBuffer.append(testData.getTesttime());
    				GB_TestTimeLabel.setText(tempStringBuffer.toString());
    				
    				tempStringBuffer.setLength(0);
    				tempStringBuffer.append(testData.getE_t());
    				tempStringBuffer.append("℃");
    				GB_EnvironmentTemperatureLabel.setText(tempStringBuffer.toString());
    				
    				tempStringBuffer.setLength(0);
    				tempStringBuffer.append(testData.getO_t());
    				tempStringBuffer.append("℃");
    				GB_CardTemperatureLabel.setText(tempStringBuffer.toString());
    				
    				if(card != null){
    					tempStringBuffer.setLength(0);
        				if(testData.getOutt().doubleValue() > 60)
            				GB_RealWaittimeLabel.setStyle("-fx-text-fill : red");
            			else if(testData.getOutt().doubleValue() > 0)
            				GB_RealWaittimeLabel.setStyle("-fx-text-fill: #e78d47");
            			else
            				GB_RealWaittimeLabel.setStyle("-fx-text-fill: mediumseagreen");
        				
        				tempStringBuffer.append((testData.getOutt()+card.getWaitt())*60);
        				tempStringBuffer.append(" S (标准时间：");
        				tempStringBuffer.append(card.getWaitt()*60);
        				tempStringBuffer.append(" S)");
        				
        				GB_RealWaittimeLabel.setText(tempStringBuffer.toString());
    				}
    				else
    					GB_RealWaittimeLabel.setText("-");
    				
    				if("Ok".equals(testData.getT_re())){
    					tempStringBuffer.setLength(0);
    					tempStringBuffer.append(testData.getA_v());
    					tempStringBuffer.append(' ');
    					if(card != null)
    						tempStringBuffer.append(card.getDanwei());
        				GB_TestResultLabel.setText(tempStringBuffer.toString());
    					GB_TestResultLabel.setStyle("-fx-text-fill: #1a3f83");
    				}
        			else{
        				GB_TestResultLabel.setText("Error");
        				GB_TestResultLabel.setStyle("-fx-text-fill: red");
        			}
    			}
    			else{
    				GB_SampleIDLabel.setText("-");
    				GB_TesterLabel.setText("-");
    				GB_RealWaittimeLabel.setText("-");
    				GB_TestTimeLabel.setText("-");
    				GB_EnvironmentTemperatureLabel .setText("-");
    				GB_CardTemperatureLabel.setText("-");
    				GB_TestResultLabel.setText("-");
    			}
    			
    			//测试曲线
    			seriesdata = new ArrayList<>();
    			
    			if(testData != null){

        	        try {
        	        	jsonArray = (JSONArray) JSONSerializer.toJSON(testData.getSerie_a());
        		        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
        		        
        		        jsonArray = (JSONArray) JSONSerializer.toJSON(testData.getSerie_b());
        		        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
        		        
        		        jsonArray = (JSONArray) JSONSerializer.toJSON(testData.getSerie_c());
        		        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));

        			} catch (Exception e) {
        				seriesdata.clear();
        			}
        	        
        	        Integer t = testData.getT_l();
        	        Integer b = testData.getB_l();
        	        Integer c = testData.getC_l();

        	        for(int i=0; i<seriesdata.size(); i++){
        	        	Data<Number, Number> data = new Data<Number, Number>(i, seriesdata.get(i));
        	        	StackPane stackPane = new StackPane();
        	        	
        	        	stackPane.setPrefSize(3, 3);
        	        	
        	        	if((t != null) && (i == t.intValue())){
        	        		stackPane.setStyle("-fx-background-color:red");
        	        		stackPane.setPrefSize(10, 10);
        	        	}
        	        	else if((b != null) && (i == b.intValue())){
        	        		stackPane.setStyle("-fx-background-color:green");
        	        		stackPane.setPrefSize(10, 10);
        	        	}
        	        	else if((c != null) && (i == c.intValue())){
        	        		stackPane.setStyle("-fx-background-color:blue");
        	        		stackPane.setPrefSize(10, 10);
        	        	}
        	        	
        	        	data.setNode(stackPane);
        	        	series.getData().add(data);
        			}

        	        seriesdata = null;
    			}
    	        
    	        if(user == null){
    	        	GB_ManagerNameLabel.setText("-");
    	        }
    	        else{
    	        	GB_ManagerNameLabel.setText(user.getName());
    	        }
    	        
    	        if(testData != null){
    	        	if(testData.getHandletime() != null)
    	        		GB_ManagTimeLabel.setText(testData.getHandletime().toString());
    	        	else
    	        		GB_ManagTimeLabel.setText("-");
    	        	
    	        	if(testData.getR_desc() != null)
    	        		GB_ReportDescTextField.setText(testData.getR_desc());
    	        	else
    	        		GB_ManagTimeLabel.setText("-");
    	        }
    	        else{
    	        	GB_ManagTimeLabel.setText("-");
    	        	GB_ReportDescTextField.setText("-");
    	        }
    	        
    	        String r_result = testData.getResult();
    	        if(r_result.equals("合格")){
    	        	S_ReportToogleGroup.selectToggle(GB_ReportOKToggleButton);
    	        	GB_ReportOKToggleButton.setDisable(true);
    	        	GB_ReportErrorToggleButton.setDisable(true);
    	        	GB_ReportDescTextField.setEditable(false);
    	        	GB_EditReportButton.setText("编辑报告");
    	        	GB_ReportControlHBox.getChildren().setAll(GB_EditReportButton);
    	        }
    	        else if(r_result.equals("不合格")){
    	        	S_ReportToogleGroup.selectToggle(GB_ReportErrorToggleButton);
    	        	GB_ReportOKToggleButton.setDisable(true);
    	        	GB_ReportErrorToggleButton.setDisable(true);
    	        	GB_ReportDescTextField.setEditable(false);
    	        	GB_EditReportButton.setText("编辑报告");
    	        	GB_ReportControlHBox.getChildren().setAll(GB_EditReportButton);
    	        }
    	        else {
    				S_ReportToogleGroup.selectToggle(null);
    				GB_ReportOKToggleButton.setDisable(false);
    	        	GB_ReportErrorToggleButton.setDisable(false);
    	        	GB_ReportDescTextField.setEditable(true);
    	        	GB_EditReportButton.setText("编辑报告");
    	        	GB_ReportControlHBox.getChildren().setAll(GB_CommitReportButton);
    			}
    	        
    	        testData = null;
        		card = null;
        		device = null;
        		user = null;
        		seriesdata = null;
        		jsonArray = null;
        	}
        	
		});
 
        GB_TestLineChart.getData().add(series);
        GB_TestLineChart.setOnMouseEntered((e)->{
        	GB_ChartPointLabel.setVisible(true);
        });
        GB_TestLineChart.setOnMouseExited((e)->{
        	GB_ChartPointLabel.setVisible(false);
        	if(minPoint >= 0){
				tempNode = (StackPane) series.getData().get(minPoint).getNode();
				tempNode.getStyleClass().remove("BigDataPoint");
			}
        });
        GB_TestLineChart.setOnMouseMoved((e)->{
        	int dataSize = 0;
        	double min = 65535;
        	double tempjuli = 0;
        	
        	firstPointX = GB_TestLineChart.getYAxis().getWidth() +GB_TestLineChart.getYAxis().getLayoutX() + GB_TestLineChart.getChildrenUnmodifiable().get(1).getLayoutX() ;

			dataSize = series.getData().size();
			min = 65535;
			
			if(minPoint >= 0){
				tempNode = (StackPane) series.getData().get(minPoint).getNode();
				tempNode.getStyleClass().remove("BigDataPoint");
			}
			
			for (int j=0; j<dataSize; j++ ) {
				tempNode = (StackPane) series.getData().get(j).getNode();
				tempjuli = tempNode.getLayoutX()+firstPointX;

				if(tempjuli >= e.getX())
					tempjuli = tempjuli - e.getX();
				else
					tempjuli = e.getX() - tempjuli;
					 
				if(min > tempjuli){
					min = tempjuli;
					minPoint = j;
				}
			}
				
			if(minPoint >= 0){
				tempNode = (StackPane) series.getData().get(minPoint).getNode();
				GB_ChartPointLabel.setText(String.valueOf(series.getData().get(minPoint).getYValue().intValue()));
				tempNode.getStyleClass().add("BigDataPoint");
				GB_ChartPointLabel.setLayoutX(tempNode.getLayoutX() + firstPointX - GB_ChartPointLabel.getWidth()/2);
				GB_ChartPointLabel.setLayoutY(tempNode.getLayoutY() + 15);
			}
        });
        
        GB_ReportOKToggleButton.setUserData("合格");
        GB_ReportErrorToggleButton.setUserData("不合格");
        
        GB_BackFatherPane.setOnMouseClicked((e)->{
        	workPageSession.setWorkPane(S_FatherPane);
        });
        
        GB_EditReportButton.setOnAction((e)->{
        	if(GB_EditReportButton.getText().equals("编辑报告")){
        		GB_ReportOKToggleButton.setDisable(false);
	        	GB_ReportErrorToggleButton.setDisable(false);
	        	GB_ReportDescTextField.setEditable(true);
        		GB_EditReportButton.setText("取消编辑");
        		GB_ReportControlHBox.getChildren().setAll(GB_EditReportButton, GB_CommitReportButton);
        	}
        	else{
        		GB_ReportOKToggleButton.setDisable(true);
	        	GB_ReportErrorToggleButton.setDisable(true);
	        	GB_ReportDescTextField.setEditable(false);
        		GB_EditReportButton.setText("编辑报告");
        		GB_ReportControlHBox.getChildren().setAll(GB_EditReportButton);
        	}
        });
        
        GB_CommitReportButton.setOnAction((e)->{
        	AccessPasswordTextField.clear();
        	AccessDialog.show(GB_RootStackPane);
        });
        
        cancelButton0.setOnAction((e)->{
        	if(AccessDialog.isVisible())
        		AccessDialog.close();
        });
        
        acceptButton0.disableProperty().bind(new BooleanBinding() {
			
        	{
        		bind(AccessPasswordTextField.lengthProperty());
        	}
        	
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				if(AccessPasswordTextField.getLength() > 5)
					return false;
				else
					return true;
			}
		});
        acceptButton0.setOnAction((e)->{
        	
        	Integer testDataId = (Integer) rootpane.getUserData();
    		
        	AccessDialog.close();
        	
        	tempUser = userRepository.findByAccount(userSession.getAccount());
        	if(tempUser == null){
        		showLogDialog("错误", "错误！");
        	}
        	else if (!AccessPasswordTextField.getText().equals(tempUser.getPassword())) {
        		showLogDialog("错误", "密码错误，禁止操作！");
			}
        	else {
        		try {
            		testData = testDataRepository.findOne(testDataId);
            		testData.setResult((String) S_ReportToogleGroup.getSelectedToggle().getUserData());
            		testData.setR_desc(GB_ReportDescTextField.getText());
            		testData.setHandletime(new Timestamp(System.currentTimeMillis()));
            		
            		testData.setAccount(userSession.getAccount());
            		
            		testDataRepository.save(testData);
            		
            		workPageSession.setWorkPane(S_FatherPane);
            		
    			} catch (Exception e2) {
    				// TODO: handle exception
    				showLogDialog("错误", "数据提交失败,请重试！");
    			}
        		testData = null;
			}	
        });
        
        acceptButton1.setOnAction((e)->{
        	if(LogDialog.isVisible())
        		LogDialog.close();
        });
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
        
        loader = null;
        in = null;
	}

	public void startReportDetailActivity(Integer testDataId) {
		
		this.rootpane.setUserData(testDataId);
		
		workPageSession.setWorkPane(this.rootpane);
	}
	
	private void showLogDialog(String headstr, String contentstr){
		LogDialogHeadLabel.setText(headstr);
		LogDialogContentLabel.setText(contentstr);
		LogDialog.show(GB_RootStackPane);
	}
}

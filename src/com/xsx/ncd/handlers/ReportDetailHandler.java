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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

@Component
public class ReportDetailHandler {

	private AnchorPane rootpane;
	private Pane S_FatherPane = null;
	
	
	//设备信息
	@FXML private Label S_DeviceidLabel;
	@FXML private Label S_UserNameLabel;
	@FXML private Label S_DeviceLocationLabel;
	
	//试剂卡信息
	@FXML private Label S_CardidLabel;
	@FXML private Label S_ItemNameLabel;
	@FXML private Label S_NormalLabel;
	@FXML private Label S_WaittimeLabel;
	
	//操作人信息
	@FXML private Label S_TesterNameLabel;
	
	//测试信息
	@FXML LineChart<Number, Number> S_TestLineChart;
	@FXML private Label S_SampleIDLabel;
	@FXML private Label S_RealWaittimeLabel;
	@FXML private Label S_CardTempLabel;
	@FXML private Label S_EnTempLabel;
	@FXML private Label S_TesttimeLabel;
	@FXML private Label S_TestResultLabel;

	
	//报告信息
	@FXML
	Label GB_ManagerNameLabel;
	@FXML
	Label GB_ManagerTimeLabel;
	@FXML
	private JFXRadioButton S_ReportOK;
	@FXML
	private JFXRadioButton S_ReportError;
	@FXML
	private ToggleGroup S_ReportToogleGroup;
	@FXML
	private JFXTextField S_ReportDescTextArea;
	@FXML
	private Button S_CommitReportButton;
	@FXML
	private Button S_BackButton;
		
	private Series<Number, Number> series = new Series<>();
	
	@Autowired
	private TestDataRepository testDataRepository;
	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserSession userSession;
	@Autowired
	private WorkPageSession workPageSession;
	
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
        
        workPageSession.getWorkPane().addListener((o, oldVal, newVal) -> {
        	if(rootpane.equals(newVal)){
        		S_FatherPane = oldVal;
        		
        		Integer testDataId = (Integer) rootpane.getUserData();
        		
        		TestData testData = testDataRepository.findOne(testDataId);
        		Card card = cardRepository.findOne(testData.getCardid());
        		Device device = deviceRepository.findOne(testData.getDeviceid());
        		User user = userRepository.findOne(testData.getUserid());
        		
        		//清空曲线
    			series.getData().clear();
    			
    			S_DeviceidLabel.setText(device.getDid());
    			S_UserNameLabel.setText(device.getName());
    			S_DeviceLocationLabel.setText(device.getAddr());
    			
    			//试剂卡信息
    			S_CardidLabel.setText(card.getCid());
    			S_ItemNameLabel.setText(card.getItem());
    			
    			//操作人信息
    			S_TesterNameLabel.setText(testData.getT_name());
    			
    			//测试信息
    			S_SampleIDLabel.setText(testData.getSid());
    			
    			if(testData.getOutt().doubleValue() <= 10)
    				S_RealWaittimeLabel.setStyle("-fx-text-fill:mediumseagreen");
    			else
    				S_RealWaittimeLabel.setStyle("-fx-text-fill: red");
    			S_RealWaittimeLabel.setText(testData.getOutt()+card.getWaitt()*60+" S ( 标准时间："+card.getWaitt()*60+" S )");
    			
    			S_CardTempLabel.setText("试剂卡温度："+testData.getO_t()+" ℃");
    			S_EnTempLabel.setText("环境温度："+testData.getE_t()+" ℃");
    			S_TesttimeLabel.setText("测试时间："+ testData.getTesttime());
    			S_TestResultLabel.setText(testData.getA_v()+" " + card.getDanwei() + " ( 参考值："+card.getNormal()+" " + card.getDanwei() + " )");
    			
    			//测试信息
    			JSONArray jsonArray = null;
    	        List<Integer> seriesdata = new ArrayList<>();
    	        
    	        try {
    	        	jsonArray = (JSONArray) JSONSerializer.toJSON(testData.getSerie_a());
    		        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
    		        
    		        jsonArray = (JSONArray) JSONSerializer.toJSON(testData.getSerie_b());
    		        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
    		        
    		        jsonArray = (JSONArray) JSONSerializer.toJSON(testData.getSerie_c());
    		        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));

    			} catch (Exception e) {
    				// TODO: handle exception
    				e.printStackTrace();
    				
    				seriesdata.clear();
    			}

    	        Integer t = testData.getT_l();
    	        Integer b = testData.getB_l();
    	        Integer c = testData.getC_l();

    	        for(int i=0; i<seriesdata.size(); i++){
    	        	Data<Number, Number> data = new Data<Number, Number>(i, seriesdata.get(i));
    	        	StackPane stackPane = new StackPane();
    	        	
    	        	stackPane.setPrefSize(0, 0);
    	        	
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

    	        if(user == null){
    	        	GB_ManagerNameLabel.setText("null");
    	        }
    	        else{
    	        	GB_ManagerNameLabel.setText(user.getName());
    	        }
    	        
    	        Timestamp handlerTime = testData.getHandletime();
    	        if(handlerTime == null)
    	        	GB_ManagerTimeLabel.setText("null");
    	        else
    	        	GB_ManagerTimeLabel.setText(handlerTime.toLocaleString());
    	        
    	        String r_result = testData.getResult();
    	        if(r_result.equals("合格")){
    	        	S_ReportToogleGroup.selectToggle(S_ReportOK);
    	        }
    	        else if(r_result.equals("不合格")){
    	        	S_ReportToogleGroup.selectToggle(S_ReportError);
    	        }
    	        else {
    				S_ReportToogleGroup.selectToggle(null);
    			}
    	        
    	        S_ReportDescTextArea.setText(testData.getR_desc());
        	}
        		
		});

        
        S_TestLineChart.getData().add(series);
        
        S_ReportOK.setUserData("合格");
        S_ReportError.setUserData("不合格");
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}

	public void startReportDetailActivity(Integer testDataId) {
		
		this.rootpane.setUserData(testDataId);
		
		workPageSession.setWorkPane(this.rootpane);
	}

	@FXML
	public void S_CommitReportAction(){
		Integer testDataId = (Integer) rootpane.getUserData();
		
		TestData testData = testDataRepository.findOne(testDataId);
		testData.setResult((String) S_ReportToogleGroup.getSelectedToggle().getUserData());
		testData.setR_desc(S_ReportDescTextArea.getText());
		testData.setHandletime(new Timestamp(System.currentTimeMillis()));
		
		User user = userRepository.findByAccount(userSession.getAccount());
		testData.setUserid(userSession.getUser().getId());
		
		testDataRepository.save(testData);
		
		workPageSession.setWorkPane(S_FatherPane);
	}
	
	@FXML
	public void S_BackAction(){
		workPageSession.setWorkPane(S_FatherPane);
	}
	
	@FXML
	public void GB_PrintfReportAction(){
		
	}
}

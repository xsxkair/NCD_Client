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
import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.ManagerRepository;
import com.xsx.ncd.repository.TestDataRepository;
import com.xsx.ncd.spring.ManagerSession;
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
	
	private ObjectProperty<TestData> S_ReportData;
	
	
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
	private ManagerRepository managerRepository;
	@Autowired
	private ManagerSession managerSession;
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
        	if(oldVal == rootpane)
        		S_ReportData.set(null);
        	else if(newVal == rootpane)
        		S_FatherPane = oldVal;
		});
        
        S_ReportData = new SimpleObjectProperty<>(null);
        S_ReportData.addListener((o, oldVal, newVal) -> {
        	//清空曲线
			series.getData().clear();
			
			S_DeviceidLabel.setText(newVal.getDevice().getDid());
			S_UserNameLabel.setText(newVal.getDevice().getName());
			S_DeviceLocationLabel.setText(newVal.getDevice().getAddr());
			
			//试剂卡信息
			S_CardidLabel.setText(newVal.getCard().getCid());
			S_ItemNameLabel.setText(newVal.getCard().getItem());
			
			//操作人信息
			S_TesterNameLabel.setText(newVal.getT_name());
			
			//测试信息
			S_SampleIDLabel.setText(newVal.getSid());
			
			if(newVal.getOutt().doubleValue() <= 10)
				S_RealWaittimeLabel.setStyle("-fx-text-fill:mediumseagreen");
			else
				S_RealWaittimeLabel.setStyle("-fx-text-fill: red");
			S_RealWaittimeLabel.setText(newVal.getOutt()+newVal.getCard().getWaitt()*60+" S ( 标准时间："+newVal.getCard().getWaitt()*60+" S )");
			
			S_CardTempLabel.setText("试剂卡温度："+newVal.getO_t()+" ℃");
			S_EnTempLabel.setText("环境温度："+newVal.getE_t()+" ℃");
			S_TesttimeLabel.setText("测试时间："+ newVal.getTesttime());
			S_TestResultLabel.setText(newVal.getA_v()+" " + newVal.getCard().getDanwei() + " ( 参考值："+newVal.getCard().getNormal()+" " + newVal.getCard().getDanwei() + " )");
			
			//测试信息
			JSONArray jsonArray = null;
	        List<Integer> seriesdata = new ArrayList<>();
	        
	        try {
	        	jsonArray = (JSONArray) JSONSerializer.toJSON(newVal.getSerie_a());
		        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
		        
		        jsonArray = (JSONArray) JSONSerializer.toJSON(newVal.getSerie_b());
		        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
		        
		        jsonArray = (JSONArray) JSONSerializer.toJSON(newVal.getSerie_c());
		        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				seriesdata.clear();
			}

	        Integer t = newVal.getT_l();
	        Integer b = newVal.getB_l();
	        Integer c = newVal.getC_l();

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
	        
	        Manager manager = newVal.getManager();
	        if(manager == null){
	        	GB_ManagerNameLabel.setText("null");
	        }
	        else{
	        	GB_ManagerNameLabel.setText(manager.getName());
	        }
	        
	        Timestamp handlerTime = newVal.getHandletime();
	        if(handlerTime == null)
	        	GB_ManagerTimeLabel.setText("null");
	        else
	        	GB_ManagerTimeLabel.setText(handlerTime.toLocaleString());
	        
	        String r_result = newVal.getResult();
	        if(r_result.equals("合格")){
	        	S_ReportToogleGroup.selectToggle(S_ReportOK);
	        }
	        else if(r_result.equals("不合格")){
	        	S_ReportToogleGroup.selectToggle(S_ReportError);
	        }
	        else {
				S_ReportToogleGroup.selectToggle(null);
			}
	        
	        S_ReportDescTextArea.setText(newVal.getR_desc());
		});

        
        S_TestLineChart.getData().add(series);
        
        S_ReportOK.setUserData("合格");
        S_ReportError.setUserData("不合格");
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}

	public void startReportDetailActivity(TestData testData) {
		
		if(testData == null)
			return;
		
		S_ReportData.set(testData);
		
		workPageSession.setWorkPane(this.rootpane);
	}

	@FXML
	public void S_CommitReportAction(){
		TestData testData = S_ReportData.get();
		testData.setResult((String) S_ReportToogleGroup.getSelectedToggle().getUserData());
		testData.setR_desc(S_ReportDescTextArea.getText());
		testData.setHandletime(new Timestamp(System.currentTimeMillis()));
		
		Manager tempmanager = managerRepository.findManagerByAccount(managerSession.getAccount());
		testData.setManager(tempmanager);
		
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

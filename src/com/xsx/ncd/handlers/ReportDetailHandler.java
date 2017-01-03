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
	private RadioButton S_ReportOK;
	@FXML
	private RadioButton S_ReportNotOK;
	@FXML
	private ToggleGroup S_ReportResultToogleGroup;
	@FXML
	private TextArea S_ReportDescTextArea;
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
	private DeviceRepository deviceRepository;
	@Autowired
	private ManagerSession managerSession;
	
	@Autowired
	private WorkPageSession workPageSession;
	@Autowired
	private SystemSetData systemSetData;
	
	@PostConstruct
	public void UI_Init(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("ReportDetailPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("ReportDetailPage.fxml");
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
		});

/*			@Override
			public void changed(ObservableValue<? extends TestDataBean> observable, TestDataBean oldValue, TestDataBean newValue) {
				// TODO Auto-generated method stub
				
				//清空曲线
				series.getData().clear();
				
				S_DeviceidLabel.setText("-");
				S_UserNameLabel.setText("-");
				S_UserAgeLabel.setText("-");
				S_UserSexLabel.setText("-");
				S_UserJobLabel.setText("-");
				S_UserPhoneLabel.setText("-");
				S_UserDescLabel.setText("-");
				S_DeviceLocationLabel.setText("-");
				
				S_CardidLabel.setText("-");
				S_ItemNameLabel.setText("-");
				S_NormalLabel.setText("-");
				S_WaittimeLabel.setText("-");
				S_OuttimeLabel.setText("-");

				S_TesterNameLabel.setText("-");
				S_TesterAgeLabel.setText("-");
				S_TesterSexLabel.setText("-");
				S_TesterJobLabel.setText("-");
				S_TesterPhoneLabel.setText("-");
				S_TesterDescLabel.setText("-");
				
				S_SampleIDLabel.setText("-");
		        S_CardTempLabel.setText("-");
		        S_EnTempLabel.setText("-");
		        S_TesttimeLabel.setText("-");
		        S_ReportUpTimeLabel.setText("-");
		        S_RealWaittimeLabel.setText("-");
		        
				//报告信息
		        GB_ManagerNameLabel.setText("-");
		        GB_ManagerTimeLabel.setText("-");
		        S_ReportDescTextArea.setText("-");
				
				if(newValue != null){
					TestDataBean testDataBean = null;
					CardBean cardBean = null;
					DeviceBean deviceBean = null;
					PersonBean sampleperson = null;
					DevicerBean tester = null;
					ManagerBean managerBean = null;
					
					testDataBean = newValue;
					
					cardBean = ReportDao.ReadCardByCID(newValue.getCid());
					deviceBean = DeviceInfoDao.ReadDeviceByDID(newValue.getDid());
					tester = DeviceInfoDao.ReadDevicerByID(newValue.getT_id());
					managerBean = ManagerDao.QueryReportManager(newValue.getM_account(), null);
					sampleperson = PersonDao.QueryPerson(newValue.getS_id());
					
					
					//更新设备信息
					S_DeviceidLabel.setText((testDataBean.getDid() == null)?"null":testDataBean.getDid().toString());
					
					S_UserNameLabel.setText((deviceBean.getName() == null)?"null":deviceBean.getName().toString());
					S_UserAgeLabel.setText((deviceBean.getAge() == null)?"null":deviceBean.getAge().toString());
					S_UserSexLabel.setText((deviceBean.getSex() == null)?"null":deviceBean.getSex().toString());
					S_UserJobLabel.setText((deviceBean.getJob() == null)?"null":deviceBean.getJob().toString());
					S_UserPhoneLabel.setText((deviceBean.getPhone() == null)?"null":deviceBean.getPhone().toString());
					S_UserDescLabel.setText((deviceBean.getDsc() == null)?"null":deviceBean.getDsc().toString());
					
					if(deviceBean != null)
						S_DeviceLocationLabel.setText((deviceBean.getDaddr() == null)?"null":deviceBean.getDaddr().toString());
					
					//试剂卡信息
					S_CardidLabel.setText((testDataBean.getCid() == null)?"null":testDataBean.getCid().toString());
					S_ItemNameLabel.setText((testDataBean.getCitem() == null)?"null":testDataBean.getCitem().toString());
					if(cardBean != null){
						S_NormalLabel.setText((cardBean.getN_v() == null)?"null":cardBean.getN_v().toString());
						S_WaittimeLabel.setText((cardBean.getWaitt() == null)?"null":cardBean.getWaitt().toString());
						S_OuttimeLabel.setText((cardBean.getOutdate() == null)?"null":cardBean.getOutdate().toString());
					}
					
					
					//操作人信息
					S_TesterNameLabel.setText((testDataBean.getT_name() == null)?"null":testDataBean.getT_name().toString());
					if(tester != null){
						S_TesterAgeLabel.setText((tester.getAge() == null)?"null":tester.getAge().toString());
						S_TesterSexLabel.setText((tester.getSex() == null)?"null":tester.getSex().toString());
						S_TesterJobLabel.setText((tester.getJob() == null)?"null":tester.getJob().toString());
						S_TesterPhoneLabel.setText((tester.getPhone() == null)?"null":tester.getPhone().toString());
						S_TesterDescLabel.setText((tester.getDsc() == null)?"null":tester.getDsc().toString());
					}

					//测试信息
					JSONArray jsonArray = null;
			        List<Integer> seriesdata = new ArrayList<>();
			        
			        try {
			        	jsonArray = (JSONArray) JSONSerializer.toJSON(testDataBean.getSerie_a());
				        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
				        
				        jsonArray = (JSONArray) JSONSerializer.toJSON(testDataBean.getSerie_b());
				        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
				        
				        jsonArray = (JSONArray) JSONSerializer.toJSON(testDataBean.getSerie_c());
				        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
				        
				        jsonArray = (JSONArray) JSONSerializer.toJSON(testDataBean.getSerie_d());
				        seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

			        Integer t = testDataBean.getT_l();
			        Integer b = testDataBean.getB_l();
			        Integer c = testDataBean.getC_l();

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
	
			        S_SampleIDLabel.setText((testDataBean.getSampleid() == null)?"null":testDataBean.getSampleid().toString());
			        S_CardTempLabel.setText((testDataBean.getO_t() == null)?"null":testDataBean.getO_t().toString());
			        S_EnTempLabel.setText((testDataBean.getE_t() == null)?"null":testDataBean.getE_t().toString());
			        S_TesttimeLabel.setText((testDataBean.getTesttime() == null)?"null":testDataBean.getTesttime().toString());
			        S_ReportUpTimeLabel.setText((testDataBean.getUptime() == null)?"null":testDataBean.getUptime().toString());
			        
			        if(cardBean != null)
			        	S_RealWaittimeLabel.setText(((cardBean.getWaitt() == null)?0:cardBean.getWaitt()*60)+(testDataBean.getOutt()==null?0:testDataBean.getOutt())+" 秒");
			        
					//报告信息
			        GB_ManagerNameLabel.setText((testDataBean.getM_name() == null)?"无":testDataBean.getM_name().toString());
			        GB_ManagerTimeLabel.setText((testDataBean.getHandletime() == null)?"无":testDataBean.getHandletime().toString());
					
			        String string = testDataBean.getResult();

					if (string == null) {	
						S_ReportResultToogleGroup.selectToggle(null);
					}
					else if (string.equals("合格")) {
						S_ReportResultToogleGroup.selectToggle(S_ReportOK);
						S_ReportDescTextArea.setText((testDataBean.getR_desc() == null)?"null":testDataBean.getR_desc().toString());
					}
					else if (string.equals("不合格")) {
						S_ReportResultToogleGroup.selectToggle(S_ReportNotOK);
						S_ReportDescTextArea.setText((testDataBean.getR_desc() == null)?"null":testDataBean.getR_desc().toString());
					}
					else{
						S_ReportResultToogleGroup.selectToggle(null);
					}
				}
			}
		});*/
        
        S_TestLineChart.getData().add(series);
        
        S_ReportOK.setUserData("合格");
        S_ReportNotOK.setUserData("不合格");
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}

	public void startReportDetailActivity(TestData testData) {
		
		if(testData == null)
			return;
		
		workPageSession.setWorkPane(this.rootpane);
	}

	
/*	@FXML
	public void S_DeleteReportAction(){
		ReportDao.DeleteReport(S_ReportData.get());
		
		UIMainPage.GetInstance().setGB_Page(ReportListPage.GetInstance().GetReportPane());
	}
	*/
	@FXML
	public void S_CommitReportAction(){
		TestDataBean testDataBean = S_ReportData.get();
		testDataBean.setResult((String) S_ReportResultToogleGroup.getSelectedToggle().getUserData());
		testDataBean.setR_desc(S_ReportDescTextArea.getText());
		testDataBean.setHandletime(new Timestamp(System.currentTimeMillis()));
		
		ManagerBean tempmanager = ManagerDao.QueryReportManager(SignedManager.GetInstance().getGB_SignedAccount(), null);
		testDataBean.setM_account(tempmanager.getAccount());
		testDataBean.setM_name(tempmanager.getName());
		
		GB_ManagerNameLabel.setText((testDataBean.getM_name() == null)?"无":testDataBean.getM_name().toString());
        GB_ManagerTimeLabel.setText((testDataBean.getHandletime() == null)?"无":testDataBean.getHandletime().toString());
		
		ReportDao.UpdateReport(testDataBean);
		
		UIMainPage.GetInstance().setGB_Page(S_FatherPane);
	}
	
	@FXML
	public void S_BackAction(){
		UIMainPage.GetInstance().setGB_Page(S_FatherPane);
	}
	
	@FXML
	public void GB_PrintfReportAction(){
		
	}
}

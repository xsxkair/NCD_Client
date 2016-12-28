package org.xsx.ncd.UI.MainScene.DevicePage.DeviceDetailPage;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

import org.xsx.ncd.Data.UIMainPage;
import org.xsx.ncd.UI.MainScene.DevicePage.DeviceDataPackage;
import org.xsx.ncd.UI.MainScene.DevicePage.DeviceTipInfo;
import org.xsx.ncd.servicetask.ReadOneDeviceService;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class DeviceDetailPage {

	private static DeviceDetailPage S_DeviceDetailPage = null;
	
	private AnchorPane rootpane;
	
	private String S_DeviceId ;
	
	private ReadOneDeviceService S_ReadOneDeviceService = new ReadOneDeviceService();
	
	@FXML
	ImageView GB_DeviceImg;
	
	@FXML
	Label GB_DeviceIDLabel;
	@FXML
	Label GB_DevicerNameLabel;
	@FXML
	Label GB_DevicerAgeLabel;
	@FXML
	Label GB_DevicerSexLabel;
	@FXML
	Label GB_DevicerPhoneLabel;
	@FXML
	Label GB_DevicerJobLabel;
	@FXML
	Label GB_DevicerDescLabel;
	@FXML
	Label GB_DevicerAddrLabel;
	
	@FXML
	ListView<String> GB_DevicerListView;
	@FXML
	TextField GB_DevicerNameTextFiled;
	@FXML
	TextField GB_DevicerAgeTextFiled;
	@FXML
	TextField GB_DevicerSexTextFiled;
	@FXML
	TextField GB_DevicerPhoneTextFiled;
	@FXML
	TextField GB_DevicerJobTextFiled;
	@FXML
	TextField GB_DevicerDescTextFiled;
	
	@FXML
	LineChart<String, Number> GB_DeviceLineChart;
	private Series<String, Number> chartseries;
	@FXML
	CategoryAxis GB_DeviceXaxis;
	@FXML
	NumberAxis GB_DeviceYaxis;
	
	private DeviceDetailPage() {
		
	}
	
	public static DeviceDetailPage GetInstance() {
		if(S_DeviceDetailPage == null)
			S_DeviceDetailPage = new DeviceDetailPage();
		
		return S_DeviceDetailPage;
	}
	
	public void UI_Init(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("DeviceDetialPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("DeviceDetialPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chartseries = new Series<>();
       
        GB_DeviceLineChart.getData().add(chartseries);
        
        UIMainPage.GetInstance().getGB_Page().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootpane.equals(newValue)){
					S_ReadOneDeviceService.setS_DeviceID(getS_DeviceId());
					S_ReadOneDeviceService.restart();
					UpDeviceTestCountInfo();
				}
				
				if(rootpane.equals(oldValue)){
					S_ReadOneDeviceService.cancel();
				}
			}
		});
        
        S_ReadOneDeviceService.setPeriod(Duration.minutes(5));
        S_ReadOneDeviceService.lastValueProperty().addListener(new ChangeListener<DeviceDataPackage>() {

			@Override
			public void changed(ObservableValue<? extends DeviceDataPackage> observable, DeviceDataPackage oldValue,
					DeviceDataPackage newValue) {
				// TODO Auto-generated method stub
				UpDeviceInfo(newValue);
			}
		});
        
        GB_DevicerListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
/*				DevicerBean devicerBean = S_ReadOneDeviceService.getLastValue().getDevicerlist().get(newValue.intValue());
				
				if(devicerBean != null){
					GB_DevicerNameTextFiled.setText(devicerBean.getName() == null?"-":devicerBean.getName());
					GB_DevicerAgeTextFiled.setText(devicerBean.getAge() == null?"-":devicerBean.getAge());
					GB_DevicerSexTextFiled.setText(devicerBean.getSex() == null?"-":devicerBean.getSex());
					GB_DevicerPhoneTextFiled.setText(devicerBean.getPhone() == null?"-":devicerBean.getPhone());
					GB_DevicerJobTextFiled.setText(devicerBean.getJob() == null?"-":devicerBean.getJob());
					GB_DevicerDescTextFiled.setText(devicerBean.getDsc() == null?"-":devicerBean.getDsc());
				}*/
			}
		});
        
        rootpane.getStylesheets().add(this.getClass().getResource("devicedetial.css").toExternalForm());
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public AnchorPane GetPane() {
		return rootpane;
	}
	
	private void UpDeviceInfo(DeviceDataPackage datas) {
		
		GB_DeviceImg.setImage(new Image(this.getClass().getResourceAsStream("/RES/deviceico_off.png")));;
		
		GB_DeviceIDLabel.setText("-");
		GB_DevicerNameLabel.setText("-");
		GB_DevicerAgeLabel.setText("-");
		GB_DevicerSexLabel.setText("-");
		GB_DevicerPhoneLabel.setText("-");
		GB_DevicerJobLabel.setText("-");
		GB_DevicerDescLabel.setText("-");
		GB_DevicerAddrLabel.setText("-");
		
		GB_DevicerListView.getItems().clear();;
		GB_DevicerNameTextFiled.setText("-");
		GB_DevicerAgeTextFiled.setText("-");
		GB_DevicerSexTextFiled.setText("-");
		GB_DevicerPhoneTextFiled.setText("-");
		GB_DevicerJobTextFiled.setText("-");
		GB_DevicerDescTextFiled.setText("-");

		if(datas.getDeviceBean() != null){
			GB_DeviceIDLabel.setText(datas.getDeviceBean().getDid());
			GB_DevicerAddrLabel.setText(datas.getDeviceBean().getAddr());
			GB_DevicerNameLabel.setText((datas.getDeviceBean().getName() == null)?"-":datas.getDeviceBean().getName());
			GB_DevicerAgeLabel.setText((datas.getDeviceBean().getAge() == null)?"-":datas.getDeviceBean().getAge());
			GB_DevicerSexLabel.setText((datas.getDeviceBean().getSex() == null)?"-":datas.getDeviceBean().getSex());
			GB_DevicerPhoneLabel.setText((datas.getDeviceBean().getPhone() == null)?"-":datas.getDeviceBean().getPhone());
			GB_DevicerJobLabel.setText((datas.getDeviceBean().getJob() == null)?"-":datas.getDeviceBean().getJob());
			GB_DevicerDescLabel.setText((datas.getDeviceBean().getDsc() == null)?"-":datas.getDeviceBean().getDsc());
		}
		
		/*if(datas.getDevicerlist() != null){
			for (DevicerBean devicer : datas.getDevicerlist()) {
				if(devicer.getName() != null)
					GB_DevicerListView.getItems().add(devicer.getName());
			}
		}*/
	}
	
	private void UpDeviceTestCountInfo() {
		chartseries.getData().clear();
		
		List<Object[]> countinfo = DeviceInfoDao.QueryDeviceTestCounyGroupByDate(getS_DeviceId());
		
		if(countinfo == null)
			return;

		for (Object[] objects : countinfo) {
			String timelabel = (String) objects[0];
			BigInteger num = (BigInteger) objects[1];
			
			if(timelabel != null){
				Data<String, Number> point = new Data<String, Number>(timelabel, num.intValue());
				StackPane pointui = new StackPane();
				pointui.getStyleClass().add("chartpoint");
				point.setNode(pointui);
				
				Label tiplabel = new Label("日期："+timelabel+"\n"+"数目："+num.intValue());
				tiplabel.setFont(new Font("System", 16));
				
				Tooltip tooltip = new Tooltip();
				tooltip.setGraphic(tiplabel);
		        Tooltip.install(pointui, tooltip);

				chartseries.getData().add(point);
			}
		}
		
		System.out.println(chartseries.getNode());
		
		chartseries.getNode().getStyleClass().add("chartstyle");

	}
	
	public String getS_DeviceId() {
		return S_DeviceId;
	}

	public void setS_DeviceId(String s_DeviceId) {
		S_DeviceId = s_DeviceId;
	}
}

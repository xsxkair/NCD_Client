package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.handlers.ReportListHandler.QueryReportService.QueryReportTask;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.util.Duration;

@Component
public class DeviceDetailHandler {
	
	private AnchorPane rootpane;
	
	private Pane fatherPane;
	
	private Device S_Device ;
	
	@FXML ImageView GB_DeviceImg;
	
	@FXML Label GB_DeviceIDLabel;
	@FXML Label GB_DevicerNameLabel;
	@FXML Label GB_DevicerAgeLabel;
	@FXML Label GB_DevicerSexLabel;
	@FXML Label GB_DevicerPhoneLabel;
	@FXML Label GB_DevicerJobLabel;
	@FXML Label GB_DevicerDescLabel;
	@FXML Label GB_DevicerAddrLabel;
	
	@FXML LineChart<String, Number> GB_DeviceLineChart;
	private Series<String, Number> chartseries;
	@FXML CategoryAxis GB_DeviceXaxis;
	@FXML NumberAxis GB_DeviceYaxis;
	@FXML VBox GB_FreshPane;
	
	private ContextMenu myContextMenu;
	private MenuItem myMenuItem1;
	private MenuItem myMenuItem2;
	
	@Autowired
	private WorkPageSession workPageSession;
	@Autowired
	private DeviceRepository deviceRepository;
	
	private QueryDeviceActivenessService s_QueryDeviceActivenessService;
	
	@PostConstruct
	public void UI_Init(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/DeviceDetialPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/DeviceDetialPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        myMenuItem1 = new MenuItem("刷新");
        myMenuItem2 = new MenuItem("返回");
        myContextMenu = new ContextMenu(myMenuItem1, myMenuItem2);
        
        chartseries = new Series<>();
       
        GB_DeviceLineChart.getData().add(chartseries);
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue) ->{
        	
        	if(rootpane.equals(newValue)){
        		fatherPane = oldValue;
        		
        		UpDeviceInfo();
        		
        		s_QueryDeviceActivenessService.restart();
			}
        	else if (rootpane.equals(oldValue)) {
        		s_QueryDeviceActivenessService.cancel();
				S_Device = null;
				chartseries.getData().clear();
			}
        });

        rootpane.getStylesheets().add(this.getClass().getResource("/com/xsx/ncd/views/devicedetial.css").toExternalForm());
        
        rootpane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if(event.getButton().equals(MouseButton.SECONDARY)){
					myContextMenu.show(rootpane, event.getScreenX(), event.getScreenY());
				}
				else
					myContextMenu.hide();
			}
		});
        
        //刷新
        myMenuItem1.setOnAction(new EventHandler<ActionEvent>() {

  			@Override
  			public void handle(ActionEvent event) {
  				// TODO Auto-generated method stub
  				UpDeviceInfo();
        		
  				s_QueryDeviceActivenessService.restart();
  			}
  		});
      		
      	//返回
        myMenuItem2.setOnAction(new EventHandler<ActionEvent>() {

  			@Override
  			public void handle(ActionEvent event) {
  				// TODO Auto-generated method stub
  				workPageSession.setWorkPane(fatherPane);
  			}
  		});	
        
        s_QueryDeviceActivenessService = new QueryDeviceActivenessService();
        GB_FreshPane.visibleProperty().bind(s_QueryDeviceActivenessService.runningProperty());
        s_QueryDeviceActivenessService.valueProperty().addListener((o, oldValue, newValue)->{
        	chartseries.getData().clear();
        	
        	if(newValue != null){
        		for (Object[] objects : newValue) {
        			String timelabel = (String) objects[0];
        			Long num = (Long) objects[1];
        			
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
        		
        		chartseries.getNode().getStyleClass().add("chartstyle");
        	}
        });
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public void ShowDeviceDetail(Device device){
		
		S_Device = device;
		
		workPageSession.setWorkPane(rootpane);
	}
	
	private void UpDeviceInfo() {
		
		Image image = null;
		
		Device device = deviceRepository.findById(S_Device.getId());
		
		Long devicetime = device.getTime();
		long currenttime = System.currentTimeMillis();

		if((devicetime == null) || ((currenttime > devicetime) && (currenttime - devicetime > 120000))){
			image = new Image(this.getClass().getResourceAsStream("/RES/deviceico_off.png"));
		}
		else if("OK".equals(device.getStatus())){
			image = new Image(this.getClass().getResourceAsStream("/RES/deviceico_ok.png"));
		}
		else {
			image = new Image(this.getClass().getResourceAsStream("/RES/deviceico_error.png"));
		}
		
		GB_DeviceImg.setImage(image);;
		
		GB_DeviceIDLabel.setText(device.getDid());
		GB_DevicerNameLabel.setText(device.getName());
		
		GB_DevicerAgeLabel.setText(device.getAge());
		GB_DevicerSexLabel.setText(device.getSex());
		GB_DevicerPhoneLabel.setText(device.getPhone());
		GB_DevicerJobLabel.setText(device.getJob());
		GB_DevicerDescLabel.setText(device.getDsc());
		GB_DevicerAddrLabel.setText(device.getAddr());

	}
	
	//查询当前设备的活跃度
	class QueryDeviceActivenessService extends Service<List<Object[]>>{

		@Override
		protected Task<List<Object[]>> createTask() {
				// TODO Auto-generated method stub
			return new QueryReportTask();
		}
			
		class QueryReportTask extends Task<List<Object[]>>{
				
			@Override
			protected List<Object[]> call(){
					// TODO Auto-generated method stub

					return deviceRepository.queryDeviceActiveness(S_Device);
				}
			}
		}	
	
}

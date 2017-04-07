package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.define.LabDataTableItem;
import com.xsx.ncd.entity.LabTestData;
import com.xsx.ncd.handlers.LabDataListHandler.QueryLabDataService;
import com.xsx.ncd.repository.LabTestDataRepository;
import com.xsx.ncd.spring.SystemSetData;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

@Component
public class LabDataSeeHandler {

	private AnchorPane rootPane = null;
	private Pane fatherPane = null;
	
	@FXML LineChart<Number, Number> GB_Chart;
	@FXML FlowPane GB_InfoVBox;
	private Label GB_CurrentLabel;
	private Label tempLabel = null;
	private int selectSeries = -1;
	private Image tImage = null;
	private Text tValueLabel = null;
	private Image bImage = null;
	private Text bValueLabel = null;
	private Image cImage = null;
	private Text cValueLabel = null;
	private Data<Number, Number> tempPoint = null;
	private StackPane tempPointNode = null;
	private Group tempGroup = null;
	private DropShadow dropShadow = null;
	
	private List<Integer> labDataIds = null;
	private List<LabTestData> labDatas = null;
	private JSONArray jsonArray = null;
	private Series<Number, Number> tempSeries = null;
	private List<Integer> tempData = null;
	private int tempValue = 0;
	private StringBuffer tempStringBuffer = null;
	private LabTestData tempLabTestData = null;
	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private LabTestDataRepository labTestDataRepository;
	
	@PostConstruct
	private void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/LabDataSeePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/LabDataSeePage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootPane.equals(newValue)){
					fatherPane = oldValue;
					labDatas = new ArrayList<>();
					
					tempStringBuffer = new StringBuffer();
					GB_InfoVBox.setVisible(true);
					
					tImage = new Image(this.getClass().getResourceAsStream("/RES/star.png"));
					bImage = new Image(this.getClass().getResourceAsStream("/RES/sun.png"));
					cImage = new Image(this.getClass().getResourceAsStream("/RES/moon.png"));
					tValueLabel = new Text();
					bValueLabel = new Text();
					cValueLabel = new Text();
					dropShadow = new DropShadow();
					dropShadow.setColor(Color.RED);
					
					tempGroup = (Group) GB_Chart.lookup(".plot-content");
					tempGroup.getChildren().add(tValueLabel);
					tempGroup.getChildren().add(bValueLabel);
					tempGroup.getChildren().add(cValueLabel);
					showLines();
				}
				else if(rootPane.equals(oldValue)){
					dropShadow = null;
					
					labDataIds.clear();
					labDataIds = null;
					
					labDatas.clear();
					labDatas = null;
					
					tempStringBuffer.setLength(0);
					tempStringBuffer = null;
					
					tempGroup.getChildren().remove(tValueLabel);
					tempGroup.getChildren().remove(bValueLabel);
					tempGroup.getChildren().remove(cValueLabel);
					tValueLabel = null;
					bValueLabel = null;
					cValueLabel = null;
						
					tImage = null;
					bImage = null;
					cImage = null;
						
					GB_Chart.getData().clear();
					GB_InfoVBox.getChildren().clear();
				}
			}
		});
        
        GB_Chart.setOnMouseClicked((e)->{
        	int i = 0;
        	tempValue = GB_Chart.getData().size();
        	
        	MouseButton button = e.getButton();
    		if(button.equals(MouseButton.SECONDARY)){
    			selectSeries = -1;
    			for (i=0; i<tempValue;i++) {
    				GB_CurrentLabel = (Label) GB_InfoVBox.getChildren().get(i);
    				GB_CurrentLabel.setEffect(null);
    				 ShowSeries(GB_Chart.getData().get(i));
    			}
    			
    			tValueLabel.setText(null);
    			bValueLabel.setText(null);
    			cValueLabel.setText(null);
    		}
    		else if(button.equals(MouseButton.MIDDLE)){
    			selectSeries++;
    			if(selectSeries >= tempValue)
    				selectSeries = 0;
    			for(i=0; i< tempValue; i++){
    				GB_CurrentLabel = (Label) GB_InfoVBox.getChildren().get(i);
    				if(i != selectSeries){
    					HideSeries(GB_Chart.getData().get(i));
    					GB_CurrentLabel.setEffect(null);
    				}
    				else{
    					ShowSeries(GB_Chart.getData().get(i));
    					GB_CurrentLabel.setEffect(dropShadow);
    				}
    			}
    			
    			tempSeries = GB_Chart.getData().get(selectSeries);
    			tempLabTestData = labDatas.get(selectSeries);
    			
    			if(tempLabTestData.getT_l() != null){
	    			tempPoint = tempSeries.getData().get(tempLabTestData.getT_l());
	    			tempPointNode = (StackPane) tempPoint.getNode();
	    			tValueLabel.setText(tempPoint.getYValue().toString());
	    			tValueLabel.setY(tempPointNode.getLayoutY() - 10);
	    			tValueLabel.setX(tempPointNode.getLayoutX() - 10);
    			}
    			else
    				tValueLabel.setText(null);
    			
    			if(tempLabTestData.getB_l() != null){
    				tempPoint = tempSeries.getData().get(tempLabTestData.getB_l());
    				tempPointNode = (StackPane) tempPoint.getNode();
    				bValueLabel.setText(tempPoint.getYValue().toString());
    				bValueLabel.setY(tempPointNode.getLayoutY() - 10);
    				bValueLabel.setX(tempPointNode.getLayoutX() - 10);
    			}
    			else
    				bValueLabel.setText(null);
    			
    			if(tempLabTestData.getC_l() != null){
	    			tempPoint = tempSeries.getData().get(tempLabTestData.getC_l());
	    			tempPointNode = (StackPane) tempPoint.getNode();
	    			cValueLabel.setText(tempPoint.getYValue().toString());
	    			cValueLabel.setY(tempPointNode.getLayoutY() - 10);
	    			cValueLabel.setX(tempPointNode.getLayoutX() - 10);
    			}
    			else
    				cValueLabel.setText(null);
    		}
        });
        
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}
	
	public void showLabDataPane(List<Integer> ids) {
		labDataIds = new ArrayList<>(ids);
		workPageSession.setWorkPane(rootPane);
	}
	
	private void showLines() {
		labDatas = labTestDataRepository.findAll(labDataIds);
		GB_Chart.getData().clear();
		GB_InfoVBox.getChildren().clear();
		Path path = null;
		
		
		for (LabTestData labTestData : labDatas) {
			jsonArray = (JSONArray) JSONSerializer.toJSON(labTestData.getSerie());
			tempData = (List<Integer>) JSONSerializer.toJava(jsonArray);
			tempValue = tempData.size();
			
			tempSeries = new Series<>();
			tempSeries.setName(labTestData.getTindex());
			for (int i = 0; i < tempValue; i++) {
				tempPoint = new Data<Number, Number>(i, tempData.get(i));
				if(labTestData.getT_l() != null && i == labTestData.getT_l()){
					tempPointNode = new StackPane(new ImageView(tImage));
					tempPointNode.setStyle("-fx-stroke-width:0;");
					tempPoint.setNode(tempPointNode);
				}
				else if(labTestData.getC_l() != null && i == labTestData.getC_l()){
					tempPointNode = new StackPane(new ImageView(cImage));
					tempPointNode.setStyle("-fx-stroke-width:0;");
					tempPoint.setNode(tempPointNode);
				}
				else if(labTestData.getB_l() != null && i == labTestData.getB_l()){
					tempPointNode = new StackPane(new ImageView(bImage));
					tempPointNode.setStyle("-fx-stroke-width:0;");
					tempPoint.setNode(tempPointNode);
				}
				tempSeries.getData().add(tempPoint);
			}
			
			GB_Chart.getData().add(tempSeries);
			path = (Path) tempSeries.getNode();
			
			tempStringBuffer.setLength(0);
			tempStringBuffer.append(tempSeries.getName());
			tempStringBuffer.append("-----");
			tempStringBuffer.append(String.valueOf(labTestData.getT_c()));
			tempStringBuffer.append("-----");
			tempStringBuffer.append(labTestData.getDsc());
			tempLabel = new Label(tempStringBuffer.toString());
			tempLabel.textFillProperty().bind(path.strokeProperty());
			
			GB_InfoVBox.getChildren().add(tempLabel);
		}
	}
	
	private void HideSeries(Series<Number, Number> series){
		Path aa = (Path) series.getNode();//charts.lookup(".chart-series-line.series"+index);
		if(aa.isVisible()){
			aa.setVisible(false);
			 
			for (int i=0; i<series.getData().size(); i++ ) {
				 StackPane bb = (StackPane) series.getData().get(i).getNode();
				 bb.setVisible(false);
			}
		}
	}
	
	private void ShowSeries(Series<Number, Number> series){
		Path aa = (Path) series.getNode();//charts.lookup(".chart-series-line.series"+index);
		if(!aa.isVisible()){
			aa.setVisible(true);
			 
			 for (int i=0; i<series.getData().size(); i++ ) {
				 StackPane bb = (StackPane) series.getData().get(i).getNode();
				 bb.setVisible(true);
			}
		}
	}
	
	@FXML
	public void GB_ReturnAction(){
		workPageSession.setWorkPane(fatherPane);
	}
}

package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.xsx.ncd.entity.TestData;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

public class ReportTipPaneHandler extends AnchorPane{
	

	public ReportTipPaneHandler(TestData testData) {

		this.UI_Init(testData);
	}

	private void UI_Init(TestData testData) {
		
		NumberAxis xAxis = new NumberAxis();
    	NumberAxis yAxis = new NumberAxis();
    	LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
    	chart.setStyle("-fx-legend-visible:false");
    	
    	Series<Number, Number> series = new Series<>();

        chart.getData().add(series);
        
        JSONArray jsonArray = null;
        List<Integer> seriesdata = new ArrayList<>();

        for(int i=0; i<3; i++){
        	  	
        	if(i == 0)
        		jsonArray = (JSONArray) JSONSerializer.toJSON(testData.getSerie_a());
        	else if(i == 1)
        		jsonArray = (JSONArray) JSONSerializer.toJSON(testData.getSerie_b());
        	else if(i == 2)
        		jsonArray = (JSONArray) JSONSerializer.toJSON(testData.getSerie_c());
        	
        	seriesdata.addAll((List<Integer>) JSONSerializer.toJava(jsonArray));
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
        
        this.getChildren().add(chart);
	}
}

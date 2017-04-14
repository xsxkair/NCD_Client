package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.define.SoftInfo;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class AboutUsHandler implements HandlerTemplet{

	private AnchorPane root = null;
	
	@FXML Label GB_SoftInfoLabel;
	private StringBuffer tempStr = null;
	
	@Autowired private WorkPageSession workPageSession;
	
	@PostConstruct
	@Override
	public void UI_Init(){
        FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/AboutUsStage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/AboutUsStage.fxml");
        loader.setController(this);
        try {
        	root = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        tempStr = new StringBuffer(SoftInfo.SoftTitle);
	    tempStr.append(" -- ");
	    tempStr.append(String.format("V %d.%d.%02d", SoftInfo.softVersion/1000, SoftInfo.softVersion%1000/100, 
	    		SoftInfo.softVersion%100));
	    
        GB_SoftInfoLabel.setText(tempStr.toString());
        
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        
        loader = null;
        in = null;
	}

	@Override
	public void showPane() {
		workPageSession.setWorkPane(root);
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

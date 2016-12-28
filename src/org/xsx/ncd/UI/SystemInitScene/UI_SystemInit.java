package org.xsx.ncd.UI.SystemInitScene;

import java.io.IOException;
import java.io.InputStream;

import org.xsx.ncd.Data.UIScence;
import org.xsx.ncd.UI.LoginScene.LoginScene;
import org.xsx.ncd.servicetask.SystemInitService;

import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;

public class UI_SystemInit {
	
	private static UI_SystemInit GB_UI_SystemInit = null;
	
	private Scene S_Scene = null;
	
	@FXML
	Label SystemInitLabel;
	
	@FXML
	ProgressBar SystemInitProgress;
	
	@FXML
	Label SystemInitProgressValue;

	
	private UI_SystemInit(){
		
	}
	
	public static UI_SystemInit GetInstance(){
		if(GB_UI_SystemInit == null)
			GB_UI_SystemInit = new UI_SystemInit();
		
		return GB_UI_SystemInit;
	}
	
	private void UI_Init() {
		Region root = null;
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("UI_SystemInit.fxml"));
        InputStream in = this.getClass().getResourceAsStream("UI_SystemInit.fxml");
        loader.setController(this);
        try {
        	root = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        S_Scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight()); 
        
        SystemInitLabel.textProperty().bind(SystemInitService.GetInstance().messageProperty());
		SystemInitProgress.progressProperty().bind(SystemInitService.GetInstance().progressProperty());
		SystemInitProgressValue.textProperty().bind(new StringBinding() {
			{
				bind(SystemInitProgress.progressProperty());
			}
			@Override
			protected String computeValue() {
				// TODO Auto-generated method stub

				int value = (int) (SystemInitProgress.progressProperty().get()*100);

				return new String(value+"%");
			}
		});
		
		SystemInitService.GetInstance().valueProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(newValue)
					UIScence.GetInstance().setGB_Scene(LoginScene.GetInstance().getS_Scene());
			}
		});
	}

	public Scene getScene() {
		this.UI_Init();
		SystemInitService.GetInstance().restart();
		return S_Scene;
	}
}

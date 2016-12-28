package com.xsx.ncd.application;

import org.xsx.ncd.Data.UIScence;
import org.xsx.ncd.UI.SystemInitScene.UI_SystemInit;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AppStart extends Application{
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.FunctionInit(primaryStage);
		
		UIScence.GetInstance().setGB_Scene(UI_SystemInit.GetInstance().getScene());
		
		primaryStage.setTitle("Ó«¹â·ÖÎöÒÇ  V2.3.0");

		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
		primaryStage.setResizable(true);
		primaryStage.show();
	}

	private void FunctionInit(Stage primaryStage) {
		UIScence.GetInstance().getGB_Scene().addListener(new ChangeListener<Scene>() {
			@Override
			public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
				// TODO Auto-generated method stub
				if(newValue != null){
					primaryStage.setScene(newValue);
					primaryStage.centerOnScreen();
				}
			}
		});
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				primaryStage.close();
				System.exit(0);
			}
		});
	}
}

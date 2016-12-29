package com.xsx.ncd.application;

import java.util.Optional;

import com.xsx.ncd.handlers.LoginHandler;
import com.xsx.ncd.handlers.WorkPaneHandler;
import com.xsx.ncd.spring.SpringFacktory;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class AppStart extends Application{
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		//this.FunctionInit(primaryStage);

		ImageView imageView = new ImageView(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				primaryStage.close();
				System.exit(0);
			}
		});

		primaryStage.setScene(new Scene(new AnchorPane(imageView)));
		primaryStage.setTitle("荧光分析仪  V2.3.0");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
		primaryStage.setResizable(true);
		primaryStage.show();
		
		LoadResourceTask loadResourceTask = new LoadResourceTask();
		new Thread(loadResourceTask).start();
		
		loadResourceTask.valueProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				
				if(newValue){
					//加载所有页面
					SpringFacktory.getCtx().getBean(LoginHandler.class).UI_Init();
					SpringFacktory.getCtx().getBean(WorkPaneHandler.class).UI_Init();
					
					SpringFacktory.getCtx().getBean(LoginHandler.class).startLoginActivity();
					primaryStage.close();
				}
				else{
					 Alert alert = new Alert(AlertType.ERROR);
					 alert.setTitle("错误");
					 alert.setContentText("系统加载失败！");

					 Optional<ButtonType> result = alert.showAndWait();
					 if (result.get() == ButtonType.OK){
						 System.out.println("xsx");
						 primaryStage.close();
							System.exit(0);
					 }
				}
			}
		});
	}
	
	class LoadResourceTask extends Task<Boolean>{

		@Override
		protected Boolean call() {
			// TODO Auto-generated method stub
			try {
    			SpringFacktory.SpringFacktoryInit();
    			return true;
    			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
	    		return false;
			}
    		
			
		}
	}
}

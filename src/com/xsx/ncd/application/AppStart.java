package com.xsx.ncd.application;

import java.util.Optional;

import com.jfoenix.controls.JFXSpinner;
import com.xsx.ncd.handlers.LoginHandler;
import com.xsx.ncd.spring.SpringFacktory;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
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

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				primaryStage.close();
				System.exit(0);
			}
		});
		
		//播放视频
		Media pick = new Media(this.getClass().getResource("/RES/纽康度-Welcome.mp4").toExternalForm());
        MediaPlayer player = new MediaPlayer(pick);
        
        player.play();
        
        //Add a mediaView, to display the media. Its necessary !
        //This mediaView is added to a Pane
        MediaView mediaView = new MediaView(player);
        mediaView.setFitWidth(500);
        
        //加载图标
        JFXSpinner spinner = new JFXSpinner();
        spinner.setPrefSize(8, 8);
        spinner.setVisible(false);
        spinner.setOpacity(0.5);
        AnchorPane.setBottomAnchor(spinner, 5.0);
        AnchorPane.setRightAnchor(spinner, 5.0);
        
        player.setOnEndOfMedia(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				spinner.setVisible(true);
			}
		});
        
		AnchorPane root = new AnchorPane(mediaView, spinner);
		root.setStyle("-fx-border-color:red");
		root.setCursor(Cursor.WAIT);

		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
		primaryStage.setScene(new Scene(root, 500, 281));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		LoadResourceTask loadResourceTask = new LoadResourceTask();
		new Thread(loadResourceTask).start();
		
		loadResourceTask.valueProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				
				if(newValue){
					SpringFacktory.getCtx().getBean(LoginHandler.class).startLoginActivity();
					primaryStage.close();
				}
				else{
					 Alert alert = new Alert(AlertType.ERROR);
					 alert.setTitle("错误");
					 alert.setContentText("系统加载失败！");

					 Optional<ButtonType> result = alert.showAndWait();
					 if (result.get() == ButtonType.OK){
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
				
				//初始化spring，创建bean
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

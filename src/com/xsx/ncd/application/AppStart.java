package com.xsx.ncd.application;

import java.util.Optional;

import com.xsx.ncd.handlers.LoginHandler;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

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
		
		Media pick = new Media(this.getClass().getResource("/RES/≈¶øµ∂»-Welcome.mp4").toExternalForm());
        MediaPlayer player = new MediaPlayer(pick);
        
        player.play();
       
        //Add a mediaView, to display the media. Its necessary !
        //This mediaView is added to a Pane
        MediaView mediaView = new MediaView(player);
        mediaView.setFitWidth(500);
        
		AnchorPane root = new AnchorPane(mediaView);
		root.setStyle("-fx-border-color:red");
		
		primaryStage.setScene(new Scene(root, 500, 281));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		LoadResourceTask loadResourceTask = new LoadResourceTask();
		new Thread(loadResourceTask).start();
		
		player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
				// TODO Auto-generated method stub
				System.out.println(player.getMedia().getDuration());
				System.out.println(player.getCurrentTime());
				
				if(loadResourceTask.getValue() != null && loadResourceTask.getValue().equals(true)){
					if(player.getMedia().getDuration().subtract(newValue).lessThan(new Duration(200))){
						SpringFacktory.getCtx().getBean(LoginHandler.class).startLoginActivity();
						primaryStage.close();
					}
				}
			}
        });
		
		loadResourceTask.valueProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				
				if(newValue){
					System.out.println(player.getMedia().getDuration());
					System.out.println(player.getCurrentTime());
					if(player.getMedia().getDuration().subtract(player.getCurrentTime()).lessThan(new Duration(200))){
						SpringFacktory.getCtx().getBean(LoginHandler.class).startLoginActivity();
						primaryStage.close();
					}
				}
				else{
					 Alert alert = new Alert(AlertType.ERROR);
					 alert.setTitle("¥ÌŒÛ");
					 alert.setContentText("œµÕ≥º”‘ÿ ß∞‹£°");

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

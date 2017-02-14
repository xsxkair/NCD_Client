package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class AboutUsHandler {

	private Stage s_Stage = null;
	private AnchorPane root = null;
	
	@PostConstruct
	private void UI_Init(){
		

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
	}
	
	public void ShowAbout(){
		
		if(s_Stage == null){
			s_Stage = new Stage();
			s_Stage.initModality(Modality.APPLICATION_MODAL);
		        
			s_Stage.setResizable(false);

			s_Stage.setScene(new Scene(root));
		        
			s_Stage.setOnCloseRequest(e->{
				s_Stage.hide();
			});
		}
   
		s_Stage.show();
	}
}

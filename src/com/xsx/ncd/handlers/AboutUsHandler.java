package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class AboutUsHandler implements HandlerTemplet{

	private Stage s_Stage = null;
	private AnchorPane root = null;
	
	@FXML ImageView logoImageView;
	private Image logoImage = null;
	
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
        
        logoImage = new Image(this.getClass().getResourceAsStream("/RES/logo.png"));
        
        logoImageView.setImage(logoImage);
        
        loader = null;
        in = null;
	}

	@Override
	public void showPane() {
		// TODO Auto-generated method stub
		if(s_Stage == null){
			s_Stage = new Stage();
			s_Stage.initModality(Modality.APPLICATION_MODAL);
			
			s_Stage.getIcons().add(logoImage);
			s_Stage.setResizable(false);

			s_Stage.setScene(new Scene(root));
			
			s_Stage.setOnCloseRequest(e->{
				s_Stage.hide();
			});
		}
   
		s_Stage.show();
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

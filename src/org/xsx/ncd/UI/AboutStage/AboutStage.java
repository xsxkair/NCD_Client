package org.xsx.ncd.UI.AboutStage;

import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutStage {
	
	private static AboutStage S_AboutStage = null;
	
	private Stage s_Stage;
	
	private AboutStage(){
		
	}
	
	public static AboutStage GetInstance(){
		if(S_AboutStage == null){
			S_AboutStage = new AboutStage();
			S_AboutStage.UI_Init();
		}
		
		return S_AboutStage;
	}
	
	private void UI_Init(){
		AnchorPane root = null;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("AboutStage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("AboutStage.fxml");
        loader.setController(this);
        try {
        	root = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        s_Stage = new Stage();
        s_Stage.initModality(Modality.APPLICATION_MODAL);
        
        s_Stage.setResizable(false);
        s_Stage.setScene(new Scene(root));
	}
	
	public void ShowAbout(){
		s_Stage.showAndWait();
	}
}

package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.repository.ManagerRepository;
import com.xsx.ncd.spring.ManagerSession;
import com.xsx.ncd.spring.SpringFacktory;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Component
public class LoginHandler {

	private Stage s_Stage;
	
	private Scene s_Scene;
	
	@FXML JFXTextField UserNameText;
	
	@FXML JFXPasswordField UserPasswordText;
	
	@FXML JFXButton LoginButton;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ManagerSession managerSession;
	@Autowired
	private MainContainHandler mainContainHandler;
	
	@PostConstruct
	public void UI_Init() {
		AnchorPane root = null;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/LoginFXML.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/LoginFXML.fxml");
        loader.setController(this);
        
        try {
        	root = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		LoginButton.disableProperty().bind(new BooleanBinding() {
			{
				bind(UserNameText.textProperty());
				bind(UserPasswordText.textProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				
				if(UserNameText.getText() != null && UserNameText.getText().length() > 0 &&
						UserPasswordText.getText() != null && UserPasswordText.getText().length() >0)
					return false;
				else
					return true;
			}
		});
		
		UserPasswordText.focusedProperty().addListener((o, oldVal, newVal) -> {

			if (!newVal) UserPasswordText.validate();
		});
		
		UserNameText.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) UserNameText.validate();
		});
		
		s_Scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
		s_Scene.getStylesheets().add(this.getClass().getResource("/com/xsx/ncd/views/login.css").toExternalForm());
	}
	
	public void startLoginActivity() {
		s_Stage = new Stage();
		 
		s_Stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
			@Override
			public void handle(WindowEvent event) {
					// TODO Auto-generated method stub
				s_Stage.close();
				System.exit(0);
			}
		});
		
		
		UserNameText.setText(null);
		UserPasswordText.setText(null);
		
		s_Stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
	    s_Stage.initModality(Modality.APPLICATION_MODAL);
	        
	    s_Stage.setResizable(false);
	    s_Stage.setScene(s_Scene);
	     
		s_Stage.show();
	}
	
	@FXML
	public void LoginAction(ActionEvent e){
		
		Manager tempuser = managerRepository.findManagerByAccountAndPassword(UserNameText.getText(), UserPasswordText.getText());

		if(tempuser != null){
			
			s_Stage.close();
			
			managerSession.setAccount(tempuser.getAccount());
			
			mainContainHandler.startWorkActivity();

		}
		else {
			ButtonType loginButtonType = new ButtonType("È·¶¨", ButtonData.OK_DONE);
			Dialog<String> dialog = new Dialog<>();
			dialog.getDialogPane().setContentText("µÇÂ½Ê§°Ü£¡");
			dialog.getDialogPane().getButtonTypes().add(loginButtonType);
			dialog.showAndWait();
		}
	}
	
}

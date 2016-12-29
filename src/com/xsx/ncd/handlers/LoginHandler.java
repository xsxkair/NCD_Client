package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.repository.ManagerRepository;
import com.xsx.ncd.spring.ManagerSession;
import com.xsx.ncd.spring.SpringFacktory;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Component
public class LoginHandler {

	private Stage s_Stage;
	
	@FXML
	TextField UserNameText;
	
	@FXML
	PasswordField UserPasswordText;
	
	@FXML
	Button LoginButton;
	
	@FXML
	HBox LoginHBox;
	
	@FXML
	StackPane LoginStackPane;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ManagerSession managerSession;

	public Boolean UI_Init() {
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
			System.out.println("error");
			return false;
		}

		LoginButton.disableProperty().bind(new BooleanBinding() {
			{
				bind(UserNameText.textProperty());
				bind(UserPasswordText.textProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				if((UserNameText.getText() != null)&&(UserNameText.getText().length() > 0)
					&&(UserPasswordText.getText() != null)&&(UserPasswordText.getText().length() > 0))
					return false;
				else
					return true;
			}
		});
		
		 s_Stage = new Stage();
		 
		 s_Stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent event) {
					// TODO Auto-generated method stub
					s_Stage.close();
					System.exit(0);
				}
			});
		 
	     s_Stage.initModality(Modality.APPLICATION_MODAL);
	        
	     s_Stage.setResizable(false);
	     s_Stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
	     
	     return true;
	}
	
	public void startLoginActivity() {
		s_Stage.show();
	}
	
	@FXML
	public void LoginAction(ActionEvent e){
		
		Manager tempuser = managerRepository.findManagerByAccountAndPassword(UserNameText.getText(), UserPasswordText.getText());

		if(tempuser != null){
			managerSession.setAccount(tempuser.getAccount());
			
			SpringFacktory.getCtx().getBean(WorkPaneHandler.class).startWorkActivity();
			
			s_Stage.close();
		}
		else {
			ButtonType loginButtonType = new ButtonType("È·¶¨", ButtonData.OK_DONE);
			Dialog<String> dialog = new Dialog<>();
			dialog.getDialogPane().setContentText("µÇÂ½Ê§°Ü£¡");
			dialog.getDialogPane().getButtonTypes().add(loginButtonType);
			dialog.showAndWait();
		}
	}
	
	@FXML
	public void GB_LoginKeyEvent(KeyEvent e){
		if(e.getCode() == KeyCode.ENTER){
			
			if((UserNameText.getText().length() > 0)&&(UserPasswordText.getText().length() > 0)){

				Manager tempuser = managerRepository.findManagerByAccountAndPassword(UserNameText.getText(), UserPasswordText.getText());
				
				if(tempuser != null){
					managerSession.setAccount(tempuser.getAccount());
					
					SpringFacktory.getCtx().getBean(WorkPaneHandler.class).startWorkActivity();
					
					s_Stage.close();
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
	}
	
}

package com.xsx.ncd.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;

@Component
public class MyInfoHandler implements HandlerTemplet{

	private AnchorPane rootpane;
	
	@FXML StackPane rootStackPane;
	
	@FXML Button GB_ModifyUserPassWordButton;
	@FXML Button GB_SaveUserInfoButton;
	@FXML TextField GB_UserNameTextField;
	@FXML TextField GB_UserAgeTextField;
	@FXML TextField GB_UserSexTextField;
	@FXML TextField GB_UserPhoneTextField;
	@FXML TextField GB_UserJobTextField;
	@FXML TextField GB_UserDescTextField;

	@FXML ImageView GB_ModifyImageView;
	@FXML ImageView GB_CancelModifyImageView;
	
	@FXML JFXDialog modifyUserInfoDialog;
	@FXML PasswordField userPasswordTextField;
	@FXML JFXButton acceptButton0;
	@FXML JFXButton cancelButton0;
	
	@FXML JFXDialog modifyUserPasswordDialog;
	@FXML PasswordField userNewPasswordTextField0;
	@FXML PasswordField userNewPasswordTextField1;
	@FXML PasswordField userOldPasswordTextField;
	@FXML JFXButton acceptButton1;
	@FXML JFXButton cancelButton1;
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton acceptButton2;
	
	private User itsMe = null;

	ContextMenu myContextMenu;
	MenuItem RefreshMenuItem;
	
	@Autowired private UserRepository userRepository;
	@Autowired private UserSession userSession;
	@Autowired private WorkPageSession workPageSession;
	
	@PostConstruct
	@Override
	public void UI_Init(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/MyInfoPagefxml.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/MyInfoPagefxml.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
        	SVGGlyphLoader.clear();

			SVGGlyphLoader.loadGlyphsFont(this.getClass().getResourceAsStream("/RES/icomoon.svg"), "icomoon.svg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootStackPane.getChildren().remove(modifyUserInfoDialog);
        rootStackPane.getChildren().remove(modifyUserPasswordDialog);
        rootStackPane.getChildren().remove(LogDialog);
        
        GB_ModifyImageView.setOnMouseClicked((e)->{
        	setEditable(true);
        });
        
        GB_CancelModifyImageView.setOnMouseClicked((e)->{
        	setEditable(false);
        });
        
        GB_SaveUserInfoButton.setOnAction((e)->{
        	userPasswordTextField.clear();
    		modifyUserInfoDialog.setTransitionType(DialogTransition.CENTER);
    		modifyUserInfoDialog.show(rootStackPane);
        });
        //确认修改个人信息
        acceptButton0.disableProperty().bind(new BooleanBinding() {
        	{
        		bind(userPasswordTextField.lengthProperty());
        	}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				if(userPasswordTextField.getLength() > 5)
					return false;
				else
					return true;
			}
		});
        acceptButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();

			if(userPasswordTextField.getText().equals(itsMe.getPassword())){
				itsMe.setName(GB_UserNameTextField.getText());
				itsMe.setAge(GB_UserAgeTextField.getText());
				itsMe.setSex(GB_UserSexTextField.getText());
				itsMe.setPhone(GB_UserPhoneTextField.getText());
				itsMe.setJob(GB_UserJobTextField.getText());
				itsMe.setDsc(GB_UserDescTextField.getText());
				
				userRepository.save(itsMe);
				setEditable(false);
			}
			else
				showLogsDialog("错误", "密码错误，禁止修改！");
		});
        
        //取消修改个人信息
        cancelButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();
		});
        
        GB_ModifyUserPassWordButton.setOnAction((e)->{
        	userNewPasswordTextField0.clear();
    		userNewPasswordTextField1.clear();
    		userOldPasswordTextField.clear();
    		modifyUserPasswordDialog.setTransitionType(DialogTransition.CENTER);
    		modifyUserPasswordDialog.show(rootStackPane);
        });
        //确认修改密码
        acceptButton1.disableProperty().bind(new BooleanBinding() {
        	{
        		bind(userOldPasswordTextField.lengthProperty());
        		bind(userNewPasswordTextField0.lengthProperty());
        		bind(userNewPasswordTextField1.lengthProperty());
        	}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				if((userOldPasswordTextField.getLength() > 5) && (userNewPasswordTextField0.getLength() > 5)
					&& (userNewPasswordTextField1.getLength() > 5))
					return false;
				else
					return true;
			}
		});
        acceptButton1.setOnMouseClicked((e)->{
        	modifyUserPasswordDialog.close();
			
			if(!userOldPasswordTextField.getText().equals(itsMe.getPassword()))
				showLogsDialog("错误", "密码错误，禁止修改！");
			else if (!userNewPasswordTextField0.getText().equals(userNewPasswordTextField1.getText())) {
				showLogsDialog("错误", "新密码必须一致！");
			}
			else {
				itsMe.setPassword(userNewPasswordTextField0.getText());
				
				userRepository.save(itsMe);
			}
		});
        
        //取消修改密码
        cancelButton1.setOnMouseClicked((e)->{
        	modifyUserPasswordDialog.close();
		});
        
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        RefreshMenuItem = new MenuItem("刷新");
        RefreshMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        	
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				upUserInfo();
			}
		});
        myContextMenu = new ContextMenu(RefreshMenuItem);
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue)->{
        	if(rootpane.equals(newValue)){
        		itsMe = userRepository.findByAccount(userSession.getAccount());
        		setEditable(false);
        		upUserInfo();
        	}
        	else if(rootpane.equals(oldValue)){
        		itsMe = null;
        	}
        });
        
        rootpane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if(event.getButton().equals(MouseButton.SECONDARY)){
						myContextMenu.show(rootpane, event.getScreenX(), event.getScreenY());
				}
			}
		});
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
        
        loader = null;
        in = null;
	}
	
	@Override
	public void showPane(){
		workPageSession.setWorkPane(rootpane);
	}
	
	private void setEditable(boolean editable) {
		GB_SaveUserInfoButton.setVisible(editable);
		GB_UserNameTextField.setEditable(editable);
		GB_UserAgeTextField.setEditable(editable);
		GB_UserSexTextField.setEditable(editable);
		GB_UserPhoneTextField.setEditable(editable);
		GB_UserJobTextField.setEditable(editable);
		GB_UserDescTextField.setEditable(editable);
		
		if(editable){
			GB_ModifyImageView.setVisible(false);
			GB_CancelModifyImageView.setVisible(true);
		}
		else{
			upUserInfo();
			GB_ModifyImageView.setVisible(true);
			GB_CancelModifyImageView.setVisible(false);
		}
	}
	
	private void upUserInfo() {
		GB_UserNameTextField.setText(itsMe.getName());
		GB_UserAgeTextField.setText(itsMe.getAge());
		GB_UserSexTextField.setText(itsMe.getSex());
		GB_UserPhoneTextField.setText(itsMe.getPhone());
		GB_UserJobTextField.setText(itsMe.getJob());
		GB_UserDescTextField.setText(itsMe.getDsc());
	}
	
	private void showLogsDialog(String head, String logs) {
		LogDialogHead.setText(head);
		LogDialogContent.setText(logs);
		LogDialog.show(rootStackPane);
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

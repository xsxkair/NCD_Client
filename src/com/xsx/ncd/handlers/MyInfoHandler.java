package com.xsx.ncd.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXDialog.DialogTransition;
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
public class MyInfoHandler {

	private AnchorPane rootpane;
	
	@FXML StackPane rootStackPane;
	
	@FXML StackPane modifyToggleNode;
	private SVGGlyph svgGlyph;
	@FXML VBox infoEditBox;
	@FXML JFXTextField userNameTextField;
	@FXML JFXTextField userSexTextField;
	@FXML JFXTextField userAgeTextField;
	@FXML JFXTextField userPhoneTextField;
	@FXML JFXTextField userJobTextField;
	@FXML JFXTextField userDescTextField;
	@FXML PasswordField userPasswordTextField;
	@FXML HBox modifyButtonBox;
	@FXML JFXButton modifyUserPasswordButton;
	@FXML JFXButton modifyUserInfodButton;
	@FXML JFXButton acceptButton0;
	@FXML JFXButton cancelButton0;
	@FXML JFXDialog modifyUserInfoDialog;
	@FXML JFXDialog modifyUserPasswordDialog;
	@FXML PasswordField userNewPasswordTextField0;
	@FXML PasswordField userNewPasswordTextField1;
	@FXML PasswordField userOldPasswordTextField;
	@FXML JFXButton acceptButton1;
	@FXML JFXButton cancelButton1;
	@FXML JFXDialog modifyLogDialog;
	@FXML Label dialogInfo;
	@FXML Label modifyUserPasswordLog;
	@FXML JFXButton acceptButton2;
	
	ContextMenu myContextMenu;
	MenuItem RefreshMenuItem;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserSession userSession;
	
	@Autowired
	private WorkPageSession workPageSession;
	
	@PostConstruct
	public void UI_Init(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/MyInfoPagefxml.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/MyInfoPagefxml.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
        	SVGGlyphLoader.clear();
        	File file = new File(this.getClass().getResource("/RES/icomoon.svg").getFile());
			SVGGlyphLoader.loadGlyphsFont(new FileInputStream(file),file.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootStackPane.getChildren().remove(modifyUserInfoDialog);
        rootStackPane.getChildren().remove(modifyUserPasswordDialog);
        rootStackPane.getChildren().remove(modifyLogDialog);

        svgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.pencil2");
        svgGlyph.setPrefSize(16, 16);
        svgGlyph.setFill(Color.GREY);
        
        modifyToggleNode.getChildren().setAll(svgGlyph);

        modifyToggleNode.setOnMouseClicked(e->{
        	if(svgGlyph.getFill().equals(Color.GREY))
        		setEditable(true);
        	else
        		setEditable(false);
        });
        
        //确认修改个人信息
        acceptButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();
			
			User user = userRepository.findByAccount(userSession.getAccount());
			
			if(user.getPassword().equals(userPasswordTextField.getText())){
				user.setName(userNameTextField.getText());
				user.setAge(userAgeTextField.getText());
				user.setSex(userSexTextField.getText());
				user.setPhone(userPhoneTextField.getText());
				user.setJob(userJobTextField.getText());
				user.setDsc(userDescTextField.getText());
			
				userRepository.save(user);
				setEditable(false);
			}
		});
        
        //取消修改个人信息
        cancelButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();
		});
        
        //确认修改密码
        acceptButton1.setOnMouseClicked((e)->{
        	modifyUserPasswordDialog.close();
			
			User user = userRepository.findByAccount(userSession.getAccount());
			
			if(!user.getPassword().equals(userOldPasswordTextField.getText()))
				showModifyLogsDialog("原始密码错误！");
			else if ((userNewPasswordTextField0.getText() == null) || (userNewPasswordTextField1.getText() == null)) {
				showModifyLogsDialog("新密码为空！");
			}
			else if (!userNewPasswordTextField0.getText().equals(userNewPasswordTextField1.getText())) {
				showModifyLogsDialog("新密码不一致！");
			}
			else if (userNewPasswordTextField0.getText().length() < 6) {
				showModifyLogsDialog("新密码长度小于6位！");
			}
			else {
				user.setName(userNameTextField.getText());
				user.setAge(userAgeTextField.getText());
				user.setSex(userSexTextField.getText());
				user.setPhone(userPhoneTextField.getText());
				user.setJob(userJobTextField.getText());
				user.setDsc(userDescTextField.getText());
			
				userRepository.save(user);
				setEditable(false);
			}
		});
        
        userNewPasswordTextField1.textProperty().addListener((o, oldValue, newValue)->{
        	if (newValue.equals(null)) {
        		modifyUserPasswordLog.setTextFill(Color.RED);
        		modifyUserPasswordLog.setText("新密码不能为空！");
			}
        	else if (newValue.length() < 6) {
        		modifyUserPasswordLog.setTextFill(Color.RED);
        		modifyUserPasswordLog.setText("密码长度至少为6位！");
			}
        	else if (!newValue.equals(userNewPasswordTextField0.getText())) {
        		modifyUserPasswordLog.setTextFill(Color.RED);
        		modifyUserPasswordLog.setText("密码不一致！");
			}
        	else {
        		modifyUserPasswordLog.setText(" ");
			}
        });
        
        //取消修改密码
        cancelButton1.setOnMouseClicked((e)->{
        	modifyUserPasswordDialog.close();
		});
        
        acceptButton2.setOnMouseClicked((e)->{
        	modifyLogDialog.close();
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
        	if((newValue != null) && (newValue.equals(rootpane))){
        		setEditable(false);
        		upUserInfo();
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
	}
	
	public void ShowMyInfoPage(){
		workPageSession.setWorkPane(rootpane);
	}
	
	private void setEditable(boolean editable) {
		for (Node node : infoEditBox.getChildren()) {
			JFXTextField textField = (JFXTextField) node;
			textField.setEditable(editable);
			if(editable){
				textField.setFocusColor(Color.web("#405aa8",1.0));
				textField.setUnFocusColor(Color.web("#4d4d4d",1.0));
			}
			else {
				textField.setFocusColor(Color.web("#405aa8",0));
				textField.setUnFocusColor(Color.web("#4d4d4d",0));
			}
		}
		modifyButtonBox.setVisible(editable);
		
		if(editable)
			svgGlyph.setFill(Color.DEEPSKYBLUE);
		else
			svgGlyph.setFill(Color.GREY);
	}
	
	private void upUserInfo() {
		User user = userRepository.findByAccount(userSession.getAccount());
		
		userNameTextField.setText(user.getName());
		userSexTextField.setText(user.getSex());
		userAgeTextField.setText(user.getAge());
		userPhoneTextField.setText(user.getPhone());
		userJobTextField.setText(user.getJob());
		userDescTextField.setText(user.getDsc());
	}
	

	@FXML
	public void modifyUserInfoAction(){

		userPasswordTextField.clear();
		modifyUserInfoDialog.setTransitionType(DialogTransition.CENTER);
		modifyUserInfoDialog.show(rootStackPane);
		
	}
	
	@FXML
	public void modifyUserPasswordAction(){

		userNewPasswordTextField0.clear();
		userNewPasswordTextField1.clear();
		userOldPasswordTextField.clear();
		modifyUserPasswordDialog.setTransitionType(DialogTransition.CENTER);
		modifyUserPasswordDialog.show(rootStackPane);	
	}
	
	private void showModifyLogsDialog(String logs) {
		dialogInfo.setText(logs);
		modifyLogDialog.show(rootStackPane);
	}
}

package com.xsx.ncd.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

@Component
public class AdministratorHandler {

	private AnchorPane rootpane;
	
	@FXML StackPane rootStackPane;
	@FXML StackPane addUserStackPane;
	@FXML StackPane deleteUserStackPane;
	@FXML ListView<User> managerrListView;
	
	@FXML JFXTextField userAccountTextField;
	@FXML JFXTextField userPasswordTextField;
	@FXML JFXTextField userNameTextField;
	@FXML JFXTextField userSexTextField;
	@FXML JFXTextField userAgeTextField;
	@FXML JFXTextField userPhoneTextField;
	@FXML JFXTextField userJobTextField;
	@FXML JFXTextField userDescTextField;
	@FXML JFXButton saveModifyUserButton;
	
	
	private SVGGlyph addSvgGlyph;
	private SVGGlyph deleteSvgGlyph;

	//添加用户
	@FXML JFXDialog addUserDialog;
	@FXML JFXTextField userAccountTextField1;
	@FXML JFXPasswordField userPasswordTextField1;
	@FXML JFXTextField userNameTextField1;
	@FXML JFXTextField userSexTextField1;
	@FXML JFXTextField userAgeTextField1;
	@FXML JFXTextField userPhoneTextField1;
	@FXML JFXTextField userJobTextField1;
	@FXML JFXTextField userDescTextField1;
	@FXML JFXPasswordField adminPasswordTextField;
	@FXML JFXButton acceptButton0;
	@FXML JFXButton cancelButton0;
	
	//修改用户
	@FXML JFXDialog editUserDialog;
	@FXML JFXPasswordField userPasswordTextField2;
	@FXML JFXTextField userNameTextField2;
	@FXML JFXTextField userSexTextField2;
	@FXML JFXTextField userAgeTextField2;
	@FXML JFXTextField userPhoneTextField2;
	@FXML JFXTextField userJobTextField2;
	@FXML JFXTextField userDescTextField2;
	@FXML JFXPasswordField adminPasswordTextField1;
	@FXML JFXButton acceptButton1;
	@FXML JFXButton cancelButton1;
	
	//告警信息
	@FXML JFXDialog logDialog;
	@FXML Label dialogInfo;
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
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/AdministratorPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/AdministratorPage.fxml");
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
        
        rootStackPane.getChildren().remove(addUserDialog);
        rootStackPane.getChildren().remove(editUserDialog);
        rootStackPane.getChildren().remove(logDialog);

        addSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.plus");
        addSvgGlyph.setPrefSize(16, 16);
        addSvgGlyph.setFill(Color.GREEN);   
        addUserStackPane.getChildren().setAll(addSvgGlyph);
        
        deleteSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.close, remove, times");
        deleteSvgGlyph.setPrefSize(16, 16);
        deleteSvgGlyph.setFill(Color.ORANGERED);
        deleteUserStackPane.getChildren().setAll(deleteSvgGlyph);
        
        managerrListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		upSelectUserInfo(newValue);

        		//修改信息文本
        		userPasswordTextField2.setText(newValue.getPassword());
        		userNameTextField2.setText(newValue.getName());
        		userSexTextField2.setText(newValue.getSex());
        		userAgeTextField2.setText(newValue.getAge());
        		userPhoneTextField2.setText(newValue.getPhone());
        		userJobTextField2.setText(newValue.getJob());
        		userDescTextField2.setText(newValue.getDsc());
        		adminPasswordTextField1.clear();
        	}
        	else {
        		//显示信息文本
        		userAccountTextField.clear();
        		userPasswordTextField.clear();
        		userNameTextField.clear();
        		userSexTextField.clear();
        		userAgeTextField.clear();
        		userPhoneTextField.clear();
        		userJobTextField.clear();
        		userDescTextField.clear();
        		
        		//添加用户文本
        		userAccountTextField1.clear();
        		userPasswordTextField1.clear();
        		userNameTextField1.clear();
        		userSexTextField1.clear();
        		userAgeTextField1.clear();
        		userPhoneTextField1.clear();
        		userJobTextField1.clear();
        		userDescTextField1.clear();
        		adminPasswordTextField.clear();
        		
        		//修改信息文本
        		userPasswordTextField2.clear();
        		userNameTextField2.clear();
        		userSexTextField2.clear();
        		userAgeTextField2.clear();
        		userPhoneTextField2.clear();
        		userJobTextField2.clear();
        		userDescTextField2.clear();
        		adminPasswordTextField1.clear();
			}
        });
        
       //添加审核人
        acceptButton0.setOnMouseClicked((e)->{
			addUserDialog.close();
			
			User admin = userRepository.findByAccount(userSession.getAccount());
			
			if(admin.getPassword().equals(adminPasswordTextField.getText())){
				
				if((userAccountTextField1.getText() == null) || (userPasswordTextField1.getText() == null)
						|| (userPasswordTextField1.getText().length() < 6))
					showModifyLogsDialog("格式错误！");
				else {
					User tempUser = new User();
					
					tempUser.setAccount(userAccountTextField1.getText());
					tempUser.setPassword(userPasswordTextField1.getText());
					tempUser.setName(userNameTextField1.getText());
					tempUser.setAge(userAgeTextField1.getText());
					tempUser.setSex(userSexTextField1.getText());
					tempUser.setPhone(userPhoneTextField1.getText());
					tempUser.setJob(userJobTextField1.getText());
					tempUser.setDsc(userDescTextField1.getText());
					tempUser.setFatheraccount(admin.getAccount());
					tempUser.setType(1);
					
					User user = userRepository.findByAccount(tempUser.getAccount());
					if(user != null)
						showModifyLogsDialog("账号已存在！");
					else
						userRepository.save(tempUser);
					}
			}
			else
				showModifyLogsDialog("管理员密码错误！");
			
			upUserList();
		});
        
        //取消修改个人信息
        cancelButton0.setOnMouseClicked((e)->{
        	addUserDialog.close();
		});
        
        //修改审核人
        acceptButton1.setOnMouseClicked((e)->{
			editUserDialog.close();
			
			User admin = userRepository.findByAccount(userSession.getAccount());
			
			if(admin.getPassword().equals(adminPasswordTextField1.getText())){
				
				if((userPasswordTextField2.getText() == null) || (userPasswordTextField2.getText().length() < 6))
					showModifyLogsDialog("格式错误！");
				else {
					User tempUser = managerrListView.getSelectionModel().getSelectedItem();
					if(tempUser != null){
						tempUser.setPassword(userPasswordTextField2.getText());
						tempUser.setName(userNameTextField2.getText());
						tempUser.setAge(userAgeTextField2.getText());
						tempUser.setSex(userSexTextField2.getText());
						tempUser.setPhone(userPhoneTextField2.getText());
						tempUser.setJob(userJobTextField2.getText());
						tempUser.setDsc(userDescTextField2.getText());
						
						userRepository.save(tempUser);
					}
					else{
						showModifyLogsDialog("错误！");
					}
				}
			}
			else
				showModifyLogsDialog("管理员密码错误！");
			
			upUserList();
		});
        
        //取消修改个人信息
        cancelButton1.setOnMouseClicked((e)->{
        	editUserDialog.close();
		});
        
        
        acceptButton2.setOnMouseClicked((e)->{
        	logDialog.close();
		});
        
        RefreshMenuItem = new MenuItem("刷新");
        RefreshMenuItem.setOnAction(e->{
        	upUserList();
        });
        
        myContextMenu = new ContextMenu(RefreshMenuItem);
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue)->{
        	if((newValue != null) && (newValue.equals(rootpane))){
        		upUserList();
        	}
        });
        
        rootpane.setOnMouseClicked(e->{
        	if(e.getButton().equals(MouseButton.SECONDARY)){
				myContextMenu.show(rootpane, e.getScreenX(), e.getScreenY());
        	}
        });
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public void ShowChileManagerPage(){
		workPageSession.setWorkPane(rootpane);
	}
	
	private void upUserList() {
		managerrListView.getItems().clear();
		
		managerrListView.getItems().addAll(userRepository.queryAllAdministrator());

		managerrListView.getSelectionModel().selectFirst();
	}
	
	private void upSelectUserInfo(User user) {
		userAccountTextField.setText(user.getAccount());
		userPasswordTextField.setText(user.getPassword());
		userNameTextField.setText(user.getName());
		userSexTextField.setText(user.getSex());
		userAgeTextField.setText(user.getAge());
		userPhoneTextField.setText(user.getPhone());
		userJobTextField.setText(user.getJob());
		userDescTextField.setText(user.getDsc());
	}
	
	private void showModifyLogsDialog(String logs) {
		dialogInfo.setText(logs);
		logDialog.show(rootStackPane);
	}
	
	@FXML
	public void AddUserAction(){		
		userAccountTextField1.clear();
		userPasswordTextField1.clear();
		userNameTextField1.clear();
		userSexTextField1.clear();
		userAgeTextField1.clear();
		userPhoneTextField1.clear();
		userJobTextField1.clear();
		userDescTextField1.clear();
		adminPasswordTextField.clear();
		
		addUserDialog.show(rootStackPane);
	}
	
	@FXML
	public void DeleteUserAction(){
		User user = managerrListView.getSelectionModel().getSelectedItem();
		if(user != null){
			userRepository.delete(user.getId());
			upUserList();
		}
	}
	
	@FXML
	public void saveModifyUserAction(){
		editUserDialog.show(rootStackPane);
	}
}

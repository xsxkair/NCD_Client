
package com.xsx.ncd.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.omg.PortableServer.IMPLICIT_ACTIVATION_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

@Component
public class ChildUserHandler implements EventHandler<MouseEvent>, HandlerTemplet{

	private AnchorPane rootpane;
	
	@FXML StackPane rootStackPane;
	
	//主管
	@FXML StackPane addUserStackPane;						//添加主管图标
	@FXML StackPane deleteUserStackPane;					//删除主管图标
	@FXML StackPane modifyUserStackPane;					//修改主管图标
	@FXML JFXListView<User> UserListView;						//主管列表
	
	@FXML Label userAccountLabel;
	@FXML Label userNameLabel;
	@FXML Label userAgeLabel;
	@FXML Label userSexLabel;
	@FXML Label userPhoneLabel;
	@FXML Label userJobLabel;
	@FXML Label userDescLabel;
	
	//添加用户
	@FXML JFXDialog UserDialog;
	@FXML Label UserDialogHeadLabel;
	@FXML VBox UserDialogVbox;
	@FXML HBox UserAccountHbox;
	@FXML JFXTextField userAccountTextField;
	@FXML JFXPasswordField userPasswordTextField;
	@FXML JFXTextField userNameTextField;
	@FXML JFXTextField userSexTextField;
	@FXML JFXTextField userAgeTextField;
	@FXML JFXTextField userPhoneTextField;
	@FXML JFXTextField userJobTextField;
	@FXML JFXTextField userDescTextField;
	@FXML JFXPasswordField adminPasswordTextField;
	@FXML JFXButton acceptButton;
	
	//告警信息
	@FXML JFXDialog logDialog;
	@FXML Label dialogInfo;
	
	ContextMenu myContextMenu;
	MenuItem RefreshMenuItem;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired private DeviceRepository deviceRepository;
	
	@Autowired
	private UserSession userSession;
	
	@Autowired
	private WorkPageSession workPageSession;
	
	@PostConstruct
	@Override
	public void UI_Init(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/ChildUserPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/ChildUserPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
        	SVGGlyphLoader.clear();
			SVGGlyphLoader.loadGlyphsFont(this.getClass().getResourceAsStream("/RES/icomoon.svg"), "icomoon.svg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootStackPane.getChildren().remove(UserDialog);
        rootStackPane.getChildren().remove(logDialog);

        SVGGlyph addUserSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.plus");
        addUserSvgGlyph.setPrefSize(12, 12);
        addUserSvgGlyph.setFill(Color.GREY);   
        addUserStackPane.getChildren().add(addUserSvgGlyph);
        addUserStackPane.setOnMouseEntered(this);
        addUserStackPane.setOnMouseExited(this);
        
        SVGGlyph deleteUserSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.close, remove, times");
        deleteUserSvgGlyph.setPrefSize(12, 12);
        deleteUserSvgGlyph.setFill(Color.GREY);
        deleteUserStackPane.getChildren().add(deleteUserSvgGlyph);
        deleteUserStackPane.setOnMouseEntered(this);
        deleteUserStackPane.setOnMouseExited(this);
        
        SVGGlyph modifyUserSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.pencil2");
        modifyUserSvgGlyph.setPrefSize(12, 12);
        modifyUserSvgGlyph.setFill(Color.GREY);
        modifyUserStackPane.getChildren().add(modifyUserSvgGlyph);
        modifyUserStackPane.setOnMouseEntered(this);
        modifyUserStackPane.setOnMouseExited(this);
        
        //主管
        UserListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		User user = userRepository.findByAccount(newValue.getAccount());

        		//显示自己信息
        		userAccountLabel.setText(user.getAccount());
        		userNameLabel.setText(user.getName());
        		userAgeLabel.setText(user.getAge());
        		userSexLabel.setText(user.getSex());
        		userPhoneLabel.setText(user.getPhone());
        		userJobLabel.setText(user.getJob());
        		userDescLabel.setText(user.getDsc());
        	}
        	else {
        		//清除自己信息
        		userAccountLabel.setText(null);
        		userNameLabel.setText(null);
        		userAgeLabel.setText(null);
        		userSexLabel.setText(null);
        		userPhoneLabel.setText(null);
        		userJobLabel.setText(null);
        		userDescLabel.setText(null);
			}
        });
        
        acceptButton.disableProperty().bind(new BooleanBinding() {
        	{
        		bind(userAccountTextField.lengthProperty());
        		bind(userPasswordTextField.lengthProperty());
        		bind(adminPasswordTextField.lengthProperty());
        	}

			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				System.out.println(userAccountTextField.getLength());
				System.out.println(userPasswordTextField.getLength());
				System.out.println(adminPasswordTextField.getLength());
				if((userAccountTextField.getLength() > 0) && (userPasswordTextField.getLength() >= 6) &&
						(adminPasswordTextField.getLength() >= 6))
					return false;
				else
					return true;
			}
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
        
        loader = null;
        in = null;
	}
	
	@Override
	public void showPane(){
		workPageSession.setWorkPane(rootpane);
	}
	
	private void upUserList() {
		List<User> myUserList = null;
		
		myUserList = userRepository.queryChildUserList(userSession.getAccount());
		
		UserListView.getItems().clear();
		UserListView.getItems().addAll(myUserList);
		UserListView.getSelectionModel().selectFirst();
	}
	
	private void showLogsDialog(String logs) {
		dialogInfo.setText(logs);
		logDialog.show(rootStackPane);
	}
	
	@FXML
	public void AddUserAction(){

		UserDialogHeadLabel.setText("添加人员");
		
		userAccountTextField.clear();
		userPasswordTextField.clear();
		userNameTextField.clear();
		userSexTextField.clear();
		userAgeTextField.clear();
		userPhoneTextField.clear();
		userJobTextField.clear();
		userDescTextField.clear();
		adminPasswordTextField.clear();
		
		if(!UserDialogVbox.getChildren().contains(UserAccountHbox))
			UserDialogVbox.getChildren().add(0, UserAccountHbox);
		
		UserDialog.setUserData(null);
		
		UserDialog.show(rootStackPane);
	}
	
	@FXML
	public void DeleteUserAction(){
		User user = UserListView.getSelectionModel().getSelectedItem();
		if(user != null){
			userRepository.delete(user.getId());
			upUserList();
		}
	}
	
	@FXML
	public void ModifyUserAction(){
		User user = UserListView.getSelectionModel().getSelectedItem();
		if(user != null){
			UserDialogHeadLabel.setText("修改人员信息");
			
			UserDialog.setUserData(user);
			
			userAccountTextField.setText(user.getAccount());
			userPasswordTextField.setText(user.getPassword());
			userNameTextField.setText(user.getName());
			userSexTextField.setText(user.getSex());
			userAgeTextField.setText(user.getAge());
			userPhoneTextField.setText(user.getPhone());
			userJobTextField.setText(user.getJob());
			userDescTextField.setText(user.getDsc());
			
			adminPasswordTextField.clear();
			
			if(UserDialogVbox.getChildren().contains(UserAccountHbox))
				UserDialogVbox.getChildren().remove(UserAccountHbox);
			
			UserDialog.show(rootStackPane);
		}
		else
			showLogsDialog("选择为空");
	}

	@FXML
	public void ConfirmAction(){
		User user = null;
		
		User admin = userRepository.findByAccount(userSession.getAccount());
		
		if(!admin.getPassword().equals(adminPasswordTextField.getText())){
			showLogsDialog("密码错误，无此权限！");
			return;
		}
		
		//添加
		if(UserDialog.getUserData() == null){
			user = new User();
		}
		//修改
		else{
			user = (User) UserDialog.getUserData();
		}
		
		user.setAccount(userAccountTextField.getText());
		user.setPassword(userPasswordTextField.getText());
		user.setName(userNameTextField.getText());
		user.setAge(userAgeTextField.getText());
		user.setSex(userSexTextField.getText());
		user.setPhone(userPhoneTextField.getText());
		user.setJob(userJobTextField.getText());
		user.setDsc(userDescTextField.getText());
		user.setFatheraccount(admin.getAccount());
		user.setType(admin.getType());
		
		UserDialog.close();
		
		userRepository.save(user);
		upUserList();
	}
	
	@FXML
	public void CancelAction(){
		
		if(UserDialog.isVisible())
			UserDialog.close();
		
		if(logDialog.isVisible())
			logDialog.close();
	}

	@Override
	public void handle(MouseEvent event) {
		// TODO Auto-generated method stub\
		StackPane stackPane = (StackPane) event.getSource();
		SVGGlyph sVGGlyph = (SVGGlyph) stackPane.getChildren().get(0);
		
		if(MouseEvent.MOUSE_ENTERED.equals(event.getEventType()))
			sVGGlyph.setFill(Color.AQUA
					);
		else
			sVGGlyph.setFill(Color.GREY);
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

package com.xsx.ncd.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

@Component
public class UserHandler {

	private AnchorPane rootpane;
	
	@FXML StackPane rootStackPane;
	
	@FXML Label UserTypeLabel;								//��ǰ�������û�����
	
	//����
	@FXML StackPane addUserStackPane;						//�������ͼ��
	@FXML StackPane deleteUserStackPane;					//ɾ������ͼ��
	@FXML StackPane modifyUserStackPane;					//�޸�����ͼ��
	@FXML ListView<User> UserListView;						//�����б�
	
	@FXML Label userAccountLabel;
	@FXML Label userNameLabel;
	@FXML Label userAgeLabel;
	@FXML Label userSexLabel;
	@FXML Label userPhoneLabel;
	@FXML Label userJobLabel;
	@FXML Label userDescLabel;
	
	//�ҵ���Ա
	@FXML StackPane addChildUserStackPane;						//�������ͼ��
	@FXML StackPane deleteChildUserStackPane;					//ɾ������ͼ��
	@FXML StackPane modifyChildUserStackPane;					//�޸�����ͼ��
	@FXML ListView<User> childUserListView;						//�����б�
	
	@FXML Label cUserAccountLabel;
	@FXML Label cUserNameLabel;
	@FXML Label cUserAgeLabel;
	@FXML Label cUserSexLabel;
	@FXML Label cUserPhoneLabel;
	@FXML Label cUserJobLabel;
	@FXML Label cUserDescLabel;	
	
	

	//����û�
	@FXML JFXDialog UserDialog;
	@FXML Label UserDialogHeadLabel;
	@FXML VBox UserDialogVbox;
	@FXML HBox UserAccountHbox;
	@FXML HBox UserFatherHbox;
	@FXML JFXTextField userAccountTextField;
	@FXML JFXPasswordField userPasswordTextField;
	@FXML JFXTextField userNameTextField;
	@FXML JFXTextField userSexTextField;
	@FXML JFXTextField userAgeTextField;
	@FXML JFXTextField userPhoneTextField;
	@FXML JFXTextField userJobTextField;
	@FXML JFXTextField userDescTextField;
	@FXML JFXComboBox<User> userFatherCom;
	@FXML JFXPasswordField adminPasswordTextField;
	@FXML JFXButton acceptButton;
	@FXML JFXButton cancelButton;
	
	//�澯��Ϣ
	@FXML JFXDialog logDialog;
	@FXML Label dialogInfo;
	
	private SVGGlyph addUserSvgGlyph;
	private SVGGlyph deleteUserSvgGlyph;
	private SVGGlyph modifyUserSvgGlyph;
	
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
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/UserPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/UserPage.fxml");
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
        
        rootStackPane.getChildren().remove(UserDialog);
        rootStackPane.getChildren().remove(logDialog);

        addUserSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.plus");
        addUserSvgGlyph.setPrefSize(16, 16);
        addUserSvgGlyph.setFill(Color.GREEN);   
        addUserStackPane.getChildren().add(addUserSvgGlyph);
        addChildUserStackPane.getChildren().add(addUserSvgGlyph);
        
        deleteUserSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.close, remove, times");
        deleteUserSvgGlyph.setPrefSize(16, 16);
        deleteUserSvgGlyph.setFill(Color.ORANGERED);
        deleteUserStackPane.getChildren().add(deleteUserSvgGlyph);
        deleteChildUserStackPane.getChildren().add(deleteUserSvgGlyph);
        
        modifyUserSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.pencil2");
        modifyUserSvgGlyph.setPrefSize(16, 16);
        modifyUserSvgGlyph.setFill(Color.BLUE);
        modifyUserStackPane.getChildren().add(modifyUserSvgGlyph);
        modifyChildUserStackPane.getChildren().add(modifyUserSvgGlyph);
        
        //����
        UserListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		User user = userRepository.findByAccount(newValue.getAccount());

        		//��ʾ�Լ���Ϣ
        		userAccountLabel.setText(user.getAccount());
        		userNameLabel.setText(user.getName());
        		userAgeLabel.setText(user.getAge());
        		userSexLabel.setText(user.getSex());
        		userPhoneLabel.setText(user.getPhone());
        		userJobLabel.setText(user.getJob());
        		userDescLabel.setText(user.getDsc());
        		
        		//��ȡ�ҵ�Ա��
        		List<User> myChildUserList = userRepository.queryChildUserList(newValue.getAccount());
        		childUserListView.getItems().clear();
        		childUserListView.getItems().addAll(myChildUserList);
        		childUserListView.getSelectionModel().selectFirst();
        	}
        	else {
        		//����Լ���Ϣ
        		userAccountLabel.setText(null);
        		userNameLabel.setText(null);
        		userAgeLabel.setText(null);
        		userSexLabel.setText(null);
        		userPhoneLabel.setText(null);
        		userJobLabel.setText(null);
        		userDescLabel.setText(null);
        		
        		childUserListView.getItems().clear();
        		childUserListView.getSelectionModel().selectFirst();
			}
        });
        
        //�ҵ�Ա��
        childUserListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		User user = userRepository.findByAccount(newValue.getAccount());

        		//��ʾ�Լ���Ϣ
        		cUserAccountLabel.setText(user.getAccount());
        		cUserNameLabel.setText(user.getName());
        		cUserAgeLabel.setText(user.getAge());
        		cUserSexLabel.setText(user.getSex());
        		cUserPhoneLabel.setText(user.getPhone());
        		cUserJobLabel.setText(user.getJob());
        		cUserDescLabel.setText(user.getDsc());
        	}
        	else {
        		//����Լ���Ϣ
        		cUserAccountLabel.setText(null);
        		cUserNameLabel.setText(null);
        		cUserAgeLabel.setText(null);
        		cUserSexLabel.setText(null);
        		cUserPhoneLabel.setText(null);
        		cUserJobLabel.setText(null);
        		cUserDescLabel.setText(null);
			}
        });

        UserTypeLabel.textProperty().addListener((o, oldValue, newValue)->{
        	upUserList();
        });
        
        RefreshMenuItem = new MenuItem("ˢ��");
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
	
	public void ShowUserPage(String whichUser){
		UserTypeLabel.setText(whichUser);
		workPageSession.setWorkPane(rootpane);
	}
	
	private void upUserList() {
		List<User> myUserList = null;
		
		if("����".equals(UserTypeLabel.getText()))
			myUserList = userRepository.queryAllFatherSaler();
		else if("�з�".equals(UserTypeLabel.getText()))
			myUserList = userRepository.queryAllFatherNcdLaber();
		else if("�����".equals(UserTypeLabel.getText()))
			myUserList = userRepository.queryAllFatherManager();
		
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

		UserDialogHeadLabel.setText(UserTypeLabel.getText()+"����");
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
		
		if(UserDialogVbox.getChildren().contains(UserFatherHbox))
			UserDialogVbox.getChildren().remove(UserFatherHbox);
		
		UserDialog.setUserData(null);
		
		UserDialog.show(rootStackPane);
	}
	
	@FXML
	public void DeleteUserAction(){
		User user = UserListView.getSelectionModel().getSelectedItem();
		if(user != null){
			List<User> users = userRepository.queryChildUserList(user.getAccount());
			if(users.isEmpty()){
				userRepository.delete(user.getId());
				upUserList();
			}
			else
				showLogsDialog("����ɾ�����˻��µ���Ա��");
		}
	}
	
	@FXML
	public void ModifyUserAction(){
		User user = UserListView.getSelectionModel().getSelectedItem();
		if(user != null){
			UserDialogHeadLabel.setText(UserTypeLabel.getText()+"����");
			
			UserDialog.setUserData(user);
			
			userAccountTextField.setText(user.getAccount());
			userPasswordTextField.setText(user.getPassword());
			userNameTextField.setText(user.getName());
			userSexTextField.setText(user.getSex());
			userAgeTextField.setText(user.getAge());
			userPhoneTextField.setText(user.getPassword());
			userJobTextField.setText(user.getJob());
			userDescTextField.setText(user.getDsc());
			
			adminPasswordTextField.clear();
			
			if(UserDialogVbox.getChildren().contains(UserAccountHbox))
				UserDialogVbox.getChildren().remove(UserAccountHbox);
			
			if(UserDialogVbox.getChildren().contains(UserFatherHbox))
				UserDialogVbox.getChildren().remove(UserFatherHbox);
			
			UserDialog.show(rootStackPane);
		}
		else
			showLogsDialog("ѡ��Ϊ��");
	}
	
	@FXML
	public void AddChildUserAction(){

		UserDialogHeadLabel.setText(UserTypeLabel.getText()+"��Ա");
		userAccountTextField.clear();
		userPasswordTextField.clear();
		userNameTextField.clear();
		userSexTextField.clear();
		userAgeTextField.clear();
		userPhoneTextField.clear();
		userJobTextField.clear();
		userDescTextField.clear();
		userFatherCom.getItems().clear();
		adminPasswordTextField.clear();
		
		if(!UserDialogVbox.getChildren().contains(UserAccountHbox))
			UserDialogVbox.getChildren().add(0, UserAccountHbox);
		
		if(!UserDialogVbox.getChildren().contains(UserFatherHbox)){
			UserDialogVbox.getChildren().add(UserDialogVbox.getChildren().size(), UserFatherHbox);	
		}
		userFatherCom.getItems().addAll(UserListView.getItems());
		userFatherCom.getSelectionModel().select(UserListView.getSelectionModel().getSelectedItem());
		
		UserDialog.setUserData(null);
		
		UserDialog.show(rootStackPane);
	}
	
	@FXML
	public void DeleteChildUserAction(){
		User user = childUserListView.getSelectionModel().getSelectedItem();
		if(user != null){
			userRepository.delete(user.getId());
			upUserList();
		}
	}
	
	@FXML
	public void ModifyChildUserAction(){
		User user = childUserListView.getSelectionModel().getSelectedItem();
		if(user != null){
			UserDialogHeadLabel.setText(UserTypeLabel.getText()+"��Ա");
			
			UserDialog.setUserData(user);
			
			userAccountTextField.setText(user.getAccount());
			userPasswordTextField.setText(user.getPassword());
			userNameTextField.setText(user.getName());
			userSexTextField.setText(user.getSex());
			userAgeTextField.setText(user.getAge());
			userPhoneTextField.setText(user.getPassword());
			userJobTextField.setText(user.getJob());
			userDescTextField.setText(user.getDsc());
			
			adminPasswordTextField.clear();
			
			if(UserDialogVbox.getChildren().contains(UserAccountHbox))
				UserDialogVbox.getChildren().remove(UserAccountHbox);
			
			if(!UserDialogVbox.getChildren().contains(UserFatherHbox)){
				UserDialogVbox.getChildren().add(UserDialogVbox.getChildren().size(), UserFatherHbox);
			}
			
			userFatherCom.getItems().clear();
			userFatherCom.getItems().addAll(UserListView.getItems());
			userFatherCom.getSelectionModel().select(UserListView.getSelectionModel().getSelectedItem());
			
			UserDialog.show(rootStackPane);
		}
		else
			showLogsDialog("ѡ��Ϊ��");
	}
	
	@FXML
	public void ConfirmAction(){
		User user = new User();
		//���
		if(UserDialog.getUserData() == null){
			//�������
			if(UserDialogHeadLabel.getText().endsWith("����")){
				user.setFatheraccount(null);
			}
			//���Ա��
			else {
				user.setFatheraccount(userFatherCom.getSelectionModel().getSelectedItem().getAccount());
			}
			
			if("����".equals(UserTypeLabel.getText()))
				user.setType(2);
			else if("�з�".equals(UserTypeLabel.getText()))
				user.setType(3);
			else if("�����".equals(UserTypeLabel.getText()))
				user.setType(4);
		}
		//�޸�
		else{
			user = (User) UserDialog.getUserData();
			
			//�޸�����
			if(UserDialogHeadLabel.getText().endsWith("����")){		
				
			}
			//�޸�Ա��
			else {
				user.setFatheraccount(userFatherCom.getSelectionModel().getSelectedItem().getAccount());
			}
		}
		
		user.setAccount(userAccountTextField.getText());
		user.setPassword(userPasswordTextField.getText());
		user.setName(userNameTextField.getText());
		user.setAge(userAgeTextField.getText());
		user.setSex(userSexTextField.getText());
		user.setPhone(userPhoneTextField.getText());
		user.setJob(userJobTextField.getText());
		user.setDsc(userDescTextField.getText());
		
		UserDialog.close();
		
		User admin = userRepository.findByAccount(userSession.getAccount());
		if(admin.getPassword().equals(adminPasswordTextField.getText())){
			userRepository.save(user);
			upUserList();
		}
		else
			showLogsDialog("��������޴�Ȩ�ޣ�");
	}
	
	@FXML
	public void CancelAction(){
		
		if(UserDialog.isVisible())
			UserDialog.close();
		
		if(logDialog.isVisible())
			logDialog.close();
	}
}

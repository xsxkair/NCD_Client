
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
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.MyUserActionEnum;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.TestData;
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
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

@Component
public class ChildUserHandler implements HandlerTemplet{

	private AnchorPane rootpane;
	
	@FXML StackPane rootStackPane;
	
	@FXML JFXListView<ListViewCell> GB_UserListView;
	
	@FXML TextField GB_UserAccountTextField;
	@FXML PasswordField GB_UserPassWordPassWordField;
	@FXML TextField GB_UserNameTextField;
	@FXML TextField GB_UserAgeTextField;
	@FXML TextField GB_UserSexTextField;
	@FXML TextField GB_UserPhoneTextField;
	@FXML TextField GB_UserJobTextField;
	@FXML TextField GB_UserDescTextField;
	
	@FXML ImageView GB_AddUserImageView;
	@FXML ImageView GB_CancelAddUserImageView;
	@FXML ImageView GB_CancelEditUserImageView;
	@FXML ImageView GB_EditUserImageView;
	private Image deleteUserIco = null;
	@FXML StackPane GB_ModifyIcoStackPane;
	@FXML Button GB_SaveUserInfoButton;
	
	@FXML JFXDialog modifyUserInfoDialog;
	@FXML PasswordField userPasswordTextField;
	@FXML JFXButton acceptButton0;
	@FXML JFXButton cancelButton0;
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton acceptButton2;

	private List<User> myUserList;
	private MyUserActionEnum GB_ActionType = MyUserActionEnum.NONE;
	private User itsMe = null;
	private ListViewCell selectListViewCell = null;
	private User tempUser = null;
	
	ContextMenu myContextMenu;
	MenuItem RefreshMenuItem;
	
	@Autowired private UserRepository userRepository;
	@Autowired private UserSession userSession;
	@Autowired private WorkPageSession workPageSession;
	
	@PostConstruct
	@Override
	public void UI_Init(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/ChildUserPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/ChildUserPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        deleteUserIco = new Image(this.getClass().getResourceAsStream("/RES/deleteUserIco.png"));
        rootStackPane.getChildren().remove(modifyUserInfoDialog);
        rootStackPane.getChildren().remove(LogDialog);
        
        GB_EditUserImageView.setOnMouseClicked((e)->{
        	setEnableEdit(true);
        	GB_ActionType = MyUserActionEnum.EDIT;
        });
        
        GB_CancelEditUserImageView.setOnMouseClicked((e)->{
        	setEnableEdit(false);
        	GB_ActionType = MyUserActionEnum.NONE;
        });
        
        GB_AddUserImageView.setOnMouseClicked((e)->{
        	GB_ActionType = MyUserActionEnum.ADD;
        	setInAddStatus(true);
        });
        GB_CancelAddUserImageView.setOnMouseClicked((e)->{
        	GB_ActionType = MyUserActionEnum.NONE;
        	setInAddStatus(false);
        });
        
        //权限确认
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
        acceptButton0.setOnAction((e)->{
        	modifyUserInfoDialog.close();
        	if(userPasswordTextField.getText().equals(itsMe.getPassword())){
        		if(GB_ActionType.equals(MyUserActionEnum.DELETE)){
        			userRepository.delete(((User)(selectListViewCell.getUserData())).getId());
        			upUserList();
        		}
        		else if(GB_ActionType.equals(MyUserActionEnum.ADD)){
        			tempUser = userRepository.findByAccount(GB_UserAccountTextField.getText());
        			if(tempUser != null){
        				showLogsDialog("错误", "用户已存在，请检查！");
        			}
        			else{
        				tempUser = new User();
        				
        				tempUser.setAccount(GB_UserAccountTextField.getText());
        				tempUser.setPassword(GB_UserPassWordPassWordField.getText());
        				tempUser.setName(GB_UserNameTextField.getText());
        				tempUser.setAge(GB_UserAgeTextField.getText());
        				tempUser.setSex(GB_UserSexTextField.getText());
        				tempUser.setPhone(GB_UserPhoneTextField.getText());
        				tempUser.setJob(GB_UserJobTextField.getText());
        				tempUser.setDsc(GB_UserDescTextField.getText());
        				tempUser.setType(itsMe.getType());
        				tempUser.setFatheraccount(itsMe.getAccount());
        				
        				userRepository.save(tempUser);
        				
        				setInAddStatus(false);
        				
        				upUserList();
        			}
        		}
        		else if(GB_ActionType.equals(MyUserActionEnum.EDIT)){
        			tempUser = userRepository.findByAccount(GB_UserAccountTextField.getText());
        			if(tempUser == null){
        				showLogsDialog("错误", "用户不存在，请检查！");
        			}
        			else{
        				tempUser.setAccount(GB_UserAccountTextField.getText());
        				tempUser.setPassword(GB_UserPassWordPassWordField.getText());
        				tempUser.setName(GB_UserNameTextField.getText());
        				tempUser.setAge(GB_UserAgeTextField.getText());
        				tempUser.setSex(GB_UserSexTextField.getText());
        				tempUser.setPhone(GB_UserPhoneTextField.getText());
        				tempUser.setJob(GB_UserJobTextField.getText());
        				tempUser.setDsc(GB_UserDescTextField.getText());
        				
        				userRepository.save(tempUser);
        				
        				setEnableEdit(false);
        				
        				upUserList();
        			}
        		}
        	}
        	else
        		showLogsDialog("错误", "密码错误，禁止操作！");
        });
        cancelButton0.setOnAction((e)->{
        	modifyUserInfoDialog.close();
        });
        
        //取消修改密码
        
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        //添加或修改
        GB_SaveUserInfoButton.disableProperty().bind(new BooleanBinding() {
        	{
        		bind(GB_UserAccountTextField.lengthProperty());
        		bind(GB_UserPassWordPassWordField.lengthProperty());
        		bind(GB_UserNameTextField.lengthProperty());
        	}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				if((GB_UserAccountTextField.getLength() > 0) && (GB_UserPassWordPassWordField.getLength() > 5)
				 && (GB_UserNameTextField.getLength() > 0))
					return false;
				else
					return true;
			}
		});
        GB_SaveUserInfoButton.setOnAction((e)->{
			userPasswordTextField.clear();
    		modifyUserInfoDialog.setTransitionType(DialogTransition.CENTER);
    		modifyUserInfoDialog.show(rootStackPane);
        });
        
        //主管
        GB_UserListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		selectListViewCell = newValue;
        		newValue.setDeleteIcoVisible(true);
        		setEnableEdit(false);
        	}
        	
        	if(oldValue != null)
        		oldValue.setDeleteIcoVisible(false);
        });
        
        RefreshMenuItem = new MenuItem("刷新");
        RefreshMenuItem.setOnAction(e->{
        	upUserList();
        });
        
        myContextMenu = new ContextMenu(RefreshMenuItem);
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue)->{
        	if(rootpane.equals(newValue)){
        		itsMe = userRepository.findByAccount(userSession.getAccount());
        		upUserList();
        	}
        	else if(rootpane.equals(oldValue)){
        		itsMe = null;
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

		myUserList = userRepository.queryChildUserList(userSession.getAccount());
		
		GB_UserListView.getItems().clear();
		for (User user : myUserList) {
			ListViewCell tempListViewCell = new ListViewCell(user, deleteUserIco);
			GB_UserListView.getItems().add(tempListViewCell);
		}

		GB_UserListView.getSelectionModel().selectFirst();
	}
	
	private void setEnableEdit(boolean editable) {
		GB_UserAccountTextField.setEditable(false);
		GB_UserPassWordPassWordField.setEditable(editable);
		GB_UserNameTextField.setEditable(editable);
		GB_UserAgeTextField.setEditable(editable);
		GB_UserSexTextField.setEditable(editable);
		GB_UserPhoneTextField.setEditable(editable);
		GB_UserJobTextField.setEditable(editable);
		GB_UserDescTextField.setEditable(editable);
		
		GB_SaveUserInfoButton.setVisible(editable);
		
		if(editable){
			GB_EditUserImageView.setVisible(false);
			GB_CancelEditUserImageView.setVisible(true);
		}
		else{
			GB_EditUserImageView.setVisible(true);
			GB_CancelEditUserImageView.setVisible(false);
			showSelectUserInfo();
		}			
	}
	
	private void setInAddStatus(boolean editable) {
		GB_UserAccountTextField.setEditable(editable);
		GB_UserPassWordPassWordField.setEditable(editable);
		GB_UserNameTextField.setEditable(editable);
		GB_UserAgeTextField.setEditable(editable);
		GB_UserSexTextField.setEditable(editable);
		GB_UserPhoneTextField.setEditable(editable);
		GB_UserJobTextField.setEditable(editable);
		GB_UserDescTextField.setEditable(editable);
		
		GB_SaveUserInfoButton.setVisible(editable);
		
		if(editable){
			GB_AddUserImageView.setVisible(false);
			GB_CancelAddUserImageView.setVisible(true);
			clearUserInfo();
		}
		else{
			GB_AddUserImageView.setVisible(true);
			GB_CancelAddUserImageView.setVisible(false);
			showSelectUserInfo();
		}			
	}
	
	private void clearUserInfo() {
		GB_UserAccountTextField.clear();;
		GB_UserPassWordPassWordField.clear();
		GB_UserNameTextField.clear();
		GB_UserAgeTextField.clear();
		GB_UserSexTextField.clear();
		GB_UserPhoneTextField.clear();
		GB_UserJobTextField.clear();
		GB_UserDescTextField.clear();
	}
	
	private void showSelectUserInfo() {
		User user = (User) selectListViewCell.getUserData();

		GB_UserAccountTextField.setText(user.getAccount());
		GB_UserPassWordPassWordField.setText(user.getPassword());
		GB_UserNameTextField.setText(user.getName());
		GB_UserAgeTextField.setText(user.getAge());
		GB_UserSexTextField.setText(user.getSex());
		GB_UserPhoneTextField.setText(user.getPhone());
		GB_UserJobTextField.setText(user.getJob());
		GB_UserDescTextField.setText(user.getDsc());
	}
	
	private void showLogsDialog(String head, String logs) {
		LogDialogHead.setText(head);
		LogDialogContent.setText(logs);
		LogDialog.show(rootStackPane);
	}
	
/*	@FXML
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
*/

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
	
	class ListViewCell extends AnchorPane{
		
		ImageView imageView = null;
		
		public ListViewCell(User user, Image image){
			Label userName = new Label(user.getName());
			AnchorPane.setTopAnchor(userName, 0.0);
	        AnchorPane.setBottomAnchor(userName, 0.0);
	        AnchorPane.setLeftAnchor(userName, 0.0);
			
			imageView = new ImageView(image);
			imageView.setVisible(false);
			imageView.setFitWidth(25);
			imageView.setFitHeight(25);
			imageView.setCursor(Cursor.HAND);
			imageView.setOnMouseClicked((e)->{
				GB_ActionType = MyUserActionEnum.DELETE;
				userPasswordTextField.clear();
	    		modifyUserInfoDialog.setTransitionType(DialogTransition.CENTER);
	    		modifyUserInfoDialog.show(rootStackPane);
			});
			AnchorPane.setTopAnchor(imageView, 0.0);
	        AnchorPane.setBottomAnchor(imageView, 0.0);
	        AnchorPane.setRightAnchor(imageView, 0.0);
			
			this.setUserData(user);
			this.getChildren().addAll(userName, imageView);
		}
		
		public void setDeleteIcoVisible(boolean status){
			imageView.setVisible(status);
		}
	}
}

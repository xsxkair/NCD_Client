
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
public class UserHandler implements EventHandler<MouseEvent>{

	private AnchorPane rootpane;
	
	private IntegerProperty userType;
	
	@FXML StackPane rootStackPane;
	
	@FXML Label UserTypeLabel;								//��ǰ�������û�����
	
	//����
	@FXML StackPane addUserStackPane;						//�������ͼ��
	@FXML StackPane deleteUserStackPane;					//ɾ������ͼ��
	@FXML StackPane modifyUserStackPane;					//�޸�����ͼ��
	@FXML JFXListView<User> UserListView;						//�����б�
	
	@FXML Label userAccountLabel;
	@FXML Label userNameLabel;
	@FXML Label userAgeLabel;
	@FXML Label userSexLabel;
	@FXML Label userPhoneLabel;
	@FXML Label userJobLabel;
	@FXML Label userDescLabel;
	
	@FXML GridPane UserInfoPane;
	
	//�ҵ��豸
	@FXML AnchorPane myDevicePane;
	@FXML JFXListView<Device> deviceListView;						//�����б�
	
	//�ҵ���Ա
	@FXML AnchorPane myChildUserPane;
	@FXML StackPane addChildUserStackPane;						//�������ͼ��
	@FXML StackPane deleteChildUserStackPane;					//ɾ������ͼ��
	@FXML StackPane modifyChildUserStackPane;					//�޸�����ͼ��
	@FXML JFXListView<User> childUserListView;						//�����б�
	
	@FXML VBox myChildUserInfoPane;
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
	@FXML HBox adminPasswordHBox;
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
	public void UI_Init(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/UserPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/UserPage.fxml");
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
        
        SVGGlyph addChildUserSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.plus");
        addChildUserSvgGlyph.setPrefSize(12, 12);
        addChildUserSvgGlyph.setFill(Color.GREY);  
        addChildUserStackPane.getChildren().add(addChildUserSvgGlyph);
        addChildUserStackPane.setOnMouseEntered(this);
        addChildUserStackPane.setOnMouseExited(this);
        
        SVGGlyph deleteChildUserSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.close, remove, times");
        deleteChildUserSvgGlyph.setPrefSize(12, 12);
        deleteChildUserSvgGlyph.setFill(Color.GREY);
        deleteChildUserStackPane.getChildren().add(deleteChildUserSvgGlyph);
        deleteChildUserStackPane.setOnMouseEntered(this);
        deleteChildUserStackPane.setOnMouseExited(this);
        
        SVGGlyph modifyChildUserSvgGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.pencil2");
        modifyChildUserSvgGlyph.setPrefSize(12, 12);
        modifyChildUserSvgGlyph.setFill(Color.GREY);
        modifyChildUserStackPane.getChildren().add(modifyChildUserSvgGlyph);
        modifyChildUserStackPane.setOnMouseEntered(this);
        modifyChildUserStackPane.setOnMouseExited(this);
        
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
        		
        		//��ȡ�ҵ��豸
        		List<Device> myDeviceList = deviceRepository.findByAccount(user.getAccount());
        		deviceListView.getItems().clear();
        		deviceListView.getItems().addAll(myDeviceList);
        		
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
        
        userType = new SimpleIntegerProperty();
        userType.addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		
        		if(!UserInfoPane.getChildren().contains(myDevicePane))
        			UserInfoPane.getChildren().add(myDevicePane);
        		if(!UserInfoPane.getChildren().contains(myChildUserPane))
        			UserInfoPane.getChildren().add(myChildUserPane);
        		if(!UserInfoPane.getChildren().contains(myChildUserInfoPane))
        			UserInfoPane.getChildren().add(myChildUserInfoPane);
        		
        		if(newValue.equals(1)){
        			UserTypeLabel.setText("����Ա");
        			if(UserInfoPane.getChildren().contains(myDevicePane))
            			UserInfoPane.getChildren().remove(myDevicePane);
            		if(UserInfoPane.getChildren().contains(myChildUserPane))
            			UserInfoPane.getChildren().remove(myChildUserPane);
            		if(UserInfoPane.getChildren().contains(myChildUserInfoPane))
            			UserInfoPane.getChildren().remove(myChildUserInfoPane);
        		}
        		else if (newValue.equals(2)) {
        			UserTypeLabel.setText("����");
				}
        		else if (newValue.equals(3)) {
        			UserTypeLabel.setText("�з�");
				}
        		else if (newValue.equals(4)) {
        			UserTypeLabel.setText("�����");
				}
        		else if (newValue.equals(5)) {
        			UserTypeLabel.setText("Ʒ��");
				}
        		
        		upUserList();
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
				if((userAccountTextField.getLength() > 0) && (userPasswordTextField.getLength() >= 6) &&
						(adminPasswordTextField.getLength() >= 6))
					return false;
				else
					return true;
			}
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
        
        loader = null;
        in = null;
	}
	
	public void ShowUserPage(int userType){
		this.userType.set(userType);
		workPageSession.setWorkPane(rootpane);
	}
	
	private void upUserList() {
		List<User> myUserList = null;
		
		if(userType.getValue().equals(1))
			myUserList = userRepository.queryAllAdministrator();
		else if (userType.getValue().equals(2)) 
			myUserList = userRepository.queryAllFatherSaler();
		else if (userType.getValue().equals(3)) 
			myUserList = userRepository.queryAllFatherNcdLaber();
		else if (userType.getValue().equals(4)) 
			myUserList = userRepository.queryAllFatherManager();
		else if (userType.getValue().equals(5)) 
			myUserList = userRepository.queryAllFatherQualityControler();
		
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

		if(userType.get() != 1)
			UserDialogHeadLabel.setText("���"+UserTypeLabel.getText()+"����");
		else
			UserDialogHeadLabel.setText("���"+UserTypeLabel.getText());
		
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
			if(userType.get() != 1)
				UserDialogHeadLabel.setText("�޸�"+UserTypeLabel.getText()+"������Ϣ");
			else
				UserDialogHeadLabel.setText("�޸�"+UserTypeLabel.getText()+"��Ϣ");
			
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
			
			if(UserDialogVbox.getChildren().contains(UserFatherHbox))
				UserDialogVbox.getChildren().remove(UserFatherHbox);
			
			UserDialog.show(rootStackPane);
		}
		else
			showLogsDialog("ѡ��Ϊ��");
	}
	
	@FXML
	public void AddChildUserAction(){

		if(userType.get() != 1)
			UserDialogHeadLabel.setText("���"+UserTypeLabel.getText()+"��Ա");
		else
			UserDialogHeadLabel.setText("���"+UserTypeLabel.getText());

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

			if(userType.get() != 1)
				UserDialogHeadLabel.setText("�޸�"+UserTypeLabel.getText()+"��Ա��Ϣ");
			else
				UserDialogHeadLabel.setText("�޸�"+UserTypeLabel.getText()+"��Ϣ");
			
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
		
		User admin = userRepository.findByAccount(userSession.getAccount());
		if(!admin.getPassword().equals(adminPasswordTextField.getText())){
			showLogsDialog("��������޴�Ȩ�ޣ�");
			return;
		}
		
		//���
		if(UserDialog.getUserData() == null){
			//���Ա��
			if(UserDialogVbox.getChildren().contains(UserFatherHbox)){
				user.setFatheraccount(userFatherCom.getSelectionModel().getSelectedItem().getAccount());
			}
			//�������
			else {
				user.setFatheraccount(null);
			}	
			
		}
		//�޸�
		else{
			user = (User) UserDialog.getUserData();
			
			//�޸�Ա��
			if(UserDialogVbox.getChildren().contains(UserFatherHbox)){
				user.setFatheraccount(userFatherCom.getSelectionModel().getSelectedItem().getAccount());
			}
			//�޸�����
			else {

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
		
		user.setType(userType.get());
		
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
}

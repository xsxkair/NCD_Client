package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.repository.ManagerRepository;
import com.xsx.ncd.spring.ManagerSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

@Component
public class ManagerInfoHandler {

	private AnchorPane rootpane;
	
	@FXML
	VBox GB_ContentPane;
	
	@FXML
	TextField GB_AdminNameTextField;
	@FXML
	TextField GB_AdminSexTextField;
	@FXML
	TextField GB_AdminAgeTextField;
	@FXML
	TextField GB_AdminPhoneTextField;
	@FXML
	TextField GB_AdminJobTextField;
	@FXML
	TextField GB_AdminDescTextField;
	@FXML
	PasswordField GB_AdminPasswordTextField;
	@FXML
	Button GB_AdminModifyButton;
	
	@FXML
	VBox GB_ManagerListPane;
	@FXML
	ListView<Manager> GB_ManagerListView;
	@FXML
	TextField GB_ManagerAccoutTextFiled;
	@FXML
	PasswordField GB_ManagerPasswordTextFiled;
	@FXML
	TextField GB_ManagerNameTextFiled;
	@FXML
	TextField GB_ManagerSexTextFiled;
	@FXML
	TextField GB_ManagerAgeTextFiled;
	@FXML
	TextField GB_ManagerPhoneTextFiled;
	@FXML
	TextField GB_ManagerJobTextFiled;
	@FXML
	TextField GB_ManagerDescTextFiled;
	@FXML
	Button GB_ModifyManagerButton;
	@FXML
	Button GB_DeleteManagerButton;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ManagerSession managerSession;
	
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue)->{
        	if((newValue != null) && (newValue.equals(rootpane)))
        		UpPageValue();
        });
        
        GB_ManagerListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	if(newValue == null){
				GB_ManagerAccoutTextFiled.setText(null);
				GB_ManagerPasswordTextFiled.setText(null);
				GB_ManagerNameTextFiled.setText(null);
				GB_ManagerSexTextFiled.setText(null);
				GB_ManagerAgeTextFiled.setText(null);
				GB_ManagerPhoneTextFiled.setText(null);
				GB_ManagerJobTextFiled.setText(null);
				GB_ManagerDescTextFiled.setText(null);
			}
        	else {
        		GB_ManagerAccoutTextFiled.setText((newValue.getAccount()==null)?"null":newValue.getAccount().toString());
				GB_ManagerPasswordTextFiled.setText((newValue.getPassword()==null)?"null":newValue.getPassword().toString());
				GB_ManagerNameTextFiled.setText((newValue.getName()==null)?"null":newValue.getName().toString());
				GB_ManagerAgeTextFiled.setText((newValue.getAge()==null)?"null":newValue.getAge().toString());
				GB_ManagerSexTextFiled.setText((newValue.getSex()==null)?"null":newValue.getSex().toString());
				GB_ManagerPhoneTextFiled.setText((newValue.getPhone()==null)?"null":newValue.getPhone().toString());
				GB_ManagerJobTextFiled.setText((newValue.getJob()==null)?"null":newValue.getJob().toString());
				GB_ManagerDescTextFiled.setText((newValue.getDsc()==null)?"null":newValue.getDsc().toString());
			}
        });
        
        GB_AdminModifyButton.disableProperty().bind(new BooleanBinding() {
        	{
        		bind(GB_AdminNameTextField.textProperty());
        		bind(GB_AdminPasswordTextField.textProperty());
        	}

			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				
				if((GB_AdminNameTextField.getText() != null)&&(GB_AdminNameTextField.getText().length() > 0)
						&&(GB_AdminPasswordTextField.getText() != null)&&(GB_AdminPasswordTextField.getText().length() > 0))
					return false;
				else
					return true;
			}
		});
        
        GB_ModifyManagerButton.disableProperty().bind(new BooleanBinding() {
        	{
        		bind(GB_ManagerAccoutTextFiled.textProperty());
        		bind(GB_ManagerPasswordTextFiled.textProperty());
        		bind(GB_ManagerNameTextFiled.textProperty());
        	}

			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				
				if((GB_ManagerAccoutTextFiled.getText() != null)&&(GB_ManagerAccoutTextFiled.getText().length() > 0)
						&&(GB_ManagerPasswordTextFiled.getText() != null)&&(GB_ManagerPasswordTextFiled.getText().length() > 0)
						&&(GB_ManagerNameTextFiled.getText() != null)&&(GB_ManagerNameTextFiled.getText().length() > 0))
					return false;
				else
					return true;
			}
		});
        GB_DeleteManagerButton.disableProperty().bind(GB_ModifyManagerButton.disabledProperty());
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public void ShowMyInfoPage(){
		workPageSession.setWorkPane(rootpane);
	}
	
	private void UpPageValue() {
		UpAdminValue();
		UpChildManagerValue();
	}
	
	private void UpAdminValue() {
		Manager admin = managerRepository.findManagerByAccount(managerSession.getAccount());
		
		GB_AdminNameTextField.setText(admin.getName());
		GB_AdminSexTextField.setText(admin.getSex());
		GB_AdminAgeTextField.setText(admin.getAge());
		GB_AdminPhoneTextField.setText(admin.getPhone());
		GB_AdminJobTextField.setText(admin.getJob());
		GB_AdminDescTextField.setText(admin.getDsc());
		GB_AdminPasswordTextField.setText(admin.getPassword());
		
		GB_ContentPane.getChildren().remove(GB_ManagerListPane);
		
		if(admin.getFatheraccount() == null){
			GB_ContentPane.getChildren().add(GB_ManagerListPane);
		}
	}
	
	private void UpChildManagerValue() {
		List<Manager> managernamelist = managerRepository.queryChildAccountList(managerSession.getAccount());

		GB_ManagerListView.getItems().clear();
		GB_ManagerListView.getItems().addAll(managernamelist);
	}
	
	@FXML
	public void GB_AdminModifyAction(){
		Manager admin = managerRepository.findManagerByAccount(managerSession.getAccount());
		
		if(CheckRight(rootpane.getScene().getWindow(), admin.getPassword())){
				
			admin.setPassword(GB_AdminPasswordTextField.getText());
			admin.setName(GB_AdminNameTextField.getText());
			admin.setAge(GB_AdminAgeTextField.getText());
			admin.setSex(GB_AdminSexTextField.getText());
			admin.setPhone(GB_AdminPhoneTextField.getText());
			admin.setJob(GB_AdminJobTextField.getText());
			admin.setDsc(GB_AdminDescTextField.getText());
		
			managerRepository.save(admin);
		}
	}
	
	@FXML
	public void GB_RefreshManagerAction(){
		UpAdminValue();
	}
	
	@FXML
	public void GB_ModifyManagerAction(){
		//获取管理员
		Manager admin = managerRepository.findManagerByAccount(managerSession.getAccount());
		
		if(CheckRight(rootpane.getScene().getWindow(), admin.getPassword())){
			
			//获取选中的审核人
			Manager manager = managerRepository.findManagerByAccount(GB_ManagerAccoutTextFiled.getText());
			
			if(manager == null){
				manager = new Manager();
			}
			manager.setAccount(GB_ManagerAccoutTextFiled.getText());
			manager.setPassword(GB_ManagerPasswordTextFiled.getText());
			manager.setName(GB_ManagerNameTextFiled.getText());
			manager.setAge(GB_ManagerAgeTextFiled.getText());
			manager.setSex(GB_ManagerSexTextFiled.getText());
			manager.setPhone(GB_ManagerPhoneTextFiled.getText());
			manager.setJob(GB_ManagerJobTextFiled.getText());
			manager.setDsc(GB_ManagerDescTextFiled.getText());
			manager.setFatheraccount(admin.getAccount());
			
			managerRepository.save(manager);
			
			UpChildManagerValue();
		}
	}

	
	@FXML
	public void GB_DeleteManagerAction(){
		//获取管理员
		Manager admin = managerRepository.findManagerByAccount(managerSession.getAccount());
		
		if(CheckRight(rootpane.getScene().getWindow(), admin.getPassword())){
					
			//获取选中的审核人
			Manager manager = GB_ManagerListView.getSelectionModel().getSelectedItem();
					
			managerRepository.delete(manager);
					
			UpChildManagerValue();
		}
	}
	
	private boolean CheckRight(Window owner, String promtext) {
		
		TextInputDialog inputDialog = new TextInputDialog("input admin password");
		inputDialog.initOwner(owner);
		Optional<String> result = inputDialog.showAndWait();
		
		if(result.isPresent()){
			if(result.get().equals(promtext))
				return true;
			else {
				Alert alert = new Alert(AlertType.ERROR, "Access denied!", ButtonType.OK);
				alert.initOwner(owner);
				alert.showAndWait();
				
				return false;
			}
		}
		
		return false;
	}
}

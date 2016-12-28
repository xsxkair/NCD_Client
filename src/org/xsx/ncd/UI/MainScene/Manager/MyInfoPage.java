package org.xsx.ncd.UI.MainScene.Manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.xsx.ncd.Data.SignedManager;
import org.xsx.ncd.Data.UIMainPage;
import org.xsx.ncd.UI.MainScene.DevicePage.DeviceDataPackage;

import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.tool.SpringFacktory;

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

public class MyInfoPage {

	private static MyInfoPage S_MyInfoPage = null;
	
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
	ListView<ManagerListViewItem> GB_ManagerListView;
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

	private MyInfoPage() {
		
	}
	
	public static MyInfoPage GetInstance() {
		if(S_MyInfoPage == null)
			S_MyInfoPage = new MyInfoPage();
		
		return S_MyInfoPage;
	}
	
	public void UI_Init(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("MyInfoPagefxml.fxml"));
        InputStream in = this.getClass().getResourceAsStream("MyInfoPagefxml.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        UIMainPage.GetInstance().getGB_Page().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootpane.equals(newValue)){
					UpPageValue();
				}
			}
		});
        
        GB_ManagerListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ManagerListViewItem>() {

			@Override
			public void changed(ObservableValue<? extends ManagerListViewItem> observable, ManagerListViewItem oldValue, ManagerListViewItem newValue) {
				// TODO Auto-generated method stub
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
					Manager tBean = SpringFacktory.getManagerRepository().findManagerByAccount(newValue.GetAccount());
					
					if(tBean == null){
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
						GB_ManagerAccoutTextFiled.setText((tBean.getAccount()==null)?"null":tBean.getAccount().toString());
						GB_ManagerPasswordTextFiled.setText((tBean.getPassword()==null)?"null":tBean.getPassword().toString());
						GB_ManagerNameTextFiled.setText((tBean.getName()==null)?"null":tBean.getName().toString());
						GB_ManagerAgeTextFiled.setText((tBean.getAge()==null)?"null":tBean.getAge().toString());
						GB_ManagerSexTextFiled.setText((tBean.getSex()==null)?"null":tBean.getSex().toString());
						GB_ManagerPhoneTextFiled.setText((tBean.getPhone()==null)?"null":tBean.getPhone().toString());
						GB_ManagerJobTextFiled.setText((tBean.getJob()==null)?"null":tBean.getJob().toString());
						GB_ManagerDescTextFiled.setText((tBean.getDsc()==null)?"null":tBean.getDsc().toString());
					}
				}
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
        GB_DeleteManagerButton.disableProperty().bind(new BooleanBinding() {
			
        	{
        		bind(GB_ManagerListView.getSelectionModel().selectedItemProperty());
        	}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				if(GB_ManagerListView.getSelectionModel().selectedItemProperty().get() == null)
					return true;
				else
					return false;
			}
		});
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public AnchorPane GetPane() {
		return rootpane;
	}
	
	private void UpPageValue() {
		UpAdminValue();
		UpChildManagerValue();
	}
	
	private void UpAdminValue() {
		Manager admin = SpringFacktory.getManagerRepository().findManagerByAccount(SignedManager.GetInstance().getGB_SignedAccount());
		
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
		List<Object[]> managernamelist = ManagerDao.QueryChildManagerNameList(SignedManager.GetInstance().getGB_SignedAccount());
		
		List<ManagerListViewItem> items = new ArrayList<>();
		for (Object[] namelist : managernamelist) {
			items.add(new ManagerListViewItem(namelist));
		}
		
		GB_ManagerListView.getItems().clear();
		GB_ManagerListView.setItems(FXCollections.observableArrayList(items));
	}
	
	@FXML
	public void GB_AdminModifyAction(){
		ManagerBean admin = ManagerDao.QueryReportManager(SignedManager.GetInstance().getGB_SignedAccount(), null);
		
		if(CheckRight(rootpane.getScene().getWindow(), admin.getPassword())){
				
			admin.setPassword(GB_AdminPasswordTextField.getText());
			admin.setName(GB_AdminNameTextField.getText());
			admin.setAge(GB_AdminAgeTextField.getText());
			admin.setSex(GB_AdminSexTextField.getText());
			admin.setPhone(GB_AdminPhoneTextField.getText());
			admin.setJob(GB_AdminJobTextField.getText());
			admin.setDsctext(GB_AdminDescTextField.getText());
		
			//失败
			if(ManagerDao.ModifyManagerInfo(admin) == false){
				Alert alert = new Alert(AlertType.ERROR, "修改失败!", ButtonType.OK);
				alert.initOwner(rootpane.getScene().getWindow());
				alert.showAndWait();
			}
		}
	}
	
	@FXML
	public void GB_RefreshManagerAction(){
		UpAdminValue();
	}
	
	@FXML
	public void GB_ModifyManagerAction(){
		//获取管理员
		ManagerBean admin = ManagerDao.QueryReportManager(SignedManager.GetInstance().getGB_SignedAccount(), null);
		
		if(CheckRight(rootpane.getScene().getWindow(), admin.getPassword())){
		
			ManagerBean	manager = new ManagerBean();
			
			manager.setAccount(GB_ManagerAccoutTextFiled.getText());
			manager.setPassword(GB_ManagerPasswordTextFiled.getText());
			manager.setName(GB_ManagerNameTextFiled.getText());
			manager.setAge(GB_ManagerAgeTextFiled.getText());
			manager.setSex(GB_ManagerSexTextFiled.getText());
			manager.setPhone(GB_ManagerPhoneTextFiled.getText());
			manager.setJob(GB_ManagerJobTextFiled.getText());
			manager.setDsctext(GB_ManagerDescTextFiled.getText());
			manager.setFatheraccount(admin.getAccount());
			manager.setDevicelist(admin.getDevicelist());
			
			if(ManagerDao.ModifyManagerInfo(manager) == false){
				Alert alert = new Alert(AlertType.ERROR, "修改失败!", ButtonType.OK);
				alert.initOwner(rootpane.getScene().getWindow());
				alert.showAndWait();
			}
			
			UpChildManagerValue();
		}
	}

	
	@FXML
	public void GB_DeleteManagerAction(){

		//获取管理员
		ManagerBean admin = ManagerDao.QueryReportManager(SignedManager.GetInstance().getGB_SignedAccount(), null);
				
		if(CheckRight(rootpane.getScene().getWindow(), admin.getPassword())){
						
			if(ManagerDao.DeleteManager(GB_ManagerListView.getSelectionModel().getSelectedItem().GetAccount()) == false){
				Alert alert = new Alert(AlertType.ERROR, "删除失败!", ButtonType.OK);
				alert.initOwner(rootpane.getScene().getWindow());
				alert.showAndWait();
			}
					
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

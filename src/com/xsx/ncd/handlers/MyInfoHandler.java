package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

@Component
public class MyInfoHandler {

	private AnchorPane rootpane;
	
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        RefreshMenuItem = new MenuItem("Ë¢ÐÂ");
        RefreshMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        	
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				UpPageValue();
			}
		});
        myContextMenu = new ContextMenu(RefreshMenuItem);
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue)->{
        	if((newValue != null) && (newValue.equals(rootpane)))
        		UpPageValue();
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
	
	private void UpPageValue() {
		UpAdminValue();
	}
	
	private void UpAdminValue() {
		User admin = userRepository.findByAccount(userSession.getAccount());
		
		GB_AdminNameTextField.setText(admin.getName());
		GB_AdminSexTextField.setText(admin.getSex());
		GB_AdminAgeTextField.setText(admin.getAge());
		GB_AdminPhoneTextField.setText(admin.getPhone());
		GB_AdminJobTextField.setText(admin.getJob());
		GB_AdminDescTextField.setText(admin.getDsc());
		GB_AdminPasswordTextField.setText(admin.getPassword());
	}
	

	@FXML
	public void GB_AdminModifyAction(){
		User admin = userRepository.findByAccount(userSession.getAccount());
		
		if(CheckRight(rootpane.getScene().getWindow(), admin.getPassword())){
				
			admin.setPassword(GB_AdminPasswordTextField.getText());
			admin.setName(GB_AdminNameTextField.getText());
			admin.setAge(GB_AdminAgeTextField.getText());
			admin.setSex(GB_AdminSexTextField.getText());
			admin.setPhone(GB_AdminPhoneTextField.getText());
			admin.setJob(GB_AdminJobTextField.getText());
			admin.setDsc(GB_AdminDescTextField.getText());
		
			userRepository.save(admin);
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

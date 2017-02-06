package com.xsx.ncd.handlers;

import com.xsx.ncd.entity.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChildReportterManagementHandler {
	
	@FXML
	VBox GB_ManagerListPane;
	@FXML
	ListView<User> GB_ManagerListView;
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
}

package com.xsx.ncd.handlers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Component
public class MainContainHandler {
	
	private Stage s_Stage;
	
	private Scene s_Scene;
	
	@FXML AnchorPane GB_RootPane;
	
	@FXML HBox GB_TodayHbox;
	@FXML ImageView GB_WorkSpaceIcoView;
	
	@FXML Label GB_SignedManagerLable;
	@FXML ImageView GB_SignedManagerIcoView;
	
	@FXML MenuBar GB_MenuBar;
	@FXML Menu GB_ReportMenu;
	@FXML Menu GB_DeviceMenu;
	@FXML Menu GB_CardMenu;
	@FXML Menu GB_CheckMenu;
	@FXML Menu GB_ToolMenu;
	@FXML MenuItem GB_ConnectDeviceMenuItem;
	
	@FXML Menu GB_UserManagementMenu;
	@FXML MenuItem MyInfoMenuItem;
	@FXML MenuItem AdminManagementItem;
	@FXML MenuItem SalerManagementMenuItem;
	@FXML MenuItem LabberManagementMenuItem;
	@FXML MenuItem ReportterManagementMenuItem;
	@FXML MenuItem ChildReportterManagementMenuItem;
	
	@FXML Menu GB_SystemSetMenu;
	@FXML Menu GB_AboutMenu;
	
	@Autowired
	private UserSession userSession;
	
	@Autowired
	private WorkPageSession workPageSession;
	
	@Autowired
	private TodayWorkHandler workSpaceHandler;
	
	@Autowired
	private MyInfoHandler managerInfoHandler;
	
	@Autowired
	private DeviceHandler deviceHandler;
	
	@Autowired
	private ReportListHandler reportListHandler;
	
	@Autowired
	private ReportOverViewPage reportOverViewPage;
	
	@Autowired
	private CardRepertoryHandler cardRepertoryHandler;
	@Autowired
	private CardInOutHandler cardInOutHandler;
	@Autowired
	private CardRecordHandler cardRecordHandler;
	@Autowired private DeviceTestHandler deviceTestHandler;
	@Autowired
	private LoginHandler loginHandler;
	
	@Autowired
	private AboutUsHandler aboutUsHandler;
	
	@PostConstruct
	public void UI_Init() {
		AnchorPane root = null;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/ContainerPane.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/ContainerPane.fxml");
        loader.setController(this);
        try {
        	root = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        GB_WorkSpaceIcoView.setImage(new Image(this.getClass().getResourceAsStream("/RES/workspace.png")));
        GB_SignedManagerIcoView.setImage(new Image(this.getClass().getResourceAsStream("/RES/Userico.png")));
        root.getStylesheets().add(this.getClass().getResource("/com/xsx/ncd/views/mainuistyle.css").toExternalForm());
        
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(newValue != null){
					GB_RootPane.getChildren().clear();
					GB_RootPane.getChildren().add(newValue);
				}
			}
		});

		s_Scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

	}
	
	public void startWorkActivity() {
		User user = userSession.getUser();

		GB_SignedManagerLable.setText(user.getName());
		
		//���ݿ����Ա
		if(user.getType().equals(0)){
			
		}
		//��������Ա
		else if(user.getType().equals(1)){

		}
		//����
		else if(user.getType().equals(2)){

		}		
		//�����з�
		else if(user.getType().equals(3)){

		}
		//һ���û�
		else if(user.getType().equals(4)){

		}
		//�����û�
		else if(user.getType().equals(5)){

		}		
		
		workPageSession.getWorkPane().set(workSpaceHandler.GetPane());

		s_Stage = new Stage();
		s_Stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				s_Stage.close();
				System.exit(0);
			}
		});

	    s_Stage.setResizable(true);
	    s_Stage.sizeToScene();
	    s_Stage.setTitle("ӫ�������  V2.3.0");

	    s_Stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
	    s_Stage.setScene(s_Scene);
		s_Stage.show();
	}
	
	@FXML
	public void GB_MyWorkSpaceAction(){
		workPageSession.setWorkPane(workSpaceHandler.GetPane());
	}
	
	@FXML
	public void ReportThumbAction(){
		reportOverViewPage.ShowReportOverViewPage();
	}
	
	@FXML
	public void QueryReportAction(){
		workPageSession.setWorkPane(reportListHandler.GetReportPane());
	}
	
	@FXML
	public void ShowDevicesAction(){
		workPageSession.setWorkPane(deviceHandler.GetPane());
	}
	
	@FXML
	public void GB_ShowCardRepertoryAction(){
		cardRepertoryHandler.ShowCardRepertoryPage();
	}
	
	@FXML
	public void GB_CardInOutAction(){
		cardInOutHandler.ShowCardInOutPage();
	}
	
	@FXML
	public void GB_ShowCardInOutRecordAction(){
		cardRecordHandler.ShowCardRecordPage();
	}
	
	@FXML
	public void GB_DeviceTestAction(){
		deviceTestHandler.showDeviceTestPage();
	}
	
	//�ҵ���Ϣ
	@FXML
	public void MyInfoAction(){
		managerInfoHandler.ShowMyInfoPage();
	}
	//����Ա����
	@FXML
	public void AdminManagementAction(){
		
	}
	//������Ա����
	@FXML
	public void SalerManagementAction(){
		
	}
	//ʵ������Ա����
	@FXML
	public void LabberManagementAction(){
		
	}
	//����˹���
	@FXML
	public void ReportterManagementAction(){
		
	}
	//������˹���
	@FXML
	public void ChildReportterManagementAction(){
			
	}
	
	@FXML
	public void AboutMeAction(){
		aboutUsHandler.ShowAbout();
	}
	
	@FXML
	public void GB_SignedManagerAction(){
		managerInfoHandler.ShowMyInfoPage();
	}
	
	@FXML
	public void GB_SignOutAction(){
		userSession.setUser(null);
		workPageSession.setWorkPane(null);
		s_Stage.close();
		loginHandler.startLoginActivity();
	}
}

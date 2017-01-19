package com.xsx.ncd.handlers;


import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.Manager;
import com.xsx.ncd.repository.ManagerRepository;
import com.xsx.ncd.spring.ManagerSession;
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
	@FXML Menu GB_MyInfoMenu;
	@FXML Menu GB_SystemSetMenu;
	@FXML Menu GB_AboutMenu;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ManagerSession managerSession;
	
	@Autowired
	private WorkPageSession workPageSession;
	
	@Autowired
	private TodayWorkHandler workSpaceHandler;
	
	@Autowired
	private ManagerInfoHandler managerInfoHandler;
	
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
		Manager manager = managerRepository.findManagerByAccount(managerSession.getAccount());

		GB_SignedManagerLable.setText(manager.getName());
		
		//生物研发
		if(manager.getType().equals(3)){
			GB_MenuBar.getMenus().removeAll(GB_ReportMenu, GB_DeviceMenu, GB_CardMenu, GB_CheckMenu);
		}
		//用户
		else if(manager.getType().equals(4)){
			//子用户
			if(manager.getFatheraccount() != null){
				GB_MenuBar.getMenus().removeAll(GB_ReportMenu, GB_DeviceMenu);
			}
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
	    s_Stage.setTitle("荧光分析仪  V2.3.0");

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
	@FXML
	public void ShowMyInfoAction(){
		managerInfoHandler.ShowMyInfoPage();
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
		managerSession.setAccount(null);
		workPageSession.setWorkPane(null);
		s_Stage.close();
		loginHandler.startLoginActivity();
	}
}

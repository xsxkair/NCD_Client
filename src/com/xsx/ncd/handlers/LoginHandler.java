package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.define.SoftInfo;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.entity.NcdSoft;
import com.xsx.ncd.handlers.ReportOverViewPage.QueryTodayReportNumByStatusService.QueryReportTask;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.repository.NcdSoftRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.SpringFacktory;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

@Component
public class LoginHandler {

	private Stage s_Stage;
	
	private Scene s_Scene;
	
	@FXML TextField UserNameText;
	
	@FXML PasswordField UserPasswordText;
	
	@FXML JFXButton LoginButton;
	@FXML ImageView	GB_CloseButton;
	
	private Image closeImage1 = null;
	private Image closeImage2 = null;
	
	private double initX;
    private double initY;
	
	@Autowired private UserRepository userRepository;
	@Autowired private UserSession userSession;
	@Autowired private MainContainHandler mainContainHandler;
	@Autowired private NcdSoftRepository ncdSoftRepository;
	
	private NcdSoft ncdSoft = null;
	private User tempuser = null;
	
	@PostConstruct
	public void UI_Init() {
		AnchorPane root = null;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/LoginFXML.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/LoginFXML.fxml");
        loader.setController(this);
        
        try {
        	root = loader.load(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        s_Scene = new Scene(root, Color.TRANSPARENT);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

                initX = me.getScreenX() - s_Stage.getX();

                initY = me.getScreenY() - s_Stage.getY();
            }
        });

    	root.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

            	s_Stage.setX(me.getScreenX() - initX);

            	s_Stage.setY(me.getScreenY() - initY);
            }
        });
    	
    	LoginButton.disableProperty().bind(new BooleanBinding() {
			{
				bind(UserNameText.lengthProperty());
				bind(UserPasswordText.lengthProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				
				if(UserNameText.getLength() > 0 && UserPasswordText.getLength() >= 6)
					return false;
				else
					return true;
			}
		});
        
        closeImage1 = new Image(this.getClass().getResourceAsStream("/RES/close1.png"));
        closeImage2 = new Image(this.getClass().getResourceAsStream("/RES/close2.png"));
        
        GB_CloseButton.setOnMouseEntered((e)->{
        	GB_CloseButton.setImage(closeImage2);
        });
        
        GB_CloseButton.setOnMouseExited((e)->{
        	GB_CloseButton.setImage(closeImage1);
        });
        
        loader = null;
        in = null;
	}
	
	
	public void startLoginActivity() {
		
		ncdSoft = ncdSoftRepository.findNcdSoftByName(SoftInfo.softName);

		if((ncdSoft != null) && (ncdSoft.getVersion() > SoftInfo.softVersion)){
			try {
				Runtime.getRuntime().exec(".\\jre\\bin\\javaw -jar UPDATE.jar ");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
		
		ncdSoft = null;
		
		s_Stage = new Stage();
		s_Stage.initStyle(StageStyle.TRANSPARENT);
		s_Stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
		s_Stage.setResizable(false);
		s_Stage.setScene(s_Scene);
 
		s_Stage.show();
	}
	
	@FXML
	public void LoginAction(ActionEvent e){
		
		tempuser = userRepository.findByAccountAndPassword(UserNameText.getText(), UserPasswordText.getText());

		if(tempuser != null){
			UserPasswordText.setText(null);
			s_Stage.close();
			s_Stage = null;
			
			userSession.setUser(tempuser);

			mainContainHandler.startWorkActivity();
		}
		else {
			ButtonType loginButtonType = new ButtonType("È·¶¨", ButtonData.OK_DONE);
			Dialog<String> dialog = new Dialog<>();
			dialog.getDialogPane().setContentText("µÇÂ½Ê§°Ü£¡");
			dialog.getDialogPane().getButtonTypes().add(loginButtonType);
			dialog.showAndWait();
		}
		
		tempuser = null;
	}
	
	@FXML
	public void GB_CloseAction(){
		s_Stage.close();
		System.exit(0);
	}
}

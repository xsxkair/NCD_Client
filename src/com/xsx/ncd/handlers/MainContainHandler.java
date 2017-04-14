package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.svg.SVGGlyph;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.JFXDecorator;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@Component
public class MainContainHandler {
	
	private Stage s_Stage;
	
	private Scene s_Scene;
	AnchorPane root = null;
	
	@FXML AnchorPane GB_HeadPane;
	@FXML AnchorPane GB_RootPane;
	
	private SVGGlyph menuHoverd = null;
	private StackPane selectMenu = null;
	private Image selectMenuImage = null;
	private ImageView selectMenuImageView = null;
	@FXML HBox GB_MenuHBox;
	@FXML StackPane GB_WorkSpaceStackPane;
	@FXML StackPane GB_ReportManagerStackPane;
	@FXML StackPane GB_DeviceStackPane;
	@FXML StackPane GB_CardStackPane;
	@FXML StackPane GB_ToolStackPane;
	@FXML StackPane GB_UsersStackPane;
	@FXML StackPane GB_AboutUsStackPane;
	
	@FXML HBox GB_MyInfoHBox;
	@FXML Text GB_UserNameText;
	@FXML HBox GB_UserOutHBox;
	
	//二级菜单pane
	@FXML FlowPane GB_SecondeItemFlowPane;
	private SimpleObjectProperty<Button> selectSecondMenu = null;

	//报告的二级菜单
	private Button[] reportSecondButton = new Button[2];
	private String[] reportSecondButtonText = {"报告概览", "报告查询"};
	//库存的二级菜单
	private Button[] cardSecondButton = new Button[3];
	private String[] cardSecondButtonText = {"试剂卡库存", "出入库", "出入库记录"};
	//工具的二级菜单
	private Button[] toolSecondButton = new Button[5];
	private String[] toolSecondButtonText = {"连接设备", "实验数据", "二维码生成", "二维码查询", "软件管理"};
	//用户的二级菜单
	private Button[] userSecondButton = new Button[7];
	private String[] userSecondButtonText = {"我的信息", "管理员管理", "销售人员", "研发人员", "审核人管理", "品控人员", "我的人员"};
	
	private User user = null;
	
	private double initX;
    private double initY;
	
	@Autowired private UserSession userSession;
	@Autowired private WorkPageSession workPageSession;
	@Autowired private TodayWorkHandler workSpaceHandler;
	@Autowired private MyInfoHandler managerInfoHandler;
	@Autowired private UserHandler userHandler;
	@Autowired private ChildUserHandler childUserHandler;
	@Autowired private DeviceHandler deviceHandler;
	@Autowired private ReportListHandler reportListHandler;
	@Autowired private ReportOverViewPage reportOverViewPage;
	@Autowired private CardRepertoryHandler cardRepertoryHandler;
	@Autowired private CardInOutHandler cardInOutHandler;
	@Autowired private CardRecordHandler cardRecordHandler;
	@Autowired private QRCodeHandler qrCodeHandler;
	@Autowired private DeviceTestHandler deviceTestHandler;
	@Autowired private SoftHandler softHandler;
	@Autowired private LoginHandler loginHandler;
	@Autowired private AboutUsHandler aboutUsHandler;
	@Autowired private CardListPage cardListPage;
	@Autowired private LabDataListHandler labDataListHandler;
	
	@PostConstruct
	public void UI_Init() {
		int i=0;
		
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
        
        selectSecondMenu = new SimpleObjectProperty<Button>(null);
        selectSecondMenu.addListener((o, oldValue, newValue)->{
        	if(oldValue != null)
        		oldValue.setStyle("-fx-background-image :  url(/RES/buttonun.png)");

        	if(newValue != null){
        		newValue.setStyle("-fx-background-image :  url(/RES/buttonselect.png);");
        		
        		Object userData = newValue.getUserData();
        		if(userData instanceof Object[]){
        			Object[] usero = (Object[]) userData;
        			((HandlerTemplet)usero[0] ).showPane(usero[1]);
        		}
        		else {
        			((HandlerTemplet)userData ).showPane();
				}
        	}
        });
        
        for (i=0; i<reportSecondButton.length; i++) {
        	reportSecondButton[i] = new Button(reportSecondButtonText[i]);
        	reportSecondButton[i].setPrefSize(84, 27);
        	reportSecondButton[i].setStyle("-fx-background-image :  url(/RES/buttonun.png);");
        	if(i == 0)
        		reportSecondButton[i].setUserData(reportOverViewPage);
        	else if (i == 1) {
        		reportSecondButton[i].setUserData(reportListHandler);
			}
        	
        	reportSecondButton[i].setOnAction((e)->{
        		selectSecondMenu.set((Button) e.getSource());
        	});
        	
        	reportSecondButton[i].setOnMouseEntered((e)->{
        		if(!e.getSource().equals(selectSecondMenu.get())){
        			((Button) e.getSource()).setCursor(Cursor.HAND);
        			((Button) e.getSource()).setStyle("-fx-background-image :  url(/RES/buttonhover.png);");
        		}
        	});
        	
        	reportSecondButton[i].setOnMouseExited((e)->{
        		if(!e.getSource().equals(selectSecondMenu.get())){
        			((Button) e.getSource()).setCursor(Cursor.DEFAULT);
        			((Button) e.getSource()).setStyle("-fx-background-image :  url(/RES/buttonun.png);");
        		}
        	});
		}
        
        for (i=0; i<cardSecondButton.length; i++) {
        	cardSecondButton[i] = new Button(cardSecondButtonText[i]);
        	cardSecondButton[i].setStyle("-fx-background-image :  url(/RES/buttonun.png);");
        	cardSecondButton[i].setPrefSize(84, 27);
        	if(i == 0)
        		cardSecondButton[i].setUserData(cardRepertoryHandler);
        	else if (i == 1) {
        		cardSecondButton[i].setUserData(cardInOutHandler);
			}
        	else if (i == 2) {
        		cardSecondButton[i].setUserData(cardRecordHandler);
			}
        	
        	cardSecondButton[i].setOnAction((e)->{
        		selectSecondMenu.set((Button) e.getSource());
        	});
        	
        	cardSecondButton[i].setOnMouseEntered((e)->{
        		if(!e.getSource().equals(selectSecondMenu.get())){
        			((Button) e.getSource()).setCursor(Cursor.HAND);
        			((Button) e.getSource()).setStyle("-fx-background-image :  url(/RES/buttonhover.png);");
        		}
        	});
        	
        	cardSecondButton[i].setOnMouseExited((e)->{
        		if(!e.getSource().equals(selectSecondMenu.get())){
        			((Button) e.getSource()).setCursor(Cursor.DEFAULT);
        			((Button) e.getSource()).setStyle("-fx-background-image :  url(/RES/buttonun.png);");
        		}
        	});
		}
        
        for (i=0; i<toolSecondButton.length; i++) {
        	toolSecondButton[i] = new Button(toolSecondButtonText[i]);
        	toolSecondButton[i].setStyle("-fx-background-image :  url(/RES/buttonun.png);");
        	toolSecondButton[i].setPrefSize(84, 27);
        	
        	if(i == 0)
        		toolSecondButton[i].setUserData(deviceTestHandler);
        	else if (i == 1)
        		toolSecondButton[i].setUserData(labDataListHandler);
        	else if (i == 2)
        		toolSecondButton[i].setUserData(new Object[]{qrCodeHandler, new Integer(-1)});
        	else if(i == 3)
        		toolSecondButton[i].setUserData(cardListPage);
        	else if (i == 4)
        		toolSecondButton[i].setUserData(softHandler);
        	
        	toolSecondButton[i].setOnAction((e)->{
        		selectSecondMenu.set((Button) e.getSource());
        	});
        	
        	toolSecondButton[i].setOnMouseEntered((e)->{
        		if(!e.getSource().equals(selectSecondMenu.get())){
        			((Button) e.getSource()).setCursor(Cursor.HAND);
        			((Button) e.getSource()).setStyle("-fx-background-image :  url(/RES/buttonhover.png);");
        		}
        	});
        	
        	toolSecondButton[i].setOnMouseExited((e)->{
        		if(!e.getSource().equals(selectSecondMenu.get())){
        			((Button) e.getSource()).setCursor(Cursor.DEFAULT);
        			((Button) e.getSource()).setStyle("-fx-background-image :  url(/RES/buttonun.png);");
        		}
        	});
		}
        
        for (i=0; i<userSecondButton.length; i++) {
        	userSecondButton[i] = new Button(userSecondButtonText[i]);
        	userSecondButton[i].setStyle("-fx-background-image :  url(/RES/buttonun.png);");
        	userSecondButton[i].setPrefSize(84, 27);

        	if(i == 0)
        		userSecondButton[i].setUserData(managerInfoHandler);
        	else if (i == 6)
        		userSecondButton[i].setUserData(childUserHandler);
        	else if (i == 1)
        		userSecondButton[i].setUserData(new Object[]{userHandler, new Integer(1)});
        	else if(i == 2)
        		userSecondButton[i].setUserData(new Object[]{userHandler, new Integer(2)});
        	else if (i == 3)
        		userSecondButton[i].setUserData(new Object[]{userHandler, new Integer(3)});
        	else if (i == 4)
        		userSecondButton[i].setUserData(new Object[]{userHandler, new Integer(4)});
        	else if (i == 5)
        		userSecondButton[i].setUserData(new Object[]{userHandler, new Integer(5)});
        	
        	userSecondButton[i].setOnAction((e)->{
        		selectSecondMenu.set((Button) e.getSource());
        	});
        	
        	userSecondButton[i].setOnMouseEntered((e)->{
        		if(!e.getSource().equals(selectSecondMenu.get())){
        			((Button) e.getSource()).setCursor(Cursor.HAND);
        			((Button) e.getSource()).setStyle("-fx-background-image :  url(/RES/buttonhover.png);");
        		}
        	});
        	
        	userSecondButton[i].setOnMouseExited((e)->{
        		if(!e.getSource().equals(selectSecondMenu.get())){
        			((Button) e.getSource()).setCursor(Cursor.DEFAULT);
        			((Button) e.getSource()).setStyle("-fx-background-image :  url(/RES/buttonun.png);");
        		}
        	});
		}
        
        GB_WorkSpaceStackPane.setUserData(workSpaceHandler);
        GB_ReportManagerStackPane.setUserData(reportSecondButton);
    	GB_DeviceStackPane.setUserData(deviceHandler);
    	GB_CardStackPane.setUserData(cardSecondButton);
    	GB_ToolStackPane.setUserData(toolSecondButton);
    	GB_UsersStackPane.setUserData(userSecondButton);
    	GB_AboutUsStackPane.setUserData(aboutUsHandler);
        
        menuHoverd = new SVGGlyph(0, "FULLSCREEN", "M0 0 L30 0 L30 1 L1 1 L1 30 L0 30 ZM48 0 L78 0 L78 30 L77 30 L77 1 L48 1 ZM78 48 L78 78 L48 78 L48 77 L77 77 L77 48 Z M0 48 L0 78 L30 78 L30 77 L1 77 L1 48 Z", Color.WHITE);		
        menuHoverd.setSize(78, 78);
        menuHoverd.setFill(Color.AQUA);
        
        selectMenuImage = new Image(this.getClass().getResourceAsStream("/RES/select.png"));
        selectMenuImageView = new ImageView(selectMenuImage);
        
        for (Node menus : GB_MenuHBox.getChildren()) {
        	StackPane tempS = (StackPane) menus;
        	
        	tempS.setOnMouseClicked((e)->{
        		System.out.println("xsx");
        		if(!tempS.equals(selectMenu)){
        			Object userData = tempS.getUserData();
            		
            		selectMenu = tempS;
            		tempS.getChildren().add(selectMenuImageView);
            		
            		if(userData instanceof HandlerTemplet){
            			GB_SecondeItemFlowPane.getChildren().clear();
            			((HandlerTemplet) userData).showPane();
            			selectSecondMenu.set(null);
            		}
            		else if(userData instanceof Button[]){
            			GB_SecondeItemFlowPane.getChildren().setAll(((Button[])userData));
            			((Button[])userData)[0].fire();
            		}
        		}
        	});
        	
        	tempS.setOnMouseEntered((e)->{
        		if(!tempS.equals(selectMenu)){
        			tempS.getChildren().add(menuHoverd);
        			tempS.setCursor(Cursor.HAND);
        		}
        	});
        	
        	tempS.setOnMouseExited((e)->{
        		if(tempS.getChildren().contains(menuHoverd)){
        			tempS.getChildren().remove(menuHoverd);
        			tempS.setCursor(Cursor.DEFAULT);
        		}
        	});
		}
        
        GB_MyInfoHBox.setOnMouseClicked((e)->{
        	GB_UsersStackPane.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0.0, 0.0, 0.0, 0.0, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, e.getPickResult()));
        });
        
        GB_UserOutHBox.setOnMouseClicked((e)->{
        	userSession.setUser(null);
    		workPageSession.setWorkPane(null);
    		s_Stage.close();
    		loginHandler.startLoginActivity();
        });
        
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

        GB_HeadPane.setOnMousePressed((e)-> {

            initX = e.getScreenX() - s_Stage.getX();

            initY = e.getScreenY() - s_Stage.getY();
        });

        GB_HeadPane.setOnMouseDragged((e)->{

        	if(s_Stage.isFullScreen()){
        		return;
        	}
            s_Stage.setX(e.getScreenX() - initX);

            s_Stage.setY(e.getScreenY() - initY);
        });
        
        GB_HeadPane.setOnMouseClicked((e)->{
        	if(e.getClickCount() == 2){
        		JFXDecorator decorator = (JFXDecorator) s_Scene.getRoot();
        		decorator.setMaxSize();
        	}
        });
        
		loader = null;
        in = null;
	}
	
	public void startWorkActivity() {
		user = userSession.getUser();

		GB_UserNameText.setText(user.getName());
		
		//数据库管理员
		if(user.getType().equals(0)){
			GB_WorkSpaceStackPane.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0.0, 0.0, 0.0, 0.0, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, new PickResult(GB_WorkSpaceStackPane, 0, 0)));
		}
		//超级管理员
		else if(user.getType().equals(1)){

			//进入报告查询界面
			workSpaceHandler.showPane();
		}
		//销售
		else if(user.getType().equals(2)){

			//进入报告查询界面
			deviceHandler.showPane();
		}
		//生物研发
		else if(user.getType().equals(3)){

			//进入报告查询界面
			workSpaceHandler.showPane();
		}
		//一级审核人
		else if(user.getType().equals(4)){
			
			//进入报告查询界面
			workSpaceHandler.showPane();
		}
		//品控
		else if(user.getType().equals(5)){
			
			//进入报告查询界面
			workSpaceHandler.showPane();
		}
		
		user = null;
		
		s_Stage = new Stage();

		JFXDecorator decorator = new JFXDecorator(s_Stage, root);
		decorator.setCustomMaximize(true);
		decorator.setOnCloseButtonAction(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		
		s_Scene = new Scene(decorator, 1024, 800);
		s_Stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
	    s_Stage.setScene(s_Scene);
	    s_Stage.setMinWidth(1024);
	    s_Stage.setMinHeight(800);
		s_Stage.show();
	}
	
}

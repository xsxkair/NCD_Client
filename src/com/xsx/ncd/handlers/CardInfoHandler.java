package com.xsx.ncd.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.CRC16;
import com.xsx.ncd.tool.DescyTool;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;

@Component
public class CardInfoHandler implements HandlerTemplet{

	private AnchorPane rootPane = null;
	private Pane fatherPane = null;
	
	@FXML StackPane rootStackPane;
	
	@FXML StackPane GB_FreshPane;
	@FXML JFXProgressBar GB_FreshProgressBar;
	
	@FXML Label GB_PihaoLabel;
	@FXML Label GB_ItemLabel;
	@FXML Label GB_PointLabel;
	@FXML Label GB_DanweiLabel;
	@FXML Label GB_LowestLabel;
	@FXML Label GB_highstLabel;
	@FXML Label GB_NormalLabel;
	@FXML Label GB_ChannelLabel;
	@FXML Label GB_TLocationLabel;
	@FXML Label GB_CLocationLabel;
	@FXML Label GB_WaittimeLabel;
	@FXML Label GB_OuttimeLabel;
	@FXML Label GB_PeakLabel1;
	@FXML Label GB_PeakLabel2;
	@FXML Label GB_Qu1ALabel;
	@FXML Label GB_Qu1BLabel;
	@FXML Label GB_Qu1CLabel;
	@FXML Label GB_Qu2ALabel;
	@FXML Label GB_Qu2BLabel;
	@FXML Label GB_Qu2CLabel;
	@FXML Label GB_Qu3ALabel;
	@FXML Label GB_Qu3BLabel;
	@FXML Label GB_Qu3CLabel;
	
	@FXML Label GB_UpInfoLabel;
	@FXML Label GB_ManageInfoLabel;
	
	@FXML HBox GB_ManageHBox;
	@FXML Button GB_CardOKButton;
	@FXML Button GB_CardErrorButton;
	
	@FXML JFXDialog UserDialog;
	@FXML JFXPasswordField adminPasswordTextField;
	@FXML JFXButton acceptButton;
	
	@FXML JFXDialog logDialog;
	@FXML Label dialogInfo;
	
	@FXML JFXDialog numDialog;
	@FXML JFXTextField OutNumTextField;
	@FXML JFXButton acceptButton1;
	
	ContextMenu myContextMenu;
	MenuItem myMenuItem1;
	
	private Integer cardId = null;
	private Card card = null;
	private User user;
	private StringBuffer stringBuffer = null;
	private MakeQRCodeService makeQRCodeService;
	private String savePath = null;
	private Integer makeNum = null;
	private StringBuffer stringBuffer1 = null;
	private StringBuffer stringBuffer2 = null;
	private StringBuffer stringBuffer3 = null;
	
	@Autowired WorkPageSession workPageSession;
	@Autowired CardRepository cardRepository;
	@Autowired UserRepository userRepository;
	@Autowired UserSession userSession;
	
	@PostConstruct
	@Override
	public void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/CardInfoPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/CardInfoPage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
        rootStackPane.getChildren().remove(UserDialog);
        rootStackPane.getChildren().remove(logDialog);
        rootStackPane.getChildren().remove(numDialog);
        
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootPane.equals(newValue)){
					fatherPane = oldValue;
					card = cardRepository.findOne(cardId);
					showCardInfo();
					
					makeQRCodeService = new MakeQRCodeService();
					
					GB_FreshPane.visibleProperty().bind(makeQRCodeService.runningProperty());
					GB_FreshProgressBar.progressProperty().bind(makeQRCodeService.progressProperty());
				}
				else {
					makeQRCodeService = null;
					GB_FreshPane.visibleProperty().unbind();
					GB_FreshProgressBar.progressProperty().unbind();
				}
			}
		});
        acceptButton.disableProperty().bind(new BooleanBinding() {
			{
				bind(adminPasswordTextField.lengthProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				
				if(adminPasswordTextField.getLength() > 0)
					return false;
				else
					return true;
			}
		});
        
        acceptButton1.disableProperty().bind(new BooleanBinding() {
			{
				bind(OutNumTextField.lengthProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub

				try {
					makeNum = Integer.valueOf(OutNumTextField.getText());
				} catch (Exception e) {
					// TODO: handle exception
					makeNum = null;
				}
				
				
				if(makeNum != null)
					return false;
				else
					return true;
			}
		});
        
        myMenuItem1 = new MenuItem("返回");
        myContextMenu = new ContextMenu();
        myContextMenu.getItems().add(myMenuItem1);
        
        //返回
        myMenuItem1.setOnAction(new EventHandler<ActionEvent>() {

  			@Override
  			public void handle(ActionEvent event) {
  				// TODO Auto-generated method stub
  				workPageSession.setWorkPane(fatherPane);
  			}
  		});	
        
        rootPane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getButton().equals(MouseButton.SECONDARY)){
					myContextMenu.show(rootPane, arg0.getScreenX(), arg0.getScreenY());
				}
			}
		});
        
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}
	
	@Override
	public void showPane(Object cardId) {
		this.cardId = ((Integer)cardId).intValue();
		workPageSession.setWorkPane(rootPane);
	}
	
	private void showCardInfo(){
		stringBuffer = new StringBuffer();
		
		if(card != null){
			GB_PihaoLabel.setText(card.getCid());
			GB_ItemLabel.setText(card.getItem());
			GB_PointLabel.setText(card.getXsd().toString());
			GB_DanweiLabel.setText(card.getDanwei());
			GB_LowestLabel.setText(card.getLow().toString());
			GB_highstLabel.setText(card.getHigh().toString());
			GB_NormalLabel.setText(card.getNormal());
			GB_ChannelLabel.setText(card.getChannel().toString());
			GB_TLocationLabel.setText(card.getT_l().toString());
			GB_CLocationLabel.setText(card.getC_l().toString());
			GB_WaittimeLabel.setText(card.getWaitt().toString());
			GB_OuttimeLabel.setText(card.getOutdate().toString());
			GB_PeakLabel1.setText(card.getFend1().toString());
			GB_PeakLabel2.setText(card.getFend2().toString());
			GB_Qu1ALabel.setText((card.getQu1_a() == null)?"0":card.getQu1_a().toString());
			GB_Qu1BLabel.setText((card.getQu1_b() == null)?"0":card.getQu1_b().toString());
			GB_Qu1CLabel.setText((card.getQu1_c() == null)?"0":card.getQu1_c().toString());
			GB_Qu2ALabel.setText((card.getQu2_a() == null)?"0":card.getQu2_a().toString());
			GB_Qu2BLabel.setText((card.getQu2_b() == null)?"0":card.getQu2_b().toString());
			GB_Qu2CLabel.setText((card.getQu2_c() == null)?"0":card.getQu2_c().toString());
			GB_Qu3ALabel.setText((card.getQu3_a() == null)?"0":card.getQu3_a().toString());
			GB_Qu3BLabel.setText((card.getQu3_b() == null)?"0":card.getQu3_b().toString());
			GB_Qu3CLabel.setText((card.getQu3_c() == null)?"0":card.getQu3_c().toString());
			
			stringBuffer.append(card.getMaker());
			stringBuffer.append("-----");
			stringBuffer.append(card.getUptime().toString());
			GB_UpInfoLabel.setText(stringBuffer.toString());
			
			stringBuffer.setLength(0);
			stringBuffer.append(card.getMstatus());
			stringBuffer.append("-----( ");
			stringBuffer.append(((card.getManager() == null)?"无":card.getManager().toString()));
			stringBuffer.append(", ");
			stringBuffer.append(((card.getManagetime() == null)?"无":card.getManagetime().toString()) );
			stringBuffer.append(" )");
			GB_ManageInfoLabel.setText(stringBuffer.toString());
		}
		else{
			showLog("Error");
		}
		
		if(userSession.getFatherAccount() != null){
			GB_CardOKButton.setVisible(false);
			GB_CardErrorButton.setVisible(false);
		}
		else{
			GB_CardOKButton.setVisible(true);
			GB_CardErrorButton.setVisible(true);
		}
		
		stringBuffer = null;
	}
	
	private void showLog(String log) {
		dialogInfo.setText(log);
		logDialog.show(rootStackPane);
	}
	
	@FXML
	public void GB_CardOKAction(){
		card.setMstatus("合格");
		adminPasswordTextField.clear();
		UserDialog.show(rootStackPane);
	}
	
	@FXML
	public void GB_CardErrorAction(){
		card.setMstatus("不合格");
		adminPasswordTextField.clear();
		UserDialog.show(rootStackPane);
	}
	
	@FXML
	public void GB_CardOutAction(){
		if(card.getMstatus().equals("合格")){
			OutNumTextField.clear();
			numDialog.show(rootStackPane);
		}
		else {
			showLog("禁止！");
		}
	}
	
	@FXML
	public void ConfirmAction1(){
		DirectoryChooser dirchoose = new DirectoryChooser();
		dirchoose.setTitle("二维码生成文件夹");
		
		File selectedFile = dirchoose.showDialog(null);
		 
		if(selectedFile != null){
			savePath = selectedFile.getPath();
			makeQRCodeService.restart();
		 }
		
		numDialog.close();
	}
	
	@FXML
	public void CancelAction(){
		if(UserDialog.isVisible())
			UserDialog.close();
		
		if(logDialog.isVisible())
			logDialog.close();
		
		if(numDialog.isVisible())
			numDialog.close();
	}
	
	@FXML
	public void ConfirmAction(){
		user = userRepository.findByAccountAndPassword(userSession.getAccount(), adminPasswordTextField.getText());
		if(user != null){
			card.setManager(user.getName());
			card.setManagetime(new Timestamp(System.currentTimeMillis()));
			cardRepository.save(card);
		}
		else
			showLog("密码错误！");
		
		adminPasswordTextField.clear();
		
		UserDialog.close();
		
		showCardInfo();
	}
	
class MakeQRCodeService extends Service<Void>{
		
		@Override
		protected Task<Void> createTask() {
			// TODO Auto-generated method stub
			return new MakeQRCodeTask();
		}
		
		class MakeQRCodeTask extends Task<Void>{

			@Override
			protected Void call(){
				stringBuffer1 = new StringBuffer();
				stringBuffer2 = new StringBuffer();
				stringBuffer3 = new StringBuffer();
				int crc = 0;
	        	SimpleDateFormat matter = new SimpleDateFormat( "yyMMdd");
	        	
	        	try {
	        		//创建文本保存二维码数据
		        	File file = new File(savePath+"/"+card.getCid()+".txt");
		        	FileWriter writer = null;
		        	
		        	if (!file.exists()){
		        		file.createNewFile();
		        	}
		        	writer = new FileWriter(file);

		        	//组合二维码固定数据	    			
	        		stringBuffer1.append(card.getItem());
	        		stringBuffer1.append('#');
	        		stringBuffer1.append(card.getChannel());
	        		stringBuffer1.append('#');
	        		stringBuffer1.append(card.getT_l());
	        		stringBuffer1.append('#');
	        		stringBuffer1.append(card.getFend1());
	        		stringBuffer1.append('#');
	        		stringBuffer1.append(card.getFend2());
	        		stringBuffer1.append('#');
	    			
	        		stringBuffer1.append(card.getQu1_a());
	        		stringBuffer1.append('#');
	        		stringBuffer1.append(card.getQu1_b());
	        		stringBuffer1.append('#');
	        		stringBuffer1.append(card.getQu1_c());
	        		stringBuffer1.append('#');

	    			if(card.getFend1().floatValue() > 0){
	    				stringBuffer1.append(card.getQu2_a());
	    				stringBuffer1.append('#');
	    				stringBuffer1.append(card.getQu2_b());
	    				stringBuffer1.append('#');
	    				stringBuffer1.append(card.getQu2_c());
	    				stringBuffer1.append('#');
	    			}
	    			
	    			if(card.getFend2().floatValue() > 0){
	    				stringBuffer1.append(card.getQu3_a());
	    				stringBuffer1.append('#');
		    			stringBuffer1.append(card.getQu3_b());
		    			stringBuffer1.append('#');
		    			stringBuffer1.append(card.getQu3_c());
		    			stringBuffer1.append('#');
	    			}
	    			
	    			stringBuffer1.append(card.getWaitt());
	    			stringBuffer1.append('#');
	    			stringBuffer1.append(card.getC_l());
	    			stringBuffer1.append('#');
	    			stringBuffer1.append(card.getCid());
	    			stringBuffer1.append('#');
	    			
		        	for(int i=0; i<makeNum.intValue(); i++){
		        		stringBuffer2.setLength(0);
		        		stringBuffer2.append(stringBuffer1);
		    			stringBuffer2.append(String .format("%05d",i));
		    			stringBuffer2.append('#');
		    			stringBuffer2.append(matter.format(card.getOutdate()));
		    			stringBuffer2.append('#');
		    			
		    			crc = CRC16.CalCRC16(stringBuffer2.toString().getBytes(), stringBuffer2.length());
		    			stringBuffer2.append(crc);

		    			stringBuffer3.setLength(0);
		    			stringBuffer3.append(DescyTool.Des(stringBuffer2.toString()));
		    			stringBuffer3.append("\r\n");
		        		
		        		writer.write(stringBuffer3.toString());
						writer.flush();
		        		
		        		updateProgress(i, makeNum.intValue());
		        		try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
		            
		        	writer.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
	        	
	        	stringBuffer1 = null;
	        	stringBuffer2 = null;
	        	stringBuffer3 = null;
	        	
				return null;
			}

		}
	}

@Override
public void showPane() {
	// TODO Auto-generated method stub
	
}
}

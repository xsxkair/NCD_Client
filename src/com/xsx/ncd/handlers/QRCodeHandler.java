package com.xsx.ncd.handlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import com.xsx.ncd.define.CardConstInfo;
import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.handlers.ReportListHandler.QueryReportService.QueryReportTask;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.CRC16;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Component
public class QRCodeHandler {
	
	private AnchorPane rootPane = null;
	
	@FXML private StackPane rootStackPane;
	
	//告警信息
	@FXML JFXDialog logDialog;
	@FXML Label dialogInfo;
	
	//密码确认
	@FXML JFXDialog UserDialog;
	@FXML JFXPasswordField adminPasswordTextField;
	@FXML JFXButton acceptButton;
		
	@FXML TextField GB_PihaoField;
	
	@FXML
	JFXComboBox<String> GB_ItemNameCom;
	
	@FXML
	JFXComboBox<Integer> GB_ChannelCom;
	
	@FXML
	TextField GB_TLocationField;
	
	@FXML
	TextField GB_WaitTimeField;
	
	@FXML
	TextField GB_CLocation;
	
	@FXML JFXDatePicker GB_OutDatePick;
	
	@FXML TextField GB_FenDuan1Field;
	@FXML TextField GB_FenDuan2Field;
	
	@FXML HBox GB_BQ1Hbox;
	@FXML TextField GB_BQ1AField;
	@FXML TextField GB_BQ1BField;
	@FXML TextField GB_BQ1CField;
	
	@FXML HBox GB_BQ2Hbox;
	@FXML TextField GB_BQ2AField;
	@FXML TextField GB_BQ2BField;
	@FXML TextField GB_BQ2CField;
	
	@FXML HBox GB_BQ3Hbox;
	@FXML TextField GB_BQ3AField;
	@FXML TextField GB_BQ3BField;
	@FXML TextField GB_BQ3CField;
	
	@FXML TextField GB_QRNumField;
	
	@FXML Button GB_MakeQRCodeButton;
	
	@FXML VBox GB_FreshPane;
	@FXML private JFXProgressBar GB_Spinner;
	
	@FXML private TextArea GB_BasicText;
	@FXML private TextArea GB_LogsArea;
	
	private MakeQRCodeService makeQRCodeService;
	private String savePath = null;
	private Card card;
	private Boolean upLoadCardInfo = false;
	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private CardRepository cardRepository;
	@Autowired private CardConstInfo NT_proBNPConstInfo;
	@Autowired private CardConstInfo CK_MBConstInfo;
	@Autowired private CardConstInfo cTnIConstInfo;
	@Autowired private CardConstInfo MyoConstInfo;
	@Autowired private UserRepository userRepository;
	@Autowired private UserSession userSession;
	
	@PostConstruct
	public void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/QRCodePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/QRCodePage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        rootStackPane.getChildren().remove(UserDialog);
        rootStackPane.getChildren().remove(logDialog);
        
        makeQRCodeService = new MakeQRCodeService();
        GB_FreshPane.visibleProperty().bind(makeQRCodeService.runningProperty());
        GB_Spinner.progressProperty().bind(makeQRCodeService.progressProperty());
        
        GB_ItemNameCom.getItems().addAll("NT-proBNP", "CK-MB", "cTnI", "Myo");
        GB_ChannelCom.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7);
        
        card = new Card();
        
        GB_BQ2Hbox.disableProperty().bind(new BooleanBinding() {
			{
				bind(GB_FenDuan1Field.lengthProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				Float fend = null;
				
				try {
					fend = Float.valueOf(GB_FenDuan1Field.getText());
				} catch (Exception e) {
					// TODO: handle exception
					fend = null;
				}
				
				if(fend == null)
					return true;
				else if(fend.floatValue() <= 0)
					return true;
				else
					return false;
			}
		});
		
		GB_BQ3Hbox.disableProperty().bind(new BooleanBinding() {
			{
				bind(GB_FenDuan2Field.textProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				Float fend = null;
				
				try {
					fend = Float.valueOf(GB_FenDuan2Field.getText());
				} catch (Exception e) {
					// TODO: handle exception
					fend = null;
				}
				
				if(fend == null)
					return true;
				else if(fend.floatValue() <= 0)
					return true;
				else
					return false;
			}
		});
		
		GB_MakeQRCodeButton.disableProperty().bind(new BooleanBinding() {
			{
				bind(GB_QRNumField.textProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				Integer temp = null;
				
				try {
					temp = Integer.valueOf(GB_QRNumField.getText());
				} catch (NumberFormatException e) {
					// TODO: handle exception
					temp = null;
				}
				
				if(temp != null)
					return false;
				else
					return true;
			}
		});
	}

	public void showQRCodePage(){
		workPageSession.setWorkPane(rootPane);
	}
	
	private Boolean combinContent() {
		
		CardConstInfo cardConstInfo = null;
		
		try{
			card.setCid(GB_PihaoField.getText());
			card.setItem(GB_ItemNameCom.getSelectionModel().getSelectedItem());
			card.setChannel(GB_ChannelCom.getSelectionModel().getSelectedItem());
			card.setT_l(Integer.valueOf(GB_TLocationField.getText()));
			card.setC_l(Integer.valueOf(GB_CLocation.getText()));
			card.setWaitt(Integer.valueOf(GB_WaitTimeField.getText()));
			card.setOutdate(java.sql.Date.valueOf(GB_OutDatePick.getValue()));
			card.setFend1(Float.valueOf(GB_FenDuan1Field.getText()));
			card.setFend2(Float.valueOf(GB_FenDuan2Field.getText()));
			card.setQu1_a(Float.valueOf(GB_BQ1AField.getText()));
			card.setQu1_b(Float.valueOf(GB_BQ1BField.getText()));
			card.setQu1_c(Float.valueOf(GB_BQ1CField.getText()));
			card.setQu2_a(Float.valueOf(GB_BQ2AField.getText()));
			card.setQu2_b(Float.valueOf(GB_BQ2BField.getText()));
			card.setQu2_c(Float.valueOf(GB_BQ2CField.getText()));
			card.setQu3_a(Float.valueOf(GB_BQ3AField.getText()));
			card.setQu3_b(Float.valueOf(GB_BQ3BField.getText()));
			card.setQu3_c(Float.valueOf(GB_BQ3CField.getText()));
			
			if("NT-proBNP".equals(card.getItem()))
				cardConstInfo = NT_proBNPConstInfo;
			else if("CK-MB".equals(card.getItem()))
				cardConstInfo = CK_MBConstInfo;
			else if("cTnI".equals(card.getItem()))
				cardConstInfo = cTnIConstInfo;
			else if("Myo".equals(card.getItem()))
				cardConstInfo = MyoConstInfo;
			
			card.setLow(cardConstInfo.getLowestresult());
			card.setHigh(cardConstInfo.getHighestresult());
			card.setXsd(cardConstInfo.getPointnum());
			card.setNormal(cardConstInfo.getNormalresult());
			card.setDanwei(cardConstInfo.getMeasure());
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
			showLogsDialog("Error");
			return false;
		}
	}
	
	private void showLogsDialog(String logs) {
		dialogInfo.setText(logs);
		logDialog.show(rootStackPane);
	}
	
	@FXML
	public void GB_MakeQRCodeAction(){
		if(combinContent()){
			DirectoryChooser dirchoose = new DirectoryChooser();
			dirchoose.setTitle("二维码生成文件夹");
			
			File selectedFile = dirchoose.showDialog(null);
			 
			if(selectedFile != null){
				savePath = selectedFile.getPath();
				
				adminPasswordTextField.clear();
				UserDialog.show(rootStackPane);
			 }
		}
	}
	
	@FXML
	public void ConfirmAction(){
		
		User admin = userRepository.findByAccount(userSession.getAccount());
		if(!admin.getPassword().equals(adminPasswordTextField.getText())){
			showLogsDialog("密码错误，无此权限！");
			return;
		}
		
		try {
			cardRepository.save(card);

			makeQRCodeService.restart();
			
			UserDialog.close();
		} catch (Exception e) {
			// TODO: handle exception
			showLogsDialog("数据上传失败，禁止生成！");
			return;
		}
	}
	
	@FXML
	public void CancelAction(){
		if(logDialog.isVisible())
			logDialog.close();
		
		if(UserDialog.isVisible())
			UserDialog.close();
	}
	
	@FXML
	public void GB_PreSeeQRCodeAction(){
		StringBuffer content = new StringBuffer();
		 SimpleDateFormat matter = new SimpleDateFormat( "yyMMdd");
		if(combinContent()){
			content.delete(0, content.length());
			
			content.append(card.getItem()+"#"+card.getChannel()+"#"+card.getT_l()+"#"+
					card.getFend1()+"#"+card.getFend2()+"#");
			
			content.append(card.getQu1_a()+"#"+card.getQu1_b()+"#"+card.getQu1_c()+"#");

			if(card.getFend1().floatValue() > 0)
				content.append(card.getQu2_a()+"#"+card.getQu2_b()+"#"+card.getQu2_c()+"#");
			
			if(card.getFend2().floatValue() > 0)
				content.append(card.getQu3_a()+"#"+card.getQu3_b()+"#"+card.getQu3_c()+"#");
			
			content.append(card.getWaitt()+"#"+card.getC_l()+"#"+card.getCid()+"#"+String .format("%05d",1)+
					"#");
			
			String date = matter.format(card.getOutdate());
			content.append(date+"#");
			
			content.append(String.valueOf(CRC16.CalCRC16(content.toString().getBytes(), content.length())));
			
			GB_BasicText.setText(content.toString());
		}
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
				return null;
			}

		}
	}
}

package com.xsx.ncd.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import com.xsx.ncd.tool.DescyTool;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Component
public class QRCodeHandler implements HandlerTemplet{
	
	private AnchorPane rootPane = null;
	private Pane fatherPane = null;
	@FXML private StackPane rootStackPane;
	
	//告警信息
	@FXML JFXDialog logDialog;
	@FXML Label dialogInfo;
	
	//密码确认
	@FXML JFXDialog UserDialog;
	@FXML JFXPasswordField adminPasswordTextField;
	@FXML JFXButton acceptButton;
		
	@FXML TextField GB_PihaoField;
	@FXML JFXComboBox<String> GB_ItemNameCom;
	@FXML JFXComboBox<Integer> GB_ChannelCom;
	@FXML TextField GB_TLocationField;
	@FXML TextField GB_WaitTimeField;
	@FXML TextField GB_CLocation;
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
	
	@FXML Button GB_MakeQRCodeButton1;
	
	@FXML VBox GB_FreshPane;
	@FXML private JFXProgressBar GB_Spinner;
	
	private SimpleIntegerProperty cardId = null;
	private Card card = null;
	private User admin = null;

	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private CardRepository cardRepository;
	@Autowired private CardConstInfo NT_proBNPConstInfo;
	@Autowired private CardConstInfo CK_MBConstInfo;
	@Autowired private CardConstInfo cTnIConstInfo;
	@Autowired private CardConstInfo MyoConstInfo;
	@Autowired private UserRepository userRepository;
	@Autowired private UserSession userSession;
	
	@PostConstruct
	@Override
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
        
        GB_ItemNameCom.getItems().addAll("NT-proBNP", "CK-MB", "cTnI", "Myo");
        GB_ChannelCom.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7);
        
        cardId = new SimpleIntegerProperty();
        cardId.addListener((o, oldValue, newValue)->{
        	if(newValue.intValue() < 0){
				card = new Card();
				GB_MakeQRCodeButton1.setVisible(false);
        	}
			else{
				card = cardRepository.findOne(newValue.intValue());
				GB_MakeQRCodeButton1.setVisible(true);
			}
			
			if(card == null)
				showLogsDialog("错误！");
			else
				showCardInfo();
        });

        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(rootPane.equals(newValue)){

					fatherPane = oldValue;
				}
				else {
					card = null;
				}
			}
		});
        

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
		
		loader = null;
        in = null;
	}

	@Override
	public void showPane(Object cardId){
		this.cardId.setValue(((Integer)cardId).intValue());
		
		workPageSession.setWorkPane(rootPane);
	}
	
	private void showCardInfo(){

		if(card.getId() != null){
			GB_PihaoField.setText(card.getCid());
			GB_ItemNameCom.getSelectionModel().select(card.getItem());
			GB_ChannelCom.getSelectionModel().select(card.getChannel());
			GB_TLocationField.setText(card.getT_l().toString());
			GB_CLocation.setText(card.getC_l().toString());
			GB_WaitTimeField.setText(card.getWaitt().toString());
			GB_OutDatePick.setValue(card.getOutdate().toLocalDate());
			GB_FenDuan1Field.setText(card.getFend1().toString());
			GB_FenDuan2Field.setText(card.getFend2().toString());
			GB_BQ1AField.setText((card.getQu1_a() == null)?"0":card.getQu1_a().toString());
			GB_BQ1BField.setText((card.getQu1_b() == null)?"0":card.getQu1_b().toString());
			GB_BQ1CField.setText((card.getQu1_c() == null)?"0":card.getQu1_c().toString());
			GB_BQ2AField.setText((card.getQu2_a() == null)?"0":card.getQu2_a().toString());
			GB_BQ2BField.setText((card.getQu2_b() == null)?"0":card.getQu2_b().toString());
			GB_BQ2CField.setText((card.getQu2_c() == null)?"0":card.getQu2_c().toString());
			GB_BQ3AField.setText((card.getQu3_a() == null)?"0":card.getQu3_a().toString());
			GB_BQ3BField.setText((card.getQu3_b() == null)?"0":card.getQu3_b().toString());
			GB_BQ3CField.setText((card.getQu3_c() == null)?"0":card.getQu3_c().toString());
		}
		else{
			GB_PihaoField.clear();
			GB_ItemNameCom.getSelectionModel().select(null);
			GB_ChannelCom.getSelectionModel().select(null);
			GB_TLocationField.clear();
			GB_CLocation.clear();
			GB_WaitTimeField.clear();
			GB_OutDatePick.setValue(null);
			GB_FenDuan1Field.clear();
			GB_FenDuan2Field.clear();
			GB_BQ1AField.clear();
			GB_BQ1BField.clear();
			GB_BQ1CField.clear();
			GB_BQ2AField.clear();
			GB_BQ2BField.clear();
			GB_BQ2CField.clear();
			GB_BQ3AField.clear();
			GB_BQ3BField.clear();
			GB_BQ3CField.clear();
		}
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
			
			if(!GB_BQ2Hbox.isDisabled()){
				card.setQu2_a(Float.valueOf(GB_BQ2AField.getText()));
				card.setQu2_b(Float.valueOf(GB_BQ2BField.getText()));
				card.setQu2_c(Float.valueOf(GB_BQ2CField.getText()));
			}
			
			if(!GB_BQ3Hbox.isDisabled()){
				card.setQu3_a(Float.valueOf(GB_BQ3AField.getText()));
				card.setQu3_b(Float.valueOf(GB_BQ3BField.getText()));
				card.setQu3_c(Float.valueOf(GB_BQ3CField.getText()));
			}
			
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
			
			card.setMaker(userSession.getAccount());
			card.setUptime(new Timestamp(System.currentTimeMillis()));
			
			if(card.getMstatus() == null){
				card.setMstatus("未审核");
			}
			else if (!card.getMstatus().equals("未审核")) {
				showLogsDialog("禁止修改");
				showCardInfo();
				return false;
			}
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
			showLogsDialog(e.getMessage());
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
			adminPasswordTextField.clear();
			UserDialog.show(rootStackPane);
		}
	}
	
	@FXML 
	public void GB_ReturnAction(){
		workPageSession.setWorkPane(fatherPane);
	}
	
	@FXML
	public void ConfirmAction(){
		
		UserDialog.close();
		
		admin = userRepository.findByAccountAndPassword(userSession.getAccount(), adminPasswordTextField.getText());
		if(admin == null){
			showLogsDialog("密码错误，无此权限！");
			return;
		}
		
		//如果不是未审核，说明已经审核过的，不能修改
		if(!card.getMstatus().equals("未审核")){
			showLogsDialog("禁止修改！");
			return;
		}
		
		try {
			cardRepository.save(card);
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

	@Override
	public void showPane() {
		// TODO Auto-generated method stub
		
	}
}

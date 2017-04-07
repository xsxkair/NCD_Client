package com.xsx.ncd.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.CardRecord;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.repository.CardRecordRepository;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.UserRepository;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.spring.WorkPageSession;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

@Component
public class CardInOutHandler implements HandlerTemplet{

	private AnchorPane rootpane;
	
	//入库
	@FXML TextField GB_InPihaoTextField;
	@FXML TextField GB_InNumTextField;
	
	//出库
	@FXML TextField GB_OutPihaoTextField;
	@FXML TextField GB_OutNumTextField;
	@FXML TextField GB_OutUserTextField;
	@FXML ComboBox<Device> GB_OutDeviceComboBox;
	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private CardRecordRepository cardRecordRepository;
	@Autowired private DeviceRepository deviceRepository;
	@Autowired private CardRepository cardRepository;
	@Autowired private UserSession managerSession;
	@Autowired private UserRepository managerRepository;
	
	@PostConstruct
	@Override
	public void UI_Init() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/CardInOutPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/CardInOutPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        workPageSession.getWorkPane().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(newValue == rootpane){
					UpOutDeviceListUI();
				}
			}
		});
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
        
        loader = null;
        in = null;
	}
	
	@Override
	public void showPane() {
		workPageSession.setWorkPane(rootpane);
	}
	
	@FXML
	public void GB_InAction(){
		String string = GB_InNumTextField.getText();
		Integer num = null;
		
		try {
			num = Integer.valueOf(string);
			
			if(num <= 0){
				ShowErrorDialog();
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ShowErrorDialog();
			return;
		}
		
		Card card = cardRepository.findCardByCid(GB_InPihaoTextField.getText());
		if(card != null){
			CardRecord cardRecord = new CardRecord();
			cardRecord.setCid(card.getCid());
			cardRecord.setAccount(managerSession.getAccount());
			cardRecord.setNum(num);
			cardRecord.setDotime(new java.sql.Timestamp(System.currentTimeMillis()));
			
			cardRecord = cardRecordRepository.save(cardRecord);
			
			if(cardRecord == null)
				ShowErrorDialog();
			else{
				ShowSuccessDialog();
				GB_InNumTextField.setText(null);
				GB_InPihaoTextField.setText(null);
			}
		}
		else{
			ShowErrorDialog();
		}
	}
	
	@FXML
	public void GB_OutAction(){
		String string = GB_OutNumTextField.getText();
		Integer num = null;
		
		try {
			num = Integer.valueOf(string);
			
			if(num <= 0){
				ShowErrorDialog();
				return;
			}
			else {
				num = (0 - num);
			}
		} catch (Exception e) {
			// TODO: handle exception
			ShowErrorDialog();
			return;
		}
		
		Device device = GB_OutDeviceComboBox.getSelectionModel().getSelectedItem();
		if(device == null){
			ShowErrorDialog();
			return;
		}
		
		Card card = cardRepository.findCardByCid(GB_OutPihaoTextField.getText());
		if(card != null){
			CardRecord cardRecord = new CardRecord();
			cardRecord.setCid(card.getCid());
			cardRecord.setAccount(managerSession.getAccount());
			cardRecord.setNum(num);
			cardRecord.setDotime(new java.sql.Timestamp(System.currentTimeMillis()));
			cardRecord.setName(GB_OutUserTextField.getText());
			cardRecord.setDid(device.getDid());
			
			cardRecord = cardRecordRepository.save(cardRecord);
			
			if(cardRecord == null)
				ShowErrorDialog();
			else{
				ShowSuccessDialog();
				GB_OutUserTextField.setText(null);
				GB_OutNumTextField.setText(null);
				GB_OutPihaoTextField.setText(null);
				GB_OutDeviceComboBox.getSelectionModel().select(null);
			}
		}
		else{
			ShowErrorDialog();
		}
	}
	
	private void UpOutDeviceListUI() {
		
		List<Device> devices = new ArrayList<>();
		User admin = null;
		
		if(managerSession.getFatherAccount() == null)
			admin = managerRepository.findByAccount(managerSession.getAccount());
		else
			admin = managerRepository.findByAccount(managerSession.getFatherAccount());
		
		devices = deviceRepository.findByAccount(admin.getAccount());
		
		GB_OutDeviceComboBox.getItems().clear();
		GB_OutDeviceComboBox.getItems().addAll(devices);
	}

	private void ShowErrorDialog() {
		ButtonType loginButtonType = new ButtonType("确定", ButtonData.OK_DONE);
		Dialog<String> dialog = new Dialog<>();
		dialog.getDialogPane().setContentText("操作失败，请检查数据！");
		dialog.getDialogPane().getButtonTypes().add(loginButtonType);
		dialog.showAndWait();
	}
	
	private void ShowSuccessDialog() {
		ButtonType loginButtonType = new ButtonType("确定", ButtonData.OK_DONE);
		Dialog<String> dialog = new Dialog<>();
		dialog.getDialogPane().setContentText("操作成功！");
		dialog.getDialogPane().getButtonTypes().add(loginButtonType);
		dialog.showAndWait();
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

package org.xsx.ncd.UI.MainScene.CardPage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.com.xsx.Dao.CardRecordDao;
import org.com.xsx.Dao.ManagerDao;
import org.com.xsx.Data.SignedManager;
import org.com.xsx.Data.UIMainPage;
import org.com.xsx.Domain.CardRecordBean;
import org.com.xsx.Domain.ManagerBean;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

public class CardInOutPage {
	
	private static CardInOutPage S_CardInOutPage = null;
	
	private AnchorPane rootpane;
	
	//入库
	@FXML
	TextField GB_InItemNameTextField;
	@FXML
	TextField GB_InItemNumTextField;
	@FXML
	Button GB_InButton;
	
	//出库
	@FXML
	TextField GB_OutItemNameTextField;
	@FXML
	TextField GB_OutItemNumTextField;
	@FXML
	TextField GB_OutPersonNameTextField;
	@FXML
	ComboBox<String> GB_OutDeviceIDComboBox;
	@FXML
	Button GB_OutButton;
	
	private CardInOutPage() {
		
	}
	
	public static CardInOutPage GetInstance() {
		if(S_CardInOutPage == null)
			S_CardInOutPage = new CardInOutPage();
		
		return S_CardInOutPage;
	}
	
	public void UI_Init() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("CardInOutPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("CardInOutPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        UIMainPage.GetInstance().getGB_Page().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(newValue == rootpane){
	
					List<String> devicelist = ManagerDao.QueryDeviceList(SignedManager.GetInstance().getGB_SignedAccount());
					GB_OutDeviceIDComboBox.getItems().addAll(devicelist);
				}
			}
		});
        
        GB_InButton.disableProperty().bind(new BooleanBinding() {
			{
				bind(GB_InItemNameTextField.textProperty());
				bind(GB_InItemNumTextField.textProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				String name = GB_InItemNameTextField.getText();
				Integer num = null;
				try {
					num = Integer.valueOf(GB_InItemNumTextField.getText());
				} catch (Exception e) {
					// TODO: handle exception
					num = null;
				}
				
				if((name != null)&&(name.length() > 0)
						&&(num != null)&&(num > 0))
					return false;
				else
					return true;
			}
		});
        UnaryOperator<Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) { 
                return change;
            }
            return null;
        };

        GB_InItemNumTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        
        GB_OutItemNumTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        
        GB_OutButton.disableProperty().bind(new BooleanBinding() {
			{
				bind(GB_OutItemNameTextField.textProperty());
				bind(GB_OutItemNumTextField.textProperty());
				bind(GB_OutPersonNameTextField.textProperty());
				bind(GB_OutDeviceIDComboBox.valueProperty());
			}
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				String name = GB_OutItemNameTextField.getText();
				String person = GB_OutPersonNameTextField.getText();
				Integer num = null;
				try {
					num = Integer.valueOf(GB_OutItemNumTextField.getText());
				} catch (Exception e) {
					// TODO: handle exception
					num = null;
				}
				
				if((name != null)&&(name.length() > 0)
						&&(person != null)&&(person.length() > 0)
						&&(GB_OutDeviceIDComboBox.getValue() != null)
						&&(num > 0))
					return false;
				else
					return true;
			}
		});
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
	}
	
	public AnchorPane GetPane() {
		return rootpane;
	}
	
	@FXML
	public void GB_InAction(){
		
		//获取管理员
		ManagerBean admin = ManagerDao.QueryReportManager(SignedManager.GetInstance().getGB_SignedAccount(), null);
						
		if(CheckRight(rootpane.getScene().getWindow(), admin.getPassword())){
			
			CardRecordBean cardRecordBean = new CardRecordBean();
			
			cardRecordBean.setItem(GB_InItemNameTextField.getText());
			
			Integer num = Integer.valueOf(GB_InItemNumTextField.getText());
			cardRecordBean.setNum(num);
			
			CardRecordDao.InOutBound(cardRecordBean, admin);
			
			GB_InItemNameTextField.setText(null);
			GB_InItemNumTextField.setText(null);
		}
	}
	
	@FXML
	public void GB_OutAction(){
		//获取管理员
		ManagerBean admin = ManagerDao.QueryReportManager(SignedManager.GetInstance().getGB_SignedAccount(), null);
								
		if(CheckRight(rootpane.getScene().getWindow(), admin.getPassword())){
					
			CardRecordBean cardRecordBean = new CardRecordBean();
					
			cardRecordBean.setItem(GB_OutItemNameTextField.getText());
					
			Integer num = Integer.valueOf(GB_OutItemNumTextField.getText());
			cardRecordBean.setNum(0-num);
			
			cardRecordBean.setName(GB_OutPersonNameTextField.getText());
			cardRecordBean.setDeviceid(GB_OutDeviceIDComboBox.getValue());
					
			CardRecordDao.InOutBound(cardRecordBean, admin);
			
			GB_OutItemNameTextField.setText(null);
			GB_OutItemNumTextField.setText(null);
			GB_OutPersonNameTextField.setText(null);
			GB_OutDeviceIDComboBox.setValue(null);
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

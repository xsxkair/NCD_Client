package com.xsx.ncd.handlers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.entity.NcdSoft;
import com.xsx.ncd.handlers.ReportOverViewPage.QueryTodayReportNumByStatusService.QueryReportTask;
import com.xsx.ncd.repository.NcdSoftRepository;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.HttpRequest;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Component
public class SoftHandler implements HandlerTemplet{

	private AnchorPane rootpane;
	@FXML StackPane rootStackPane;
	
	@FXML VBox GB_FreshPane;
	
	@FXML JFXDialog logDialog;
	@FXML Label logsText;
	
	private FileChooser fileChooser = null;
	
	@FXML JFXComboBox<String> softTypeComboBox;
	
	@FXML VBox softInfoVBox;
	//最新版信息
	@FXML Label newestVersionLabel;
	@FXML Label newestSizeLabel;
	@FXML Label newestMd5Label;
	
	//上传
	@FXML JFXTextField VersionField;
	@FXML Label FileNameLabel;
	@FXML Label FileSizeLabel;
	@FXML Label FileMd5Label;
	@FXML JFXButton uploadSoftButton;
	
	private ObjectProperty<File> clientFile;
	private UpClientService upClientService = null;
	
	@Autowired private WorkPageSession workPageSession;
	@Autowired private NcdSoftRepository ncdSoftRepository;
	
	@PostConstruct
	@Override
	public void UI_Init(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/views/SoftPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/views/SoftPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        softTypeComboBox.getItems().addAll("客户端软件", "客户端补丁", "设备软件");
        softTypeComboBox.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		upDataPageValue();
        	}
        });
        softInfoVBox.disableProperty().bind(softTypeComboBox.getSelectionModel().selectedItemProperty().isNull());
        
        fileChooser = new FileChooser();
        
        clientFile = new SimpleObjectProperty<File>(null);
        
        clientFile.addListener((o, oldValue, newValue)->{
        	upSeletedClientSoftInfo(newValue);
        });
        
        VersionField.lengthProperty().addListener((o, oldValue, newValue)->{
        	if((newValue.intValue() > 0) && (clientFile.get() != null))
        		uploadSoftButton.setDisable(false);
        	else
        		uploadSoftButton.setDisable(true);
        });
        
        
        upClientService = new UpClientService();

        GB_FreshPane.visibleProperty().bind(upClientService.runningProperty());

        upClientService.valueProperty().addListener((o, oldValue, newValue)->{
        	if(newValue == true){
        		showLogsDialog("上传成功！");
        		upDataPageValue();
        	}
        	else {
        		showLogsDialog("上传失败！");
			}
        }); 
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue)->{
        	if(rootpane.equals(newValue)){
        		//upDataPageValue();
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
	public void showPane(){
		workPageSession.setWorkPane(rootpane);
	}
	
	private void upDataPageValue() {
		NcdSoft ncdSoft = null;
		String str = null;
		Integer version;
		
		str = softTypeComboBox.getSelectionModel().getSelectedItem();
		//显示最新报告管理软件信息
		if("客户端软件".equals(str)){
			ncdSoft = ncdSoftRepository.findNcdSoftByName("Client");
		}
		else if ("客户端补丁".equals(str)) {
			ncdSoft = ncdSoftRepository.findNcdSoftByName("CPath");
		}
		else if ("设备软件".equals(str)) {
			ncdSoft = ncdSoftRepository.findNcdSoftByName("Device");
		}
		
		if(ncdSoft != null){
			version = ncdSoft.getVersion();
			str = String.format("%d.%d.%02d", version/1000, version%1000/100, version%100);
			newestVersionLabel.setText(str);
			newestMd5Label.setText(ncdSoft.getMD5());
			newestSizeLabel.setText(ncdSoft.getFsize()+"字节");
		}
		else{
			newestVersionLabel.setText(null);
			newestMd5Label.setText(null);
			newestSizeLabel.setText(null);
		}
		
		
		VersionField.clear();
		FileNameLabel.setText(null);
		FileSizeLabel.setText(null);
		FileMd5Label.setText(null);
	}
	
	private void upSeletedClientSoftInfo(File file) {
		String md5 = null;
		Double size = null;
		
		if(file != null){
			try {
				md5 = DigestUtils.md5Hex(new FileInputStream(file));
				FileMd5Label.setText(md5);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FileMd5Label.setText("error");
	    		
			}
			
			FileNameLabel.setText(file.getName());

			FileSizeLabel.setText(String.valueOf(file.length())+"字节");
		}
		else {
			FileNameLabel.setText(null);
			FileSizeLabel.setText(null);
			FileMd5Label.setText(null);
		}
	}

	private void showLogsDialog(String logs) {
		logsText.setText(logs);
		logDialog.show(rootStackPane);
	}
	
	@FXML public void downSoftFileAction(){
		
	}
	
	@FXML public void uploadSoftAction(){
		upClientService.restart();
	}
	
	@FXML
	public void selectFileAction(){
		fileChooser.setTitle("选择文件");
		
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("所有", "*.*"));
		
		File selectedFile = fileChooser.showOpenDialog(null);
		 
		 if(selectedFile != null){ 
			 clientFile.set(selectedFile);
		 }
	}

	@FXML
	public void logDialogAction(){
		if(logDialog.isVisible())
			logDialog.close();
	}
	
	class UpClientService extends Service<Boolean>{

		@Override
		protected Task<Boolean> createTask() {
			// TODO Auto-generated method stub
			return new QueryReportTask();
		}
		
		class QueryReportTask extends Task<Boolean>{

			@Override
			protected Boolean call(){
				// TODO Auto-generated method stub
				String str = null;
				int version;
				
				version = Integer.valueOf(VersionField.getText());
				
				str = softTypeComboBox.getSelectionModel().getSelectedItem();
				//显示最新报告管理软件信息
				if("客户端软件".equals(str)){
					return HttpRequest.uploadFile(clientFile.get(), version, 1);
				}
				else if ("客户端补丁".equals(str)) {
					return HttpRequest.uploadFile(clientFile.get(), version, 2);
				}
				else if ("设备软件".equals(str)) {
					return HttpRequest.uploadFile(clientFile.get(), version, 3);
				}
				else {
					return false;
				}
			}
		}
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
}

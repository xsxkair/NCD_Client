package com.xsx.ncd.handlers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.entity.NcdSoft;
import com.xsx.ncd.repository.NcdSoftRepository;
import com.xsx.ncd.spring.WorkPageSession;
import com.xsx.ncd.tool.HttpRequest;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Component
public class SoftHandler {

	private AnchorPane rootpane;
	
	private FileChooser fileChooser = null;
	
	//报告管理软件
	@FXML Label clientLastVersionLabel;
	@FXML Label clientLastSizeLabel;
	@FXML Label clientLastMd5Label;
	@FXML JFXButton clientNewFileButton;
	@FXML JFXTextField clientNewVersionField;
	@FXML Label clientNewFileNameLabel;
	@FXML Label clientNewFileSizeLabel;
	@FXML Label clientNewFileMd5Label;
	@FXML JFXButton clientUploadButton;
	private ObjectProperty<File> clientFile;
	
	
	//设备软件
	@FXML Label deviceLastVersionLabel;
	@FXML Label deviceLastSizeLabel;
	@FXML Label deviceLastMd5Label;
	@FXML JFXButton deviceNewFileButton;
	@FXML JFXTextField deviceNewVersionField;
	@FXML Label deviceNewFileNameLabel;
	@FXML Label deviceNewFileSizeLabel;
	@FXML Label deviceNewFileMd5Label;
	@FXML JFXButton deviceUploadButton;
	private ObjectProperty<File> deviceFile;

	@Autowired private WorkPageSession workPageSession;
	@Autowired private NcdSoftRepository ncdSoftRepository;
	
	@PostConstruct
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

        fileChooser = new FileChooser();
        
        clientFile = new SimpleObjectProperty<File>(null);
        
        clientFile.addListener((o, oldValue, newValue)->{
        	upSeletedClientSoftInfo(newValue);
        });
        
        clientNewVersionField.lengthProperty().addListener((o, oldValue, newValue)->{
        	if((newValue.intValue() > 0) && (clientFile.get() != null))
        		clientUploadButton.setDisable(false);
        	else
        		clientUploadButton.setDisable(true);
        });
        
        deviceFile = new SimpleObjectProperty<File>(null);
        deviceFile.addListener((o, oldValue, newValue)->{
        	upSeletedDeviceSoftInfo(newValue);
        });
        
        deviceNewVersionField.lengthProperty().addListener((o, oldValue, newValue)->{
        	if((newValue.intValue() > 0) && (deviceFile.get() != null))
        		deviceUploadButton.setDisable(false);
        	else {
        		deviceUploadButton.setDisable(true);
			}
        });
        
        
        workPageSession.getWorkPane().addListener((o, oldValue, newValue)->{
        	if(rootpane.equals(newValue)){
        		upDataPageValue();
        	}
        });
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
        
        loader = null;
        in = null;
	}
	
	public void ShowSoftPage(){
		workPageSession.setWorkPane(rootpane);
	}
	
	private void upDataPageValue() {
		NcdSoft ncdSoft = null;
		String str = null;
		Integer version;
		
		//显示最新报告管理软件信息
		ncdSoft = ncdSoftRepository.findNcdSoftByName("Client");
		
		if(ncdSoft != null){
			version = ncdSoft.getVersion();
			str = String.format("%d.%d.%02d", version/1000, version%1000/100, version%100);
			clientLastVersionLabel.setText(str);
			clientLastMd5Label.setText(ncdSoft.getMD5());
			clientLastSizeLabel.setText(ncdSoft.getFsize()+"字节");
		}
		else{
			clientLastVersionLabel.setText(null);
			clientLastMd5Label.setText(null);
			clientLastSizeLabel.setText(null);
		}
		
		
		clientNewFileMd5Label.setText(null);
		clientNewFileNameLabel.setText(null);
		clientNewFileSizeLabel.setText(null);
		clientNewVersionField.clear();
		clientFile.set(null);
		clientUploadButton.setDisable(true);
		
		//显示最新设备软件信息
		ncdSoft = ncdSoftRepository.findNcdSoftByName("Device");
		
		if(ncdSoft != null){
			version = ncdSoft.getVersion();
			str = String.format("%d.%d.%02d", version/1000, version%1000/100, version%100);
			deviceLastVersionLabel.setText(str);
			deviceLastMd5Label.setText(ncdSoft.getMD5());
			deviceLastSizeLabel.setText(ncdSoft.getFsize()+"字节");
		}
		else {
			deviceLastVersionLabel.setText(null);
			deviceLastMd5Label.setText(null);
			deviceLastSizeLabel.setText(null);
		}
		
		deviceNewFileMd5Label.setText(null);
		deviceNewFileNameLabel.setText(null);
		deviceNewFileSizeLabel.setText(null);
		deviceNewVersionField.clear();
		deviceFile.set(null);
		deviceUploadButton.setDisable(true);
	}
	
	private void upSeletedClientSoftInfo(File file) {
		String md5 = null;
		Double size = null;
		
		if(file != null){
			try {
				md5 = DigestUtils.md5Hex(new FileInputStream(file));
				clientNewFileMd5Label.setText(md5);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				clientNewFileMd5Label.setText("error");
	    		
			}
			
			clientNewFileNameLabel.setText(file.getName());
			
			size = (double) file.length();
			size /= 1048576;
    		clientNewFileSizeLabel.setText(String.format("%.2f MB", size));
    		
    		if(clientNewVersionField.getLength() > 0)
    			clientUploadButton.setDisable(false);
		}
		else {
			clientNewFileMd5Label.setText(null);
    		clientNewFileNameLabel.setText(null);
    		clientNewFileSizeLabel.setText(null);
    		
    		clientUploadButton.setDisable(true);
		}
	}
	
	private void upSeletedDeviceSoftInfo(File file) {
		String md5 = null;
		Double size = null;
		
		if(file != null){
			try {
				md5 = DigestUtils.md5Hex(new FileInputStream(file));
				deviceNewFileMd5Label.setText(md5);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				deviceNewFileMd5Label.setText("error");
			}
			
			deviceNewFileNameLabel.setText(file.getName());
			
			size = (double) file.length();
			size /= 1048576;
			deviceNewFileSizeLabel.setText(String.format("%.2f MB", size));
			
			if(deviceNewVersionField.getLength() > 0)
				deviceUploadButton.setDisable(false);
		}
		else {
			deviceNewFileMd5Label.setText(null);
			deviceNewFileNameLabel.setText(null);
    		deviceNewFileSizeLabel.setText(null);
    		
    		deviceUploadButton.setDisable(true);
		}
	}
	
	@FXML
	public void chooseClientSoftFileAction(){
		fileChooser.setTitle("选择文件");
		
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("压缩文件", "*.zip"));
		
		File selectedFile = fileChooser.showOpenDialog(null);
		 
		 if(selectedFile != null){ 
			 clientFile.set(selectedFile);
		 }
	}
	
	@FXML
	public void upLoadClientSoftFileAction(){
		HttpRequest.uploadFile(clientFile.get(), Integer.valueOf(clientNewVersionField.getText()), false);
	}
	
	@FXML
	public void chooseDeviceSoftFileAction(){
		fileChooser.setTitle("选择文件");
		
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Binary Files", "*.bin"));
		
		File selectedFile = fileChooser.showOpenDialog(null);
		 
		 if(selectedFile != null){ 
			 deviceFile.set(selectedFile);
		 }
	}
	
	@FXML
	public void upLoadDeviceSoftFileAction(){
		HttpRequest.uploadFile(clientFile.get(), Integer.valueOf(deviceNewVersionField.getText()), true);
	}
}

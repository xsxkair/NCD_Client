package com.xsx.ncd.handlers;

import com.xsx.ncd.entity.Device;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DeviceThumnPane extends VBox{
	
	public DeviceThumnPane(Image image, Device device){
		
		this.setUserData(device);
		
		ImageView deviceimage = new ImageView(image);
		deviceimage.setFitWidth(150);
		deviceimage.setFitHeight(123);
		Label deviceid = new Label(device.getDid());
		deviceid.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		
		this.setCursor(Cursor.HAND);
		this.setAlignment(Pos.CENTER);
		getChildren().addAll(deviceimage, deviceid);
		
		this.setPadding(new Insets(15, 15, 15, 15));
		this.setStyle("-fx-background-color:#D8EFF2");
		
		//提示
		Label userLabel = new Label("责任人："+ device.getName());
		Label addrLabel = new Label("设备地点："+ device.getAddr());
		VBox vBox = new VBox(userLabel, addrLabel);
		
		Tooltip tooltip = new Tooltip();
		tooltip.setGraphic(vBox);
        Tooltip.install(this, tooltip);
	}
}

package com.xsx.ncd.handlers;

import com.xsx.ncd.entity.Device;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DeviceThumnPane extends VBox{
	
	public DeviceThumnPane(Image image, Image logo, Device device){
		
		this.setUserData(device);
		
		ImageView deviceimage = new ImageView(image);
		deviceimage.setFitWidth(150);
		deviceimage.setFitHeight(150);
		
		ImageView logoImageView = new ImageView(logo);
		logoImageView.setFitWidth(50);
		logoImageView.setFitHeight(42);
		
		Label deviceIdLabel = new Label(device.getDid());
		deviceIdLabel.getStyleClass().add("DeviceThumnIdLabel");
		Label deviceAddrLabel = new Label(device.getAddr());
		deviceAddrLabel.getStyleClass().add("DeviceThumnAddrLabel");
		VBox vBox = new VBox(deviceIdLabel, deviceAddrLabel);
		vBox.getStyleClass().add("DeviceThumnPaneChild");
		
		HBox hBox = new HBox(logoImageView, vBox);
		hBox.getStyleClass().add("DeviceThumnPane");

		getChildren().addAll(deviceimage, hBox);
		this.getStyleClass().add("DeviceThumnPaneRoot");

		this.setSpacing(10);
	}
}

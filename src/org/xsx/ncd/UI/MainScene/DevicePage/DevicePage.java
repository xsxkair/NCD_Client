package org.xsx.ncd.UI.MainScene.DevicePage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xsx.ncd.Data.UIMainPage;
import org.xsx.ncd.UI.MainScene.DevicePage.DeviceDetailPage.DeviceDetailPage;
import org.xsx.ncd.servicetask.ReadDeviceInfoService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;

public class DevicePage {
	
	private static DevicePage S_DevicePage = null;
	
	private AnchorPane devicepane;
	
	@FXML
	TableView<DeviceTableItem> DeviceListShowPane;
	@FXML
	TableColumn<DeviceTableItem, Image> TableColumn1;
	@FXML
	TableColumn<DeviceTableItem, String> TableColumn2;
	@FXML
	TableColumn<DeviceTableItem, String> TableColumn3;
	@FXML
	TableColumn<DeviceTableItem, String> TableColumn4;
	@FXML
	TableColumn<DeviceTableItem, String> TableColumn5;
	@FXML
	TableColumn<DeviceTableItem, String> TableColumn6;
	
	@FXML
	ScrollPane DeviceICOShowRootPane;
	@FXML
	FlowPane DeviceThumbShowPane;
	
	@FXML
	ToggleButton DeviceListShowButton;
	
	@FXML
	StackPane GB_FreshPane;
	@FXML
	ProgressIndicator GB_RefreshBar;
	
	//更新设备状态任务
	private ReadDeviceInfoService S_ReadDeviceInfoService = new ReadDeviceInfoService();
	
	private DevicePage(){
		
	}
	
	public static DevicePage GetInstance() {
		if(S_DevicePage == null){
			S_DevicePage = new DevicePage();
		}
		
		return S_DevicePage;
	}
	
	public void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("DevicePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("DevicePage.fxml");
        loader.setController(this);
        try {
        	devicepane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //列表选择按钮
        DeviceListShowPane.visibleProperty().bind(DeviceListShowButton.selectedProperty());
        
        TableColumn1.setCellValueFactory(new PropertyValueFactory<DeviceTableItem, Image>("deviceico"));
        TableColumn1.setCellFactory(new MyColumnCallback<DeviceTableItem, Image>());
        
        TableColumn2.setCellValueFactory(new PropertyValueFactory<DeviceTableItem, String>("deviceid"));
        TableColumn2.setCellFactory(new MyColumnCallback<DeviceTableItem, String>());
        
        TableColumn3.setCellValueFactory(new PropertyValueFactory<DeviceTableItem, String>("devicemanagername"));
        TableColumn3.setCellFactory(new MyColumnCallback<DeviceTableItem, String>());
        
        TableColumn4.setCellValueFactory(new PropertyValueFactory<DeviceTableItem, String>("devicemanagerphone"));
        TableColumn4.setCellFactory(new MyColumnCallback<DeviceTableItem, String>());
        
        TableColumn5.setCellValueFactory(new PropertyValueFactory<DeviceTableItem, String>("deviceaddr"));
        TableColumn5.setCellFactory(new MyColumnCallback<DeviceTableItem, String>());
        
        TableColumn6.setCellValueFactory(new PropertyValueFactory<DeviceTableItem, String>("devicestatus"));
        TableColumn6.setCellFactory(new MyColumnCallback<DeviceTableItem, String>());
        
        S_ReadDeviceInfoService.setPeriod(Duration.minutes(1));
        
        GB_RefreshBar.progressProperty().bind(S_ReadDeviceInfoService.progressProperty());
        S_ReadDeviceInfoService.stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				// TODO Auto-generated method stub
				GB_FreshPane.setVisible((newValue == State.RUNNING));
			}
		});
        DeviceListShowPane.itemsProperty().bind(S_ReadDeviceInfoService.lastValueProperty());
        
        S_ReadDeviceInfoService.lastValueProperty().addListener(new ChangeListener<ObservableList<DeviceTableItem>>() {

			@Override
			public void changed(ObservableValue<? extends ObservableList<DeviceTableItem>> observable,
					ObservableList<DeviceTableItem> oldValue, ObservableList<DeviceTableItem> newValue) {
				// TODO Auto-generated method stub
				if(newValue != null){
					DeviceThumbShowPane.getChildren().clear();

					for (DeviceTableItem deviceTableItem : newValue) {
						
						DeviceThumnPane temp = deviceTableItem.getDevicethumn();
						
						//提示					
						Tooltip tooltip = new Tooltip();
						tooltip.setGraphic(new DeviceTipInfo(deviceTableItem));
				        Tooltip.install(temp, tooltip);
				        
				        temp.setOnMouseClicked(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent event) {
								// TODO Auto-generated method stub
								DeviceDetailPage.GetInstance().setS_DeviceId(temp.getDeviceid());
								UIMainPage.GetInstance().setGB_Page(DeviceDetailPage.GetInstance().GetPane());
							}
						});
				        
						DeviceThumbShowPane.getChildren().add(temp);
					}
				}
			}
		});
        
        UIMainPage.GetInstance().getGB_Page().addListener(new ChangeListener<Pane>() {

			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				// TODO Auto-generated method stub
				if(devicepane.equals(newValue)){
					S_ReadDeviceInfoService.restart();
				}
				
				if(devicepane.equals(oldValue)){
					S_ReadDeviceInfoService.cancel();
				}
			}
		});
        
        AnchorPane.setTopAnchor(devicepane, 0.0);
        AnchorPane.setBottomAnchor(devicepane, 0.0);
        AnchorPane.setLeftAnchor(devicepane, 0.0);
        AnchorPane.setRightAnchor(devicepane, 0.0);

	}
	
	public AnchorPane GetPane(){

		return devicepane;
	}
	
	class MyColumnCallback<S, T> implements Callback<TableColumn<S, T>,TableCell<S, T>>{

		@Override
		public TableCell<S, T> call(TableColumn<S, T> param) {
			// TODO Auto-generated method stub
			TableCell<S, T> cell = new TableCell<S, T>(){
				@Override
				public void updateItem(T item, boolean empty) {
					if(item != null){
						if(item instanceof Image){
							ImageView imageView = new ImageView((Image)item);
							imageView.setFitWidth(50);
							imageView.setFitHeight(41);
							setGraphic(imageView);
						}
						else if(item instanceof String){
							setText((String) item);
						}
					}
					else {
						setGraphic(null);
						setText(null);
					}
				}
				
			};
			
			
			cell.setAlignment(Pos.CENTER);
			
			Tooltip tooltip = new Tooltip();
			
			cell.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					TableRow<T> row = cell.getTableRow();
					
					if(row.getIndex() < DeviceListShowPane.getItems().size()){
				        
				        tooltip.setGraphic(new DeviceTipInfo(DeviceListShowPane.getItems().get(row.getIndex())));
				        Tooltip.install(cell, tooltip);
					}
					else
						Tooltip.uninstall(cell, tooltip);
				}
			});
			
			cell.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					TableRow<T> row = cell.getTableRow();
					if(event.getClickCount() == 2){
						DeviceDetailPage.GetInstance().setS_DeviceId(DeviceListShowPane.getItems().get(row.getIndex()).getDeviceid());
						UIMainPage.GetInstance().setGB_Page(DeviceDetailPage.GetInstance().GetPane());
					}
				}
			});
			
			return cell;
		}
	}

}

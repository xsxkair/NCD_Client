package com.xsx.ncd.tool;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.control.skin.DatePickerSkin;

public class DateTimePickerSkin extends DatePickerSkin {

    private DateTimePicker datePicker;
    private DatePickerContent ret;

    public DateTimePickerSkin(DateTimePicker datePicker){
        super(datePicker);
        this.datePicker = datePicker;
    }

    @Override
    public Node getPopupContent() {
        if (ret == null){
            ret = (DatePickerContent) super.getPopupContent();

            Slider hours = new Slider(0, 23, (datePicker.getDateTimeValue() != null ? datePicker.getDateTimeValue().getMinute() : 0));      
            Label hoursValue = new Label("时：" + (datePicker.getDateTimeValue() != null ? datePicker.getDateTimeValue().getHour() : "") + " ");

            Slider minutes = new Slider(0, 59, (datePicker.getDateTimeValue() != null ? datePicker.getDateTimeValue().getMinute() : 0));
            Label minutesValue =  new Label("分：" + (datePicker.getDateTimeValue() != null ? datePicker.getDateTimeValue().getMinute() : "") + " ");

            Slider seconds = new Slider(0, 59, (datePicker.getDateTimeValue() != null ? datePicker.getDateTimeValue().getSecond() : 0));        
            Label secondsValue = new Label("秒：" + (datePicker.getDateTimeValue() != null ? datePicker.getDateTimeValue().getSecond() : "") + " ");

            ret.getChildren().addAll(new HBox(hoursValue, hours), new HBox(minutesValue, minutes), new HBox(secondsValue, seconds));

            hours.valueProperty().addListener((observable, oldValue, newValue) -> {
            	datePicker.setDateTimeValue(datePicker.getDateTimeValue().withHour(newValue.intValue()));
                hoursValue.setText("时：" + String.format("%02d", datePicker.getDateTimeValue().getHour()) + " ");
            });

            minutes.valueProperty().addListener((observable, oldValue, newValue) -> {
            	datePicker.setDateTimeValue(datePicker.getDateTimeValue().withMinute(newValue.intValue()));
                minutesValue.setText("分：" + String.format("%02d", datePicker.getDateTimeValue().getMinute()) + " ");
            });

            seconds.valueProperty().addListener((observable, oldValue, newValue) -> {
            	datePicker.setDateTimeValue(datePicker.getDateTimeValue().withSecond(newValue.intValue()));
                secondsValue.setText("秒：" + String.format("%02d", datePicker.getDateTimeValue().getSecond()) + " ");
            });

        }
        return ret;
    }


}
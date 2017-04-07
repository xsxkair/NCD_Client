package com.xsx.ncd.tool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;


public class DateTimePicker extends DatePicker{

    private ObjectProperty<LocalDateTime>  dateTimeValue = new SimpleObjectProperty<>();

    public DateTimePicker(){
        super();
        dateTimeValue.set(LocalDateTime.now());
        valueProperty().addListener(t -> {
            dateTimeValue.set(LocalDateTime.of(this.getValue(),dateTimeValue.get().toLocalTime()));
        });
        
        setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            @Override
            public String toString ( LocalDate object ) {
            	if(dateTimeValue.get() == null)
            		return null;
            	else
            		return dateTimeValue.get().format(formatter);
            }

            @Override
            public LocalDate fromString ( String string ) {
                return LocalDate.parse(string, formatter);
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin () {
        return new DateTimePickerSkin(this);
    }

    public LocalDateTime getDateTimeValue() {
        return dateTimeValueProperty().get();
    }

    public void setDateTimeValue (LocalDateTime dateTimeValue) {
        dateTimeValueProperty().set(dateTimeValue);
    }

    public ObjectProperty<LocalDateTime> dateTimeValueProperty(){
        return dateTimeValue;
    }


}
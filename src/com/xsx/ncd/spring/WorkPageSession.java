package com.xsx.ncd.spring;

import org.springframework.stereotype.Component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;

@Component
public class WorkPageSession {
	
	//���������ʾ����
	private ObjectProperty<Pane> WorkPane = new SimpleObjectProperty<>();

	public ObjectProperty<Pane> getWorkPane() {
		return WorkPane;
	}

	public void setWorkPane(Pane gB_MainPane) {
		WorkPane.set(gB_MainPane);
	}
}

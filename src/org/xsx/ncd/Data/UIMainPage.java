package org.xsx.ncd.Data;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class UIMainPage {
	
	private static UIMainPage GB_UIMainPage = null; 
	
	private ObjectProperty<Pane>	GB_Page = null;
	
	private UIMainPage(){
		
	}
	
	public static UIMainPage GetInstance() {
		if(GB_UIMainPage == null)
			GB_UIMainPage = new UIMainPage();
		
		if(GB_UIMainPage.GB_Page == null)
			GB_UIMainPage.GB_Page = new SimpleObjectProperty<>();
		
		return GB_UIMainPage;
	}

	public ObjectProperty<Pane> getGB_Page() {
		return GB_Page;
	}

	public void setGB_Page(Pane scene) {
		GB_Page.set(scene);
	}
}

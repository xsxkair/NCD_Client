package org.xsx.ncd.Data;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;

public class UIScence {
	
	private static UIScence GB_UIScence = null; 
	
	private ObjectProperty<Scene>	GB_Scene = null;
	
	private UIScence(){
		
	}
	
	public static UIScence GetInstance() {
		if(GB_UIScence == null)
			GB_UIScence = new UIScence();
		
		if(GB_UIScence.GB_Scene == null)
			GB_UIScence.GB_Scene = new SimpleObjectProperty<>();
		
		return GB_UIScence;
	}

	public ObjectProperty<Scene> getGB_Scene() {
		return GB_Scene;
	}

	public void setGB_Scene(Scene scene) {
		GB_Scene.set(scene);
	}
}

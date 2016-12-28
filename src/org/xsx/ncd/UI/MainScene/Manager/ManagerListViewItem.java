package org.xsx.ncd.UI.MainScene.Manager;

public class ManagerListViewItem {
	
	private Object[] data;
	
	public ManagerListViewItem(Object[] data){
		this.data = data;
	}
	
	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}
	
	public String GetAccount(){
		return (String) data[0];
	}
	
	public String GetName(){
		return (String) data[1];
	}

	@Override
	public String toString(){
		String data_0 = (String) data[0];
		String data_1 = (String) data[1];
		return data_1;
	}
}

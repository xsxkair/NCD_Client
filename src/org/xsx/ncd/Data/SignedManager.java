package org.xsx.ncd.Data;


public class SignedManager {

	private static SignedManager S_SignedManager = null;
	
	private String GB_SignedAccount = null;
	
	private SignedManager(){
		
	}
	
	public static SignedManager GetInstance() {
		if(S_SignedManager == null)
			S_SignedManager = new SignedManager();
		
		return S_SignedManager;
	}

	public String getGB_SignedAccount() {
		return GB_SignedAccount;
	}

	public void setGB_SignedAccount(String gB_SignedAccount) {
		GB_SignedAccount = gB_SignedAccount;
	}

}

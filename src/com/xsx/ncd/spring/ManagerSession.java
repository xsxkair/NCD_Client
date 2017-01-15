package com.xsx.ncd.spring;

import org.springframework.stereotype.Component;

@Component
public class ManagerSession {
	
	private String account = null;
	
	private String fatherAccount = null;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getFatherAccount() {
		return fatherAccount;
	}

	public void setFatherAccount(String fatherAccount) {
		this.fatherAccount = fatherAccount;
	}
	
	
}

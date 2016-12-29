package com.xsx.ncd.spring;

import org.springframework.stereotype.Component;

@Component
public class ManagerSession {
	
	private String account = "xsx";

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	
}

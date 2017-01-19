package com.xsx.ncd.spring;

import org.springframework.stereotype.Component;

@Component
public class ManagerSession {
	
	private String account = null;
	
	private String fatherAccount = null;
	
	private Integer userType = null;

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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	
}

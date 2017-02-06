package com.xsx.ncd.spring;

import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.User;

@Component
public class UserSession {

	private User user = null;

	public String getAccount() {
		return user.getAccount();
	}

	public String getFatherAccount() {
		return user.getFatheraccount();
	}
	
	public String getName() {
		return user.getName();
	}

	public Integer getUserType() {
		return user.getType();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

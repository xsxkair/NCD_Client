package com.xsx.ncd.spring;

import org.springframework.stereotype.Component;

@Component
public class SystemSetData {
	
	private int pageSize = 50;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}

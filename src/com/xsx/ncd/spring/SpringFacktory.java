package com.xsx.ncd.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.DeviceRepository;
import com.xsx.ncd.repository.ManagerRepository;
import com.xsx.ncd.repository.TestDataRepository;

public class SpringFacktory {
	
	private static ApplicationContext ctx = null;
	
	public static void SpringFacktoryInit(){
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	public static ApplicationContext getCtx() {
		return ctx;
	}

	public static ManagerRepository getManagerRepository() {
		return ctx.getBean(ManagerRepository.class);
	}
	
	public static CardRepository getCardRepository() {
		return ctx.getBean(CardRepository.class);
	}
	
	public static DeviceRepository getDeviceRepository() {
		return ctx.getBean(DeviceRepository.class);
	}
	
	public static TestDataRepository getTestDataRepository() {
		return ctx.getBean(TestDataRepository.class);
	}
	
	public static ManagerSession getManagerSession(){
		return ctx.getBean(ManagerSession.class);
	}
	
	public static WorkPageSession getUIObject(){
		return ctx.getBean(WorkPageSession.class);
	}
}

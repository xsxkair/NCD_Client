package com.xsx.ncd.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xsx.ncd.define.CardConstInfo;

public class SpringFacktory {
	
	private static ApplicationContext ctx = null;
	
	public static void SpringFacktoryInit(){
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	public static ApplicationContext getCtx() {
		return ctx;
	}
	
	public static CardConstInfo getCardConstInfoByItem(String item){
		
		CardConstInfo cardConstInfo = null;
		
		if("NT-proBNP".equals(item))
			cardConstInfo = (CardConstInfo) ctx.getBean("NT_proBNPConstInfo");
		else if("CK-MB".equals(item))
			cardConstInfo = (CardConstInfo) ctx.getBean("CK_MBConstInfo");
		else if("cTnI".equals(item))
			cardConstInfo = (CardConstInfo) ctx.getBean("cTnIConstInfo");
		else if("Myo".equals(item))
			cardConstInfo = (CardConstInfo) ctx.getBean("MyoConstInfo");
		else
			cardConstInfo = null;
		
		return cardConstInfo;
	}
}

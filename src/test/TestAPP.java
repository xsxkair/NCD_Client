package test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xsx.ncd.spring.SpringFacktory;

public class TestAPP {
	
	
	
	@Test
	public void testCommonCustomRepositoryMethod(){
		SpringFacktory.SpringFacktoryInit();
		
		System.out.println(SpringFacktory.getManagerSession().getAccount());
		
	}
}

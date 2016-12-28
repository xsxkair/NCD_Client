package test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestAPP {
	
	private ApplicationContext ctx = null;


	{
		


	}
	
	@Test
	public void testCommonCustomRepositoryMethod(){
		try {
			ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
			System.out.println("success");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error");
		}
		
	}
}

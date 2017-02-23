package com.xsx.ncd.tool;
/**
 *@author: xsx
 *@date:2015年10月29日上午10:29:34
 *
 *@file name:Int_Byte_Change.java
 */
public class Int_Byte_Change {
	public static int ByteToInt(byte a){
		int b = 256;
		if(a < 0){
			b = b+a;
		}
		else {
			b=a;
		}
		
		return b;
	}
}

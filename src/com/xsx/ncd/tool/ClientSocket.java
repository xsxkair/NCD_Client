package com.xsx.ncd.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientSocket {
	
	public static String ComWithServer(String ip, String senddata){
		String result = null;
		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		SocketAddress remoteAddr = null; 
		int timeoutc = 10;
		int readnum = 0;
		try {
			socket = new Socket();
			remoteAddr = new InetSocketAddress(ip, 9001);
			socket.connect(remoteAddr, 1000);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			out.write(senddata.getBytes());
			byte[] bytes = new byte[4096];
			while((timeoutc--) > 0)
			{
				if(in.available() > 0)
				{
					readnum = in.read(bytes);
					break;
				}
				Thread.sleep(10);
			}
			if(readnum > 0)
				result = new String(bytes, 0, readnum);
		}finally {
			try {
				if(socket != null)
					socket.close();
				
				if(in != null)
					in.close();
				
				if(out != null)
					out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return result;
		}
	
	}
}

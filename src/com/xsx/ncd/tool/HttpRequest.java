package com.xsx.ncd.tool;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    /**
     * ��ָ��URL����GET����������
     * 
     * @param url
     *            ���������URL
     * @param param
     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return URL ������Զ����Դ����Ӧ���
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ��ָ�� URL ����POST����������
     * 
     * @param url
     *            ��������� URL
     * @param param
     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return ������Զ����Դ����Ӧ���
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("���� POST ��������쳣��"+e);
            e.printStackTrace();
        }
        //ʹ��finally�����ر��������������
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    public static void uploadFile(File file, Integer version, Boolean isDevice) {  
        try {  
  
            // ���з�  
            final String newLine = "\r\n";  
            final String boundaryPrefix = "--";  
            // �������ݷָ���  
            String BOUNDARY = "----WebKitFormBoundaryLdaZZRA9RAhrUwTj";  
            // ������������  
            URL url = null;
            
            /*****�ļ�����ͷ*****/
            StringBuilder fileHead = new StringBuilder();
            /*****�汾����ͷ*****/
            StringBuilder versionHead = new StringBuilder();
            /*****��β*****/
            StringBuilder endStr = new StringBuilder();
            
            if(isDevice)
            	url = new URL("http://116.62.108.201:8080/NCD_Server/deviceCodeUpload"); 
            else
            	url = new URL("http://116.62.108.201:8080/NCD_Server/clientUpload");
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            // ����ΪPOST��  
            conn.setRequestMethod("POST");  
            // ����POST�������������������  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);

            // ��������ͷ����  
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
            conn.setRequestProperty("Charsert", "UTF-8");
            
            /*****����ļ�����ͷ*****/
            fileHead = new StringBuilder();  
            fileHead.append(boundaryPrefix);  
            fileHead.append(BOUNDARY);  
            fileHead.append(newLine);  
            // �ļ�����,photo���������������޸�  
            fileHead.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName()  
                    + "\"" + newLine);  
            fileHead.append("Content-Type:application/octet-stream");  
            // ����ͷ�������Ժ���Ҫ�������У�Ȼ����ǲ�������  
            fileHead.append(newLine);  
            fileHead.append(newLine);  
            
            /*****��Ϲ̼��汾����ͷ*****/
            versionHead = new StringBuilder();  
            versionHead.append(boundaryPrefix);  
            versionHead.append(BOUNDARY);  
            versionHead.append(newLine);  
            // �ļ�����,photo���������������޸�  
            versionHead.append("Content-Disposition: form-data; name=\"version\"" + newLine);  
            // ����ͷ�������Ժ���Ҫ�������У�Ȼ����ǲ�������  
            versionHead.append(newLine);
            
            /*****��Ͻ�β*****/
            endStr = new StringBuilder();
            endStr.append(boundaryPrefix);  
            endStr.append(BOUNDARY);
            endStr.append(boundaryPrefix);
            endStr.append(newLine); 
            
          //�����������ݳ���
            long dataSize = fileHead.length() + file.length() + versionHead.length() + String.valueOf(version).length() + endStr.length();
            conn.setRequestProperty("Content-Length", String.valueOf(dataSize));
            
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
  

            /*****�����ļ�*****/
            out.write(fileHead.toString().getBytes());  
  
            // ����������,���ڶ�ȡ�ļ�����  
            DataInputStream in = new DataInputStream(new FileInputStream(  
                    file));  
            byte[] bufferOut = new byte[1024];  
            int bytes = 0;  
            // ÿ�ζ�1KB����,���ҽ��ļ�����д�뵽�������  
            while ((bytes = in.read(bufferOut)) != -1) {  
                out.write(bufferOut, 0, bytes);  
            }  
            // �����ӻ���  
            out.write(newLine.getBytes());  
            in.close();  
  
            /*****�����ļ��汾*****/
            out.write(versionHead.toString().getBytes());
            out.write(String.valueOf(version).getBytes());
            out.write(newLine.getBytes());
            
            /*****���ͽ�β*****/
            out.write(endStr.toString().getBytes());
  
            out.flush();  
            out.close();  
  
            // ����BufferedReader����������ȡURL����Ӧ  
            BufferedReader reader = new BufferedReader(new InputStreamReader(  
                    conn.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
               System.out.println(line);  
            }  
  
        } catch (Exception e) {  
            System.out.println("����POST��������쳣��" + e);  
            e.printStackTrace();  
        }  
    }  
}

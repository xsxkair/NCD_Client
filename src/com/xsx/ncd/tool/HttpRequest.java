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
    
    public static void uploadFile(File file) {  
        try {  
  
            // ���з�  
            final String newLine = "\r\n";  
            final String boundaryPrefix = "--";  
            // �������ݷָ���  
            String BOUNDARY = "========7d4a6d158c9";  
            // ������������  
            URL url = new URL("http://116.62.108.201:8080/NCD_Server/clientUpload");  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            // ����ΪPOST��  
            conn.setRequestMethod("POST");  
            // ����POST�������������������  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            // ��������ͷ����  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("Charsert", "UTF-8");  
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
  
            // �ϴ��ļ�  
  
            StringBuilder sb = new StringBuilder();  
            sb.append(boundaryPrefix);  
            sb.append(BOUNDARY);  
            sb.append(newLine);  
            // �ļ�����,photo���������������޸�  
            sb.append("Content-Disposition: form-data;name=\"photo\";filename=\"" + file.getName()  
                    + "\"" + newLine);  
            sb.append("Content-Type:application/octet-stream");  
            // ����ͷ�������Ժ���Ҫ�������У�Ȼ����ǲ�������  
            sb.append(newLine);  
            sb.append(newLine);  
  
            // ������ͷ������д�뵽�������  
            out.write(sb.toString().getBytes());  
  
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
  
            // ����������ݷָ��ߣ���--����BOUNDARY�ټ���--��  
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)  
                    .getBytes();  
            // д�Ͻ�β��ʶ  
            out.write(end_data);  
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
POST /NCD_Server/deviceCodeUpload HTTP/1.1
Host: 116.62.108.201:8080
Connection: keep-alive
Content-Length: 322
Cache-Control: max-age=0
Origin: http://116.62.108.201:8080
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36
Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryLdaZZRA9RAhrUwTj
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
Referer: http://116.62.108.201:8080/NCD_Server/deviceCodeUpload
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.8
Cookie: JSESSIONID=6D181E4F8BCEBB77E6D063EF0893D971

------WebKitFormBoundaryLdaZZRA9RAhrUwTj
Content-Disposition: form-data; name="file"; filename="新建文本文档 (2).bin"
Content-Type: application/octet-stream

1234567890
------WebKitFormBoundaryLdaZZRA9RAhrUwTj
Content-Disposition: form-data; name="version"

1013
------WebKitFormBoundaryLdaZZRA9RAhrUwTj--

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
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
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
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
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
  
            // 换行符  
            final String newLine = "\r\n";  
            final String boundaryPrefix = "--";  
            // 定义数据分隔线  
            String BOUNDARY = "========7d4a6d158c9";  
            // 服务器的域名  
            URL url = new URL("http://116.62.108.201:8080/NCD_Server/clientUpload");  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            // 设置为POST情  
            conn.setRequestMethod("POST");  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            // 设置请求头参数  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("Charsert", "UTF-8");  
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
  
            // 上传文件  
  
            StringBuilder sb = new StringBuilder();  
            sb.append(boundaryPrefix);  
            sb.append(BOUNDARY);  
            sb.append(newLine);  
            // 文件参数,photo参数名可以随意修改  
            sb.append("Content-Disposition: form-data;name=\"photo\";filename=\"" + file.getName()  
                    + "\"" + newLine);  
            sb.append("Content-Type:application/octet-stream");  
            // 参数头设置完以后需要两个换行，然后才是参数内容  
            sb.append(newLine);  
            sb.append(newLine);  
  
            // 将参数头的数据写入到输出流中  
            out.write(sb.toString().getBytes());  
  
            // 数据输入流,用于读取文件数据  
            DataInputStream in = new DataInputStream(new FileInputStream(  
                    file));  
            byte[] bufferOut = new byte[1024];  
            int bytes = 0;  
            // 每次读1KB数据,并且将文件数据写入到输出流中  
            while ((bytes = in.read(bufferOut)) != -1) {  
                out.write(bufferOut, 0, bytes);  
            }  
            // 最后添加换行  
            out.write(newLine.getBytes());  
            in.close();  
  
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。  
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)  
                    .getBytes();  
            // 写上结尾标识  
            out.write(end_data);  
            out.flush();  
            out.close();  
  
            // 定义BufferedReader输入流来读取URL的响应  
            BufferedReader reader = new BufferedReader(new InputStreamReader(  
                    conn.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
               System.out.println(line);  
            }  
  
        } catch (Exception e) {  
            System.out.println("发送POST请求出现异常！" + e);  
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
Content-Disposition: form-data; name="file"; filename="鏂板缓鏂囨湰鏂囨。 (2).bin"
Content-Type: application/octet-stream

1234567890
------WebKitFormBoundaryLdaZZRA9RAhrUwTj
Content-Disposition: form-data; name="version"

1013
------WebKitFormBoundaryLdaZZRA9RAhrUwTj--

package com.example.menfer.byoureyes.YEUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.List;
import java.util.Map;

/**
 * Created by Menfer on 2017/5/3.
 * 向指定URL发送GET和POST方式的请求
 */
public class GetPostUtils {

    /**
     * 向指定url发送GET方式的请求
     * 参数：url：发送请求的URL
     *       params：为name1=value1&name2=value2的形式
     * 返回URL所代表的远程资源的响应
     * */
    public static String sendGet(String url,String params)
    {
        String result = "";
        InputStream is = null;
        try{
            String urlName = url+"?"+params;
            URL realUrl = new URL(urlName);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            //设置通用属性
            //conn.setRequestProperty("accept","*/*");
            //conn.setRequestProperty("connection","Keep-Alive");
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int code = conn.getResponseCode();
            if(200==code){
                is = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buffer = new byte[1024];
                while((len = is.read(buffer))!=-1){
                    baos.write(buffer,0,len);
                }
                //is.close();
                result = baos.toString();
            }else{
                result = "网络连接错误";
            }
        }catch(Exception e){
            //错误检测未进行
        }
        //使用finally块来关闭输入流
        finally{
            try{
                if(is != null){
                    is.close();
                }
            }catch (IOException ioe){}
        }
        return result;
    }


    /**
     * 向指定URL发送POST请求
     * */
    public static String sendPost(String url, String params){
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try{
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用属性
            //conn.setRequestProperty("accept","*/*");
            //conn.setRequestProperty("connection","Keep-Alive");

            //发送POST请求所需配置，后续修改需留意
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数
            out.print(params);
            //flush输出流的缓冲
            out.flush();
            //定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = in.readLine()) != null){
                result += "\n" + line;
            }
        }catch (Exception e){
            //错误检测未进行
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                //错误检测未进行
            }
        }
        return result;
    }
}

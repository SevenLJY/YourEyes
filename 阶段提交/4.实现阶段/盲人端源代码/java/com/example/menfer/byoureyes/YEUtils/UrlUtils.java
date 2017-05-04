package com.example.menfer.byoureyes.YEUtils;

import com.example.menfer.byoureyes.FixedValue;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * 用于Url连接与服务器交互的工具类
 * 替代之前的GetPostUtils
 * Created by Menfer on 2017/5/4.
 */
public class UrlUtils  {
    //各方法的返回值
    static String result;

    /**
     * doGet()用GET方式向服务器发送请求
     * 参数：baseUrl：服务器IP地址以及应用路径
     *       paramsMap：参数表（HashMap），将解析到Url中
     * */
    public static String doGet(String baseUrl, HashMap<String,String> paramsMap){
        //默认设置连接失败
        result = "ConnectionFailed";
        try{
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            tempParams.append("?");
            for(String key:paramsMap.keySet()){
                if(pos>0){
                    tempParams.append("&");
                }
                tempParams.append(key + "=" +paramsMap.get(key));
                pos++;
            }
            String requestUrl = baseUrl+tempParams.toString();
            //新建一个URL对象
            URL url = new URL(requestUrl);
            //打开一个HttpUrlConnection
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            //设置连接主机超时时间
            urlConn.setConnectTimeout(5*1000);
            //设置从主机读取数据超时时间
            urlConn.setReadTimeout(5*1000);
            //设置请求为GET
            urlConn.setRequestMethod("GET");
            //设置请求中的媒体类型信息
            urlConn.setRequestProperty("Content-type","application/json");
            //设置客户端与服务器连接类型
            urlConn.setRequestProperty("Connection","Keep-Alive");
            //开始连接
            urlConn.connect();
            //判断连接是否成功
            if(200==urlConn.getResponseCode()){
                result = streamToString(urlConn.getInputStream());
            }else{
                result = FixedValue.ConnectionFailed;
            }
            urlConn.disconnect();

        }catch (Exception e){
            result = FixedValue.ExceptionOccured;
        }
        return result;
    }

    /**
     * doPost()通过post请求与服务器连接
     * 参数：baseUrl：服务器IP与相关应用路径
     *       paramsMap：参数表（HashMap）
     * */
    public static String doPost(String baseUrl, HashMap<String, String> paramsMap){
        try{
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for(String key:paramsMap.keySet()){
                if(pos>0){
                    tempParams.append("&");
                }
                tempParams.append(key + "=" + paramsMap.get(key));
                pos++;
            }
            String params = tempParams.toString();
            //将请求的参数转换成byte数组
            byte[] postData = params.getBytes();
            //新建一个URL连接
            URL url = new URL(baseUrl);
            //打开一个URL连接
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            //设置连接超时时间
            urlConn.setConnectTimeout(5*1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5*1000);
            //设置允许输出为true
            urlConn.setDoOutput(true);
            //设置请求允许输入为true
            urlConn.setDoInput(true);
            //设置不使用缓存
            urlConn.setUseCaches(false);
            //设置为POST方式
            urlConn.setRequestMethod("POST");
            //设置是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            //设置请求内容类型,注意必须指定以表单形式提交，否则无法读取内容。后续更改需留意
            urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            //开始连接
            urlConn.connect();
            //发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();
            dos.close();
            //判断请求是否成功
            if(urlConn.getResponseCode() == 200){
                //获取返回数据
                result = streamToString(urlConn.getInputStream());
            }else{
                result = FixedValue.ConnectionFailed;
            }
        }catch (Exception e){
            result = FixedValue.ConnectionFailed;
        }
        return result;
    }

    /**
     * 将输入流转换成字符串
     * 参数： is：从网络获取的输入流
     */
    private static String streamToString(InputStream is){
        try{
            ByteArrayOutputStream baos =  new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = is.read(buffer)) != -1){
                baos.write(buffer,0,len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        }catch(Exception e){
            return FixedValue.ExceptionOccured;
        }
    }
}

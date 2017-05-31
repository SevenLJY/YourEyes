package com.example.menfer.byoureyes.YEUtils;

import com.example.menfer.byoureyes.FixedValue;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    /**
     * downloadFile() 从服务器下载文件
     * 参数：fileUrl：文件Url
     *       filePath:文件储存路径
     * 返回值为下载是够成功
     * */
    public static boolean downloadFIle(String fileUrl, String filePath){
        //返回值预设为false
        boolean result = false;
        try{
            //新建一个URL对象
            URL url = new URL(fileUrl);
            //打开一个URL连接
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            //设置连接超时和读取数据超时时间
            urlConn.setConnectTimeout(5*1000);
            urlConn.setReadTimeout(5*1000);
            //设置使用缓存
            urlConn.setUseCaches(true);
            //设置请求方式为POST
            urlConn.setRequestMethod("POST");
            //设置媒体类型
            urlConn.setRequestProperty("Contetn-type","application/json");
            //设置连接类型
            urlConn.setRequestProperty("Connection","Keep-Alive");
            //开始连接
            urlConn.connect();
            //判断是否成功
            if (urlConn.getResponseCode() == 200){
                File descFile = new File(filePath);
                FileOutputStream fos = new FileOutputStream(descFile);
                byte[] buffer = new byte[1024];
                int len;
                InputStream inputStream = urlConn.getInputStream();
                while((len = inputStream.read(buffer)) != -1){
                    //写到本地
                    fos.write(buffer,0,len);
                }
                result = true;
            }
            //断开连接
            urlConn.disconnect();
        }catch (Exception e){}
        return result;
    }
    /**
     * uploadFile()  从本地上传文件到服务器
     * 参数：baseUrl:服务器地址
     *       filePath：文件路径
     *       paramsMap：文件属性
     *       filename：上传后的文件名
     * 返回：上传是否成功
     * */
    public static boolean uploadFile(String baseUrl, String filePath,  String filename, HashMap<String,String> paramsMap){
        try{
            //新建文件对象
            File file = new File(filePath);
            //新建URL对象
            URL url = new URL(baseUrl);
            //设置连接对象
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            //设置允许连接读取、写入，不允许使用缓存，连接超时时间、读取超时时间
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setConnectTimeout(5*1000);
            urlConn.setReadTimeout(5*1000);
            //设置连接方法为POST
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Connection","Keep-Alive");
            //将上传数据设置成表单格式

            //设置文件类型
            urlConn.setRequestProperty("Content-Type","multipart/form-data; boundary=\" + \"*****");
            DataOutputStream requestStream = new DataOutputStream(urlConn.getOutputStream());
            requestStream.writeBytes("--" + "*****" + "\r\n");
            //文件参数信息
            StringBuilder tempParams = new StringBuilder();
            tempParams.append("Content-Disposition: form-data; name=\"" + filename + "\"; filename=\"" + filename + "\"; ");
            int pos = 0;
            int size=paramsMap.size();
            for (String key : paramsMap.keySet()) {
                tempParams.append(key + "=" + paramsMap.get(key));
                if (pos < size-1) {
                    tempParams.append("; ");
                }
                pos++;
            }
            tempParams.append("\r\n");
            tempParams.append("Content-Type: application/octet-stream\r\n");
            tempParams.append("\r\n");
            String params = tempParams.toString();
            requestStream.writeBytes(params);
            //发送数据文件
            FileInputStream fileInputStream = new FileInputStream(file);
            int bytesRead;
            byte[] buffer = new byte[1024];
            DataInputStream in = new DataInputStream(fileInputStream);
            while((bytesRead = in.read(buffer)) != -1){
                requestStream.write(buffer,0,bytesRead);
            }
            requestStream.writeBytes("\r\n");
            requestStream.flush();
            requestStream.writeBytes("--" + "*****" + "--" + "\r\n");
            requestStream.flush();
            fileInputStream.close();
            //判断是否连接成功
            if(200 == urlConn.getResponseCode()){
                //获取返回数据
                result = streamToString(urlConn.getInputStream());
            }else {
                result = FixedValue.ConnectionFailed;
            }
        }catch (Exception e){
            result = FixedValue.ExceptionOccured;
        }
        int intResult = Integer.parseInt(result);
        if(0 == intResult){
            return true;
        }else {
            return false;
        }
    }

}

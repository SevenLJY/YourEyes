package com.example.menfer.byoureyes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Menfer on 2017/5/3.
 * 软件中常用的固定的值以及表示用户当前状态的值
 */
public class FixedValue {
    final public static String serverIP = "http://115.159.148.212:8000/";     //服务器IP和监听端口
    final public static String ConnectionFailed = "ConnectionFailed";         //连接失败
    final public static String ExceptionOccured = "ExceptionOccured";         //出现未知异常
    final public static String photo = "photo";                                  //用户头像文件存储和上传时名称
    final public static String noInput = "NoInput";                              //输入框为空


    public static Boolean needHelp = false;
    public static String username = null;
    public static boolean pswChanged = false;   //登录密码是否被修改
}

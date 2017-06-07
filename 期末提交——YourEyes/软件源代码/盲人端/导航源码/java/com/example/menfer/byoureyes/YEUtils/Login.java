package com.example.menfer.byoureyes.YEUtils;

import com.example.menfer.byoureyes.FixedValue;

import java.util.HashMap;

/**
 * Created by Menfer on 2017/4/27.
 * 登录的工具类
 */
public class Login {

    private static int result = 0;  //存储登录结果

    /**
     * tryLogin()
     * 尝试进行登录
     * 参数：username：用户名
     *       password：密码
     * 对用户传入的用户名等进行检查，如果合法则进行登录。
     * 返回值：0：登录成功
     *         1：用户名为空
     *         2：密码为空
     *         3：用户名与密码不匹配
     *         4：网络原因登录失败
     *         5:未知异常
     */
    public static int tryLogin(String username,String password){
        result = 0;
        if(notInput(username)){
            return 1;
        }
        if(notInput(password)){
            return 2;
        }
        HashMap<String,String> paramsMap = new HashMap<String, String>();
        paramsMap.put("username",username);
        paramsMap.put("password",password);
        String tempResult = UrlUtils.doPost(FixedValue.serverIP+"blogin",paramsMap);
        if (tempResult.equals(FixedValue.ConnectionFailed)){
            result = 4;
        }else if(tempResult.equals(FixedValue.ExceptionOccured)){
            result = 5;
        }else {
            result = Integer.parseInt(tempResult);
        }
        return result;
    }

    /**
     * notInput()
     * 用户未输入
     * */
    private static boolean notInput(String str){
        if((null == str)||(0==str.length())||(""==str)){
            return true;
        }else{
            return false;
        }
    }

}

package com.example.menfer.byoureyes.YEUtils;

import com.example.menfer.byoureyes.FixedValue;

import java.util.HashMap;

/**
 * Created by Menfer on 2017/4/27.
 * 注册的工具类
 */
public class Register {
    /**
     * tryRegister()
     * 尝试进行注册
     * 参数：username：用户名
     *       password：密码
     *       confPassword：确认密码
     * 对用户传入的用户名等进行检查，如果合法则进行注册。
     * 返回值：0：注册成功
     *         1：用户名为空
     *         2：用户名不合规范
     *         3：密码为空
     *         4：密码不合规范
     *         5：密码与确认密码不同
     *         6：用户名已存在
     *         7：网络原因注册失败
     *         8:未知错误（在本类中实际为response转换为int出现异常，不提示用户）
     */

    static int result=0;  //存储注册结果


    public static int tryRegister(String username,String password,String confPassword){
        result = 0;
        //检查格式
        if (notInput(username)){
            return 1;
        }
        if(usernameBadFormat(username)){
            return 2;
        }
        if (!Check.uesrname_stringFilter(username)){
            return 2;
        }
        if (notInput(password)){
            return 3;
        }
        if (pswBadFormat(password)){
            return 4;
        }
        if (!Check.password_stringFilter(password)){
            return 4;
        }
        if(!PCequal(password,confPassword)) {
            return 5;
        }
        //开始注册的代码
        HashMap<String,String> paramsMap = new HashMap<String, String>();
        paramsMap.put("username",username);
        paramsMap.put("password",password);
        String tempResult = UrlUtils.doPost(FixedValue.serverIP+"bregister",paramsMap);
        if (tempResult.equals(FixedValue.ConnectionFailed)){
            result = 7;
        }else if(tempResult.equals(FixedValue.ExceptionOccured)){
            result = 8;
        }else {
            result = Integer.parseInt(tempResult);
        }
        return result;
    }

    /**
     * usernameBadFormat()
     * 对传入参数--用户名，检查是否格式规范
     * */
    private static boolean usernameBadFormat(String username){
        if(username.isEmpty()||username.length()<9||username.length()>16){
            return true;
        }else{
            return false;
        }
    }

    /**
     * pswBadFormat()
     * 对传入参数--密码，检查是否格式规范
     * */
    private static boolean pswBadFormat(String password){
        if(password.isEmpty()||password.length()<9||password.length()>16){
            return true;
        }else{
            return false;
        }
    }

    /**
     * PCequal()
     * 检查密码与确认密码是否相同
     * */
    private static boolean PCequal(String password,String confPassword){
        if(password.equals(confPassword)){
            return true;
        }else {
            return false;
        }
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

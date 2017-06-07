package com.example.menfer.byoureyes.YEUtils;

import com.example.menfer.byoureyes.FixedValue;

import java.util.HashMap;

/**
 * Created by Menfer on 2017/6/5.
 * 该类包括更改用户的是否需要帮助状态和更新位置信息
 */
public class NeedHelp {

    private static int result;
    /**
     * 将用户是否需要帮助的信息传至服务器
     * 参数：username：用户名
     *       need：是否需要帮助
     * 返回值：0：修改成功
     *         1：网络连接失败
     *         2：未知异常
     * */
    public static int setNeedHelp(String username, Boolean need){
        HashMap<String,String> paramsMap = new HashMap<String, String>();
        paramsMap.put("username",username);
        if (need){
            paramsMap.put("needHelp","need");
        }else {
            paramsMap.put("needHelp","notneed");
        }
        String tempResult = UrlUtils.doPost(FixedValue.serverIP+"bneedhelp",paramsMap);
        if (tempResult.equals(FixedValue.ConnectionFailed)){
            result = 1;
        }else if(tempResult.equals(FixedValue.ExceptionOccured)){
            result = 2;
        }else {
            result = Integer.parseInt(tempResult);
        }
        return result;
    }

    /**
     * 将用户位置上传至服务器
     * 参数：username：用户名
     *       latitude：纬度
     *       longitude：经度
     *返回值：0：成功
     *        1：网络连接失败
     *        2：未知异常
     * */
    public static int sendLocation(String username, double latitude, double longitude){
        HashMap<String,String> paramsMap = new HashMap<String, String>();
        String strLatitude = Double.toString(latitude);
        String strLongitude = Double.toHexString(longitude);
        paramsMap.put("username",username);
        paramsMap.put("latitude",strLatitude);
        paramsMap.put("longitude",strLongitude);
        String tempResult = UrlUtils.doPost(FixedValue.serverIP+"bnewlocation",paramsMap);
        if (tempResult.equals(FixedValue.ConnectionFailed)){
            result = 1;
        }else if(tempResult.equals(FixedValue.ExceptionOccured)){
            result = 2;
        }else {
            result = Integer.parseInt(tempResult);
        }
        return result;
    }


}

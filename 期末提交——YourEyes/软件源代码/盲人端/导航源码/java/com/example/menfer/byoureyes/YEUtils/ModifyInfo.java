package com.example.menfer.byoureyes.YEUtils;

import com.example.menfer.byoureyes.FixedValue;

import java.util.HashMap;

/**
 * Created by Menfer on 2017/4/27.
 * 修改信息工具类
 */
public class ModifyInfo {

    private static int result = 0;
    private static int newAge = -1;

    /**
     * tryModify()
     * 尝试修改信息
     * 参数：gender：性别编号，1为男，2为女
     *      phonenumber：电话号码
     *      age：年龄
     *返回值：0：修改成功
     *        1；年龄格式错误
     *        2：手机号码格式错误
     *        3：网络连接失败
     *        4：异常错误
     */

    public static int tryModify(String username, String age, String phoneNumber, String gender){
        if (username == null){
            return 4;
        }
       try {
           newAge = Integer.parseInt(age);
           if(newAge<10||newAge>100){
               return 1;
           }
       }catch(Exception e){
           return 1;
       }
        if(notInput(phoneNumber)){
            phoneNumber = "null";
        }
        if(phoneBadFormat(phoneNumber)){
            return 2;
        }
        if (!Check.phoneNumber_stringFilter(phoneNumber)){
            return 2;
        }
        HashMap<String,String> paramsMap = new HashMap<String, String>();
        paramsMap.put("age",age);
        paramsMap.put("gender",gender);
        paramsMap.put("phoneNumber",phoneNumber);
        paramsMap.put("username",username);
        String tempResult = UrlUtils.doPost(FixedValue.serverIP+"bmodifyInfo",paramsMap);
        if (tempResult.equals(FixedValue.ConnectionFailed)){
            result = 3;
        }else if(tempResult.equals(FixedValue.ExceptionOccured)){
            result = 4;
        }else {
            result = Integer.parseInt(tempResult);
        }
        return result;
    }

    /**
     * phoneBadFormat()
     * 对传入参数--电话号码，检查是否格式规范
     * */
    private static boolean phoneBadFormat(String phoneNumber){
        if(phoneNumber.isEmpty()||phoneNumber.length()!=11){
            return true;
        }else{
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

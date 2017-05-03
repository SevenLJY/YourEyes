package com.example.menfer.byoureyes.YEUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Menfer on 2017/5/3.
 * 用正则表达式检查输入框内内容是否规范
 */
public class Check {
    static String returnstr = null;

    public static boolean uesrname_stringFilter(String str)throws PatternSyntaxException{
        String pattern = "^[a-zA-Z0-9_]{9,16}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    public static boolean password_stringFilter(String str)throws PatternSyntaxException{
        String pattern = "^[a-zA-Z0-9_]{9,16}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }
}

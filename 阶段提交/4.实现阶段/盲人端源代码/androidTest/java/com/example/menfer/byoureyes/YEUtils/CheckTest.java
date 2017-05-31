package com.example.menfer.byoureyes.YEUtils;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Menfer on 2017/5/31.
 */
public class CheckTest extends TestCase{

    @Test
    public void testUesrname_stringFilter() throws Exception {
        assertFalse("username_stringFilter failed", Check.uesrname_stringFilter("123"));  //长度太小
        assertFalse("username_stringFilter failed", Check.uesrname_stringFilter("12345678901234567"));    //长度过大
        assertFalse("username_stringFilter failed", Check.uesrname_stringFilter("*1234567891234"));   //包含非法字符
        assertTrue("username_stringFilter failed", Check.uesrname_stringFilter("12345678912345_"));    //正确格式
    }

    @Test
    public void testPassword_stringFilter() throws Exception {
        assertFalse("password_stringFilter failed", Check.password_stringFilter("123"));        //密码过短
        assertFalse("password_stringFilter failed", Check.password_stringFilter("12345678901234567"));         //密码过长
        assertFalse("password_stringFilter failed", Check.password_stringFilter("*1234567891234"));       //包含非法字符
        assertTrue("password_stringFilter failed", Check.password_stringFilter("12345678912345_"));        //正确格式
    }

    @Test
    public void testPhoneNumber_stringFilter() throws Exception {
        assertFalse("phoneNumber_stringFilter failed", Check.phoneNumber_stringFilter("123"));      //长度过小
        assertFalse("phoneNumber_stringFilter failed", Check.phoneNumber_stringFilter("138999988889"));   //长度过大
        assertFalse("phoneNumber_stringFilter failed", Check.phoneNumber_stringFilter("123a8889999"));     //包含非法字符
        assertTrue("phoneNumber_stringFilter failed", Check.phoneNumber_stringFilter("17712345678"));      //正确格式
    }
}
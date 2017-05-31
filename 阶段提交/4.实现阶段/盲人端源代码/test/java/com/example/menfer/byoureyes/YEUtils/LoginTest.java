package com.example.menfer.byoureyes.YEUtils;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Menfer on 2017/5/31.
 */
public class LoginTest extends TestCase{

    @Test
    public void testTryLogin() throws Exception {
        assertEquals("tryLogin failed", Login.tryLogin("", "123456"),1);      //未填写用户名
        assertEquals("tryLogin failed", Login.tryLogin("123456", ""),2);      //未填写密码
        assertEquals("tryLogin failed", Login.tryLogin("123456789666", "123456789666"),3);      //数据库中无该用户，即不匹配
        assertEquals("tryLogin failed", Login.tryLogin("123456789", "1234567890"),0);           //正确登录信息
    }
}
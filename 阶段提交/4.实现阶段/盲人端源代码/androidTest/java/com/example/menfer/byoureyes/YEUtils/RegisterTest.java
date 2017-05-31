package com.example.menfer.byoureyes.YEUtils;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Menfer on 2017/5/31.
 */
public class RegisterTest extends TestCase{

    @Test
    public void testTryRegister() throws Exception {
        assertEquals("tryRegister Failed",1,Register.tryRegister("","12345654321","12345654321"));   //用户名为空
        assertEquals("tryRegister Failed",2,Register.tryRegister("123","12345654321","12345654321"));   //用户名不合规范
        assertEquals("tryRegister Failed",2,Register.tryRegister("1234567890123456789","12345654321","12345654321"));   //用户名不合规范
        assertEquals("tryRegister Failed",3,Register.tryRegister("123456789","","12345654321"));   //密码为空
        assertEquals("tryRegister Failed",4,Register.tryRegister("123456789","123","12345654321"));   //密码不规范
        assertEquals("tryRegister Failed",4,Register.tryRegister("123456789","12345678901234567","12345654321"));   //密码不规范
        assertEquals("tryRegister Failed",5,Register.tryRegister("123456789","123456789","1234567890"));   //密码不与确认密码不同
        assertEquals("tryRegister Failed",6,Register.tryRegister("123456789","123456789","123456789"));   //用户名已存在
        assertEquals("tryRegister Failed",0,Register.tryRegister("123456789666","123456789","123456789"));   //注册成功


    }
}
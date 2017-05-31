package com.example.menfer.byoureyes.YEUtils;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Menfer on 2017/5/31.
 */
public class ModifyPasswordTest extends TestCase{

    @Test
    public void testTryModify() throws Exception {
        //注意，改密码时传入参数需随时更换，密码是否正确影响了测试
        assertEquals("tryModify failed",1,ModifyPassword.tryModify("","123456789","1234567890","1234567890"));  //用户名为空
        assertEquals("tryModify failed",2,ModifyPassword.tryModify("123456789","","1234567890","1234567890"));  //原密码为空
        assertEquals("tryModify failed",3,ModifyPassword.tryModify("123456789","123456789","",""));  //新密码为空
        assertEquals("tryModify failed",4,ModifyPassword.tryModify("123456789","123456789","123456","123456"));  //新密码不规范
        assertEquals("tryModify failed",5,ModifyPassword.tryModify("123456789","123456789","1234567890","123456789"));  //新密码与确认密码不匹配
        assertEquals("tryModify failed",6,ModifyPassword.tryModify("123456789","123456789","1234567890","1234567890"));  //用户名与密码不匹配
        assertEquals("tryModify failed",0,ModifyPassword.tryModify("123456789","1234567890","123456789","123456789"));  //修改成功
    }
}
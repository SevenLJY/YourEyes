package com.example.menfer.byoureyes.YEUtils;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Menfer on 2017/5/31.
 */
public class ModifyInfoTest extends TestCase{

    @Test
    public void testTryModify() throws Exception {
        assertEquals("tryModify Failed", 1, ModifyInfo.tryModify("123456789","0","17788889999","male"));    //年龄过小
        assertEquals("tryModify Failed", 1, ModifyInfo.tryModify("123456789","200","17788889999","male"));    //年龄过大
        assertEquals("tryModify Failed",2, ModifyInfo.tryModify("123456789","30","1778888999","male"));    //手机号码过短
        assertEquals("tryModify Failed",2, ModifyInfo.tryModify("123456789","30","177888899998","male"));    //手机号码过长
        //注意，下一测试每次的内容不可相同，即修改信息的内容不允许与原有信息完全相同，后三项中至少有一项不同
        assertEquals("tryModify Failed", 0, ModifyInfo.tryModify("123456789","30","17788889999","male"));   //修改成功
    }
}
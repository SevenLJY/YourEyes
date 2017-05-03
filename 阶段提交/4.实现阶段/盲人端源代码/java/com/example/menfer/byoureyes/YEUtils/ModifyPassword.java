package com.example.menfer.byoureyes.YEUtils;

/**
 * Created by Menfer on 2017/4/27.
 * 修改密码工具类
 */
public class ModifyPassword {

    private static int result = 0;

    /**
     * tryModify()
     * 尝试修改密码
     * 参数：username：用户名
     *       password：原密码
     *       newPassword：新密码
     *       confPassword：确认密码
     *返回值：0：修改成功
     *        1：用户名为空
     *        2：原密码为空
     *        3：新密码为空
     *        4：新密码不规范
     *        5：新密码与确认密码不同
     *        6：用户名与密码不匹配
     *        7：网络原因修改失败
     */

    public static int tryModify(String username, String password, String newPassword, String confPassword){
        if(notInput(username)){
            return 1;
        }
        if(notInput(password)){
            return 2;
        }
        if(notInput(newPassword)){
            return 3;
        }
        if(pswBadFormat(newPassword)){
            return 4;
        }
        if(!PCequal(newPassword,confPassword)){
            return 5;
        }
        //开始修改密码
        return result;
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

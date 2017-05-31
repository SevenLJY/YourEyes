package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.ModifyPassword;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;

/**
 * Created by Menfer on 2017/5/4.
 */
public class ModifyPasswordActivity extends Activity {
    private EditText et_username;
    private EditText et_password;
    private EditText et_newPassword;
    private EditText et_confPassword;
    private Button btn_modifyPassword;

    private String username;
    private String password;
    private String newPassword;
    private String confPassword;

    int result = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x126){
                test(result);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypassword);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password =(EditText)findViewById(R.id.et_password);
        et_newPassword = (EditText)findViewById(R.id.et_newPassword);
        et_confPassword = (EditText)findViewById(R.id.et_confpassword);
        btn_modifyPassword = (Button)findViewById(R.id.btn_modifyPassword);
        btn_modifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                newPassword = et_newPassword.getText().toString();
                confPassword = et_confPassword.getText().toString();
                new Thread(){
                    @Override
                    public void run() {
                        result = ModifyPassword.tryModify(username,password,newPassword,confPassword);
                        handler.sendEmptyMessage(0x126);
                    }
                }.start();
            }
        });
    }

    /**
     * test()
     * 根据登录结果进行操作
     * */
    private void test(int result) {
        switch (result) {
            case 0:
                ToastUtil.show(ModifyPasswordActivity.this, "修改密码成功，请返回登录"); //之后跳转到相应界面
                break;
            case 1:
                ToastUtil.show(ModifyPasswordActivity.this, "请输入用户名");
                break;
            case 2:
                ToastUtil.show(ModifyPasswordActivity.this, "请输入原密码");
                break;
            case 3:
                ToastUtil.show(ModifyPasswordActivity.this, "请输入新密码");
                break;
            case 4:
                ToastUtil.show(ModifyPasswordActivity.this, "新密码不合规范");
                break;
            case 5:
                ToastUtil.show(ModifyPasswordActivity.this, "新密码与确认密码不相同");
                break;
            case 6:
                ToastUtil.show(ModifyPasswordActivity.this, "用户名或原密码错误");
                break;
            case 7:
                ToastUtil.show(ModifyPasswordActivity.this, "网络连接失败");
                break;
            case 8:
                ToastUtil.show(ModifyPasswordActivity.this, "未知异常");
                break;
            default:
                break;
        }
    }
}

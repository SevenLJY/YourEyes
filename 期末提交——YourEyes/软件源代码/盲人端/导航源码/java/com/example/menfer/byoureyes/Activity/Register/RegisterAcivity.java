package com.example.menfer.byoureyes.Activity.Register;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.Register;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;

/**
 * Created by Menfer on 2017/4/27.
 */
public class RegisterAcivity extends Activity {
    EditText et_username;
    EditText et_password;
    EditText et_confpassword;
    Button btn_register;

    String username=null;
    String password = null;
    String confpassword = null;

    int result = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x124){
                test(result);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        et_confpassword = (EditText)findViewById(R.id.et_confpassword);
        btn_register = (Button)findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                confpassword = et_confpassword.getText().toString();
                //result = Register.tryRegister(username,password,confpassword);
                //test(result);
                //新开线程完成注册
                new Thread(){
                    @Override
                    public void run() {
                        result = Register.tryRegister(username,password,confpassword);
                        handler.sendEmptyMessage(0x124);
                    }
                }.start();
            }
        });

    }

    /**
     * test()方法根据注册返回的结果进行相应提示
     * */
    private void test(int result){
        switch(result){
            case 0:
                ToastUtil.show(RegisterAcivity.this,"注册成功  请返回登录");
                break;
            case 1:
                ToastUtil.show(RegisterAcivity.this,"请输入用户名");
                break;
            case 2:
                ToastUtil.show(RegisterAcivity.this,"用户名不合规范");
                break;
            case 3:
                ToastUtil.show(RegisterAcivity.this,"请输入密码");
                break;
            case 4:
                ToastUtil.show(RegisterAcivity.this,"密码不合规范");
                break;
            case 5:
                ToastUtil.show(RegisterAcivity.this,"密码与确认密码不同");
                break;
            case 6:
                ToastUtil.show(RegisterAcivity.this,"用户名已存在");
                break;
            case 7:
                ToastUtil.show(RegisterAcivity.this,"网络连接失败");
                break;
            case 8:
                ToastUtil.show(RegisterAcivity.this,"出现未知异常");
                break;
            default:
                break;
        }
    }
}

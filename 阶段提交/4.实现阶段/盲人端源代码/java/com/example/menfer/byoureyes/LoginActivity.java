package com.example.menfer.byoureyes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Text;
import com.example.menfer.byoureyes.YEUtils.Login;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;

/**
 * Created by Menfer on 2017/4/27.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_register;
    private TextView tv_modifyPassword;
    private String username = "";
    private String password = "";
    private int result=0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x125){
                test(result);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_register = (TextView)findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        tv_modifyPassword = (TextView)findViewById(R.id.tv_modifyPassword);
        tv_modifyPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                new Thread(){
                    @Override
                    public void run() {
                        result = Login.tryLogin(username,password);
                        handler.sendEmptyMessage(0x125);
                    }
                }.start();
                break;
            case R.id.tv_register:
                Intent intentToRegister = new Intent(LoginActivity.this,RegisterAcivity.class);
                startActivity(intentToRegister);
                break;
            case R.id.tv_modifyPassword:
                Intent intentToModify = new Intent(LoginActivity.this,ModifyPasswordActivity.class);
                startActivity(intentToModify);
                break;
            default:
                break;
        }
    }

    /**
     * test()
     * 根据登录结果进行操作
     * */
    private void test(int result){
        switch(result){
            case 0:
                ToastUtil.show(LoginActivity.this,"登录成功"); //之后跳转到相应界面
                break;
            case 1:
                ToastUtil.show(LoginActivity.this,"请输入用户名");
                break;
            case 2:
                ToastUtil.show(LoginActivity.this,"请输入密码");
                break;
            case 3:
                ToastUtil.show(LoginActivity.this,"用户名或密码错误");
                break;
            case 4:
                ToastUtil.show(LoginActivity.this,"网络连接失败");
                break;
            case 5:
                ToastUtil.show(LoginActivity.this,"未知异常");
                break;
            default:
                break;
        }
    }
}

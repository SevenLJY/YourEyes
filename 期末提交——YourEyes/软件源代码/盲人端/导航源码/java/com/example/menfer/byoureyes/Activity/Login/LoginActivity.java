package com.example.menfer.byoureyes.Activity.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.menfer.byoureyes.Activity.MapActivity;
import com.example.menfer.byoureyes.Activity.ModifyPassword.ModifyPasswordActivity;
import com.example.menfer.byoureyes.Activity.Register.RegisterAcivity;
import com.example.menfer.byoureyes.FixedValue;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.Login;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;

/**
 * Created by Menfer on 2017/4/27.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean rememberPassword;

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_register;
    private TextView tv_modifyPassword;
    private CheckBox cb_rememberPassword;
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
        //初始化SharedPreferences
        preferences = getSharedPreferences("YoureyesLogin",MODE_PRIVATE);
        editor = preferences.edit();
        //初始化组件
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_register = (TextView)findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        tv_modifyPassword = (TextView)findViewById(R.id.tv_modifyPassword);
        tv_modifyPassword.setOnClickListener(this);
        cb_rememberPassword = (CheckBox)findViewById(R.id.cb_rememberPassword);
        rememberPassword = preferences.getBoolean("rememberPassword",false);
        if(rememberPassword){
            cb_rememberPassword.setChecked(true);
            username = preferences.getString("username",null);
            password = preferences.getString("password",null);
            et_username.setText(username);
            et_password.setText(password);
        }
        //记住密码选框的监听
        cb_rememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    rememberPassword = true;
                }else {
                    rememberPassword = false;
                }
            }
        });

        et_password.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //VoiceUtil.voiceInput(LoginActivity.this, et_password);
                return false;
            }
        });

        et_username.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //VoiceUtil.voiceInput(LoginActivity.this, et_username);
                return false;
            }
        });

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
                //检查是否选中记住密码进行相关存储操作
                if(rememberPassword){
                    editor.putBoolean("rememberPassword",true);
                    editor.putString("username",username);
                    editor.putString("password",password);
                }else {
                    editor.putBoolean("rememberPassword",false);
                    editor.remove("username");
                    editor.remove("password");
                }
                editor.commit();
                //ToastUtil.show(LoginActivity.this,"登录成功"); //之后跳转到相应界面
                FixedValue.username = username;
                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(intent);
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

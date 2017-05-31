package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.menfer.byoureyes.FixedValue;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.ModifyInfo;
import com.example.menfer.byoureyes.YEUtils.Register;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;

/**
 * Created by Menfer on 2017/5/30.
 */
public class ModifyInfoActivity extends Activity {

    EditText et_age;
    EditText et_phone;
    RadioGroup rg_gender;
    Button btn_modify;

    String phoneNumber;
    String age;
    String gender;

    int result = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){
                test(result);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        et_age = (EditText)findViewById(R.id.et_age);
        et_phone = (EditText)findViewById(R.id.et_phone);
        rg_gender = (RadioGroup)findViewById(R.id.rg_gender);
        btn_modify = (Button)findViewById(R.id.btn_modify);

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = FixedValue.username;
                age = et_age.getText().toString();
                phoneNumber = et_phone.getText().toString();
                if(rg_gender.getCheckedRadioButtonId() == R.id.rb_male){
                    gender = "male";
                }else {
                    gender = "female";
                }
                new Thread(){
                    @Override
                    public void run() {
                        result = ModifyInfo.tryModify(username, age, phoneNumber, gender);
                        handler.sendEmptyMessage(0x123);
                    }
                }.start();
            }
        });
    }

    private void test(int result) {
        switch (result) {
            case 0:
                ToastUtil.show(ModifyInfoActivity.this, "修改成功");
                break;
            case 1:
                ToastUtil.show(ModifyInfoActivity.this, "年龄输入有误");
                break;
            case 2:
                ToastUtil.show(ModifyInfoActivity.this, "手机号码不合规范");
                break;
            case 3:
                ToastUtil.show(ModifyInfoActivity.this, "网络连接失败");
                break;
            case 4:
                ToastUtil.show(ModifyInfoActivity.this, "出现未知异常，您可能尚未登录");
                break;
            default:
                break;
        }
    }
}

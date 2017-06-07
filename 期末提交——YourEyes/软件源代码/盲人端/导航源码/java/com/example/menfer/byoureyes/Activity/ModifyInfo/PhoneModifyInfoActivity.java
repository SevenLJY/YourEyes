package com.example.menfer.byoureyes.Activity.ModifyInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.menfer.byoureyes.Activity.Login.PasswordLoginActivity;
import com.example.menfer.byoureyes.Activity.MapActivity;
import com.example.menfer.byoureyes.FixedValue;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.Check;
import com.example.menfer.byoureyes.YEUtils.Login;
import com.example.menfer.byoureyes.YEUtils.ModifyInfo;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;

/**
 * Created by Menfer on 2017/6/1.
 */
public class PhoneModifyInfoActivity extends Activity implements GestureDetector.OnGestureListener{
    GestureDetector detector;
    EditText et_phone;
    VoiceUtil voiceUtil;
    String gender;
    String age;
    String phone = null;
    int result = 0;
    String message = "修改电话号码，右滑语音输入，左滑删除，，上滑确认，下滑返回。强烈建议您在家人朋友的帮助下进行修改信息。";

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
        SpeechUtility.createUtility(PhoneModifyInfoActivity.this, SpeechConstant.APPID+"=58593564");
        setContentView(R.layout.activity_phone);
        et_phone = (EditText)findViewById(R.id.et_phone);
        voiceUtil = new VoiceUtil(PhoneModifyInfoActivity.this);
        voiceUtil.speak(message);
        Intent intent = getIntent();
        age = intent.getExtras().getString("age");
        gender = intent.getExtras().getString("gender");
        detector = new GestureDetector(this, this);
    }

    /**
     * test()方法根据登录返回的结果进行相应提示
     * */
    private void test(int result){
        switch(result){
            case 0:
                voiceUtil.stopSpeak();
                voiceUtil.speak("修改成功。");
                break;
            case 1:
                //不存在1的情况
                break;
            case 2:
                //不存在2的情况
                break;
            case 3:
                voiceUtil.stopSpeak();
                voiceUtil.speak("网络连接失败，请稍后重试");
                break;
            case 4:
                voiceUtil.stopSpeak();
                voiceUtil.speak("出现未知异常，请稍后重试");
                break;
            default:
                break;
        }
    }

    /**
     * 将点击事件交给detector处理
     * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    //抽象函数实现，无具体意义
    @Override
    public void onShowPress(MotionEvent motionEvent) {}

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if(motionEvent.getX()-motionEvent1.getX()>100){
            //向左滑的操作 删除
            try
            {
                String keyCommand = "input keyevent " + KeyEvent.KEYCODE_DEL;
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(keyCommand);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
            }
        }else if(motionEvent.getX()-motionEvent1.getX()<-100){
            //向右滑的操作 语音输入
            voiceUtil.voiceInput(et_phone);
        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作 确认
            phone = et_phone.getText().toString();
            voiceUtil.stopSpeak();
            voiceUtil.speak("输入的电话号码为,[n1]"+phone + ",确认请长[=chang2]按屏幕");
        }else if(motionEvent.getY()-motionEvent1.getY()<-100) {
            //向下滑的操作，返回
            try
            {
                String keyCommand = "input keyevent " + KeyEvent.KEYCODE_BACK;
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(keyCommand);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        phone = et_phone.getText().toString();
        if(phone == null){
            voiceUtil.stopSpeak();
            voiceUtil.speak("尚未输入电话号码。");
        }else {
            try{
                if(!Check.phoneNumber_stringFilter(phone)){
                    voiceUtil.stopSpeak();
                    voiceUtil.speak("电话号码输入不规范，请输入11位数字");
                }else {
                    if (FixedValue.username == null){
                        voiceUtil.stopSpeak();
                        voiceUtil.speak("您尚未登录，请登录后执行此操作。");
                    }else {
                        new Thread(){
                            @Override
                            public void run() {
                                result = ModifyInfo.tryModify(FixedValue.username,age,phone,gender);
                                handler.sendEmptyMessage(0x124);
                            }
                        }.start();
                    }
                }
            }catch (Exception e){
                voiceUtil.stopSpeak();
                voiceUtil.speak("出现未知异常。请稍后重试。");
            }
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    protected void onDestroy() {
        voiceUtil.stopSpeak();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        voiceUtil.stopSpeak();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (voiceUtil == null){
            voiceUtil = new VoiceUtil(PhoneModifyInfoActivity.this);
        }
        voiceUtil.speak(message);
    }
}

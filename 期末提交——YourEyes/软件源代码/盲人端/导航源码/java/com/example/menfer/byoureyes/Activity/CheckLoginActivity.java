package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.menfer.byoureyes.Activity.Login.UsernameLoginActivity;
import com.example.menfer.byoureyes.FixedValue;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.Login;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;

/**
 * Created by Menfer on 2017/6/3.
 * 检查用户是否已经登录
 * 如已经登录则直接帮助用户以旧用户名登录
 */
public class CheckLoginActivity extends Activity implements GestureDetector.OnGestureListener{
    SharedPreferences preferences;
    String username;
    String password;
    String message;
    VoiceUtil voiceUtil;
    GestureDetector detector;
    int result = 1;

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
        setContentView(R.layout.activity_check_login);
        voiceUtil = new VoiceUtil(CheckLoginActivity.this);
        SpeechUtility.createUtility(CheckLoginActivity.this, SpeechConstant.APPID+"=58593564");
        preferences = getSharedPreferences("YoureyesUse", MODE_PRIVATE);
        detector = new GestureDetector(this, this);
        username = preferences.getString("username",null);
        password = preferences.getString("password",null);
        message =  "检测到已有用户名，"+username+",上滑使用该用户名登录，右滑重新输入，下滑返回。";
        if (username == null){
            Intent intent = new Intent(CheckLoginActivity.this, UsernameLoginActivity.class);
            startActivity(intent);
        }else {
            voiceUtil.speak(message);
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
            //向右滑的操作 跳转到登录用户名输入
            Intent intent = new Intent(CheckLoginActivity.this,UsernameLoginActivity.class);
            startActivity(intent);
        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作 确认 进行登录
            new Thread(){
                @Override
                public void run() {
                    result = Login.tryLogin(username,password);
                    handler.sendEmptyMessage(0x125);
                }
            }.start();
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

    /**
     * test()
     * 根据登录结果进行操作
     * */
    private void test(int result){
        switch(result){
            case 0:
                //检查是否选中记住密码进行相关存储操作
                FixedValue.username = username;
                Intent intent = new Intent(CheckLoginActivity.this, MapActivity.class);
                startActivity(intent);
                break;
            case 1:
                voiceUtil.stopSpeak();
                voiceUtil.speak("出现异常，已跳转至用户名输入界面");
                Intent intent1 = new Intent(CheckLoginActivity.this, UsernameLoginActivity.class);
                startActivity(intent1);
                break;
            case 2:
                voiceUtil.stopSpeak();
                voiceUtil.speak("出现异常，已跳转至用户名输入界面");
                Intent intent2 = new Intent(CheckLoginActivity.this, UsernameLoginActivity.class);
                startActivity(intent2);
                break;
            case 3:
                FixedValue.pswChanged = true;
                Intent intent3 = new Intent(CheckLoginActivity.this, UsernameLoginActivity.class);
                startActivity(intent3);
                break;
            case 4:
                voiceUtil.stopSpeak();
                voiceUtil.speak("网络连接失败");
                break;
            case 5:
                voiceUtil.stopSpeak();
                voiceUtil.speak("未知异常");
                break;
            default:
                break;
        }
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        //长按无效
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
            voiceUtil = new VoiceUtil(CheckLoginActivity.this);
        }
        voiceUtil.speak(message);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}

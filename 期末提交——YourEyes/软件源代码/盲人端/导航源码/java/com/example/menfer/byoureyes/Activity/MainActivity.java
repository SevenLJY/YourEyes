package com.example.menfer.byoureyes.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;

import com.example.menfer.byoureyes.Activity.ModifyPassword.UsernameModifypasswordActivity;
import com.example.menfer.byoureyes.Activity.Register.UsernameRegisterActivity;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    //检测是否是第一次使用该软件
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean use_first;
    //手势检测实例
    GestureDetector detector;
    //语音对象
    VoiceUtil voiceUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeechUtility.createUtility(MainActivity.this, SpeechConstant.APPID+"=58593564");
        detector = new GestureDetector(this, this);
        voiceUtil = new VoiceUtil(MainActivity.this);
        //初始化SharedPreferences
        preferences = getSharedPreferences("YoureyesUse",MODE_PRIVATE);
        editor = preferences.edit();

        use_first = preferences.getBoolean("use_first_time",true);

        if(use_first){
            //第一次使用软件的操作
            //将使用情况设置为非第一次使用
            editor.putBoolean("use_first_time", false);
            editor.commit();
            String use_first_warning = "欢迎使用 Your Eyes ，检测到您是第一次使用该软件，强烈建议您向右滑动进入菜单，帮助，来更好的使用该软件。向上滑动进行注册，向下滑动进行登录，向左滑动修改密码。";
            //voiceUtil.speak(use_first_warning);
        }else {
            //不是第一次使用软件的操作
            String message = "向上滑动进行注册，向下滑动进入登录，向左滑动修改密码，向右滑动进入菜单";
            //voiceUtil.speak(message);
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
            //向左滑的操作,进入修改密码
            Intent intent = new Intent(MainActivity.this, UsernameModifypasswordActivity.class);
            startActivity(intent);
        }else if(motionEvent.getX()-motionEvent1.getX()<-100){
            //向右滑的操作，进入菜单
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作，进入注册界面
            Intent intent2 = new Intent(MainActivity.this, UsernameRegisterActivity.class);
            startActivity(intent2);
        }else if(motionEvent.getY()-motionEvent1.getY()<-100) {
            //向下滑的操作，进入登录界面
            Intent intent3 = new Intent(MainActivity.this, CheckLoginActivity.class);
            startActivity(intent3);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

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
            voiceUtil = new VoiceUtil(MainActivity.this);
        }
        String message = "向上滑动进行注册，向下滑动进入登录，向左滑动修改密码，向右滑动进入菜单";
        voiceUtil.speak(message);
    }
}

package com.example.menfer.byoureyes.Activity.ModifyPassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.Check;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;

/**
 * Created by Menfer on 2017/6/1.
 */
public class UsernameModifypasswordActivity extends Activity implements GestureDetector.OnGestureListener{
    EditText et_username;
    GestureDetector detector;
    VoiceUtil voiceUtil;

    String message = "修改密码。请输入用户名，右滑语音输入，左滑删除，上滑确认，下滑返回.强烈建议您在家人朋友的帮助下进行修改。";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpeechUtility.createUtility(UsernameModifypasswordActivity.this, SpeechConstant.APPID+"=58593564");
        setContentView(R.layout.activity_username_register);
        voiceUtil = new VoiceUtil(UsernameModifypasswordActivity.this);
        voiceUtil.speak(message);
        detector = new GestureDetector(this, this);
        et_username = (EditText)findViewById(R.id.et_username);
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
        voiceUtil.stopSpeak();
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
            voiceUtil.voiceInput(et_username);
        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作 确认
            String username = et_username.getText().toString();
            voiceUtil.stopSpeak();
            voiceUtil.speak("输入的用户名为"+ "[n1]"+username + ",确认请长[=chang2]按屏幕");
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
        voiceUtil.stopSpeak();
        String username = et_username.getText().toString();
        if(!Check.uesrname_stringFilter(username)){
            voiceUtil.stopSpeak();
            voiceUtil.speak("用户名不合规范，请输入9到16位数字、字母或下划线。");
        }else {
            Bundle data = new Bundle();
            data.putString("username",username);
            Intent intent = new Intent(UsernameModifypasswordActivity.this, PasswordModifypasswordActivity.class);
            intent.putExtras(data);
            startActivity(intent);
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
            voiceUtil = new VoiceUtil(UsernameModifypasswordActivity.this);
        }
        voiceUtil.speak(message);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        voiceUtil.stopSpeak();
        if (voiceUtil == null){
            voiceUtil = new VoiceUtil(UsernameModifypasswordActivity.this);
        }
        voiceUtil.speak(message);

    }
}

package com.example.menfer.byoureyes.Activity.ModifyPassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.Check;
import com.example.menfer.byoureyes.YEUtils.ModifyPassword;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;

/**
 * Created by Menfer on 2017/6/1.
 */
public class NewpasswordModifypasswordActivity extends Activity implements GestureDetector.OnGestureListener{
    EditText et_newPassword;
    GestureDetector detector;
    VoiceUtil voiceUtil;
    String username;
    String password;
    String newPassword;

    int result = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x124){
                test(result);
            }
        }
    };

    String message = "修改密码。请输入新密码，右滑语音输入，左滑删除，上滑确认，下滑返回.强烈建议您在家人朋友的帮助下进行修改。";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpeechUtility.createUtility(NewpasswordModifypasswordActivity.this, SpeechConstant.APPID+"=58593564");
        setContentView(R.layout.activity_newpassword_modifypassword);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        password = intent.getExtras().getString("password");
        voiceUtil = new VoiceUtil(NewpasswordModifypasswordActivity.this);
        voiceUtil.speak(message);
        detector = new GestureDetector(this, this);
        et_newPassword = (EditText)findViewById(R.id.et_newpasswordInModify);
    }

    /**
     * test()方法根据返回的结果进行相应提示
     * */
    private void test(int result){
        switch(result){
            case 0:
                voiceUtil.stopSpeak();
                voiceUtil.speak("修改成功，请返回登录。");
                break;
            case 1:
                //新修改方式不存在1的情况
                break;
            case 2:
                //新修改方式不存在2的情况
                break;
            case 3:
                //新修改方式不存在3的情况
                break;
            case 4:
                //新修改方式不存在4的情况
                break;
            case 5:
                //新修改方式不存在5的情况
                break;
            case 6:
                //新修改方式不存在6的情况
                break;
            case 7:
                voiceUtil.stopSpeak();
                voiceUtil.speak("网络连接失败，请稍后重试。");
                break;
            case 8:
                voiceUtil.stopSpeak();
                voiceUtil.speak("出现未知异常");
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
            voiceUtil.voiceInput(et_newPassword);
        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作 确认
            String newPassword = et_newPassword.getText().toString();
            voiceUtil.stopSpeak();
            voiceUtil.speak("输入的新密码为"+ "[n1]"+ newPassword + ",确认修改请长[=chang2]按屏幕");
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
        newPassword = et_newPassword.getText().toString();
        if(!Check.password_stringFilter(newPassword)){
            voiceUtil.stopSpeak();
            voiceUtil.speak("新密码不合规范，请输入9到16位数字、字母或下划线。");
        }else {
            new Thread(){
                @Override
                public void run() {
                    result = ModifyPassword.tryModify(username,password,newPassword,newPassword);  //减少了确认密码这一步;
                    handler.sendEmptyMessage(0x124);
                }
            }.start();
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
            voiceUtil = new VoiceUtil(NewpasswordModifypasswordActivity.this);
        }
        voiceUtil.speak(message);
    }
}

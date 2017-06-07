package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Menfer on 2017/5/30.
 * 帮助界面可给用户一些使用时的提示
 * 暂时没有相关提示，留待拓展
 */
public class HelpActivity extends Activity implements GestureDetector.OnGestureListener{
    GestureDetector detector;
    String message = "欢迎使用帮助。您可在菜单中进入该界面,下滑返回。该软件支持语音输入，但存在识别错误的风险，建议您在亲人朋友的帮助下进行账户操作。";
    VoiceUtil voiceUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        SpeechUtility.createUtility(HelpActivity.this, SpeechConstant.APPID+"=58593564");
        voiceUtil = new VoiceUtil(HelpActivity.this);
        voiceUtil.stopSpeak();
        voiceUtil.speak(message);
        detector = new GestureDetector(this, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if(motionEvent.getX()-motionEvent1.getX()>100){
            //向左滑的操作,无
        }else if(motionEvent.getX()-motionEvent1.getX()<-100){

        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作,无
        }else if(motionEvent.getY()-motionEvent1.getY()<-100) {
            //向下滑的操作，返回
            finish();
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }
}

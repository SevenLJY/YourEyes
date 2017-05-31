package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.menfer.byoureyes.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Menfer on 2017/5/30.
 */
public class HelpActivity extends Activity implements GestureDetector.OnGestureListener{
    GestureDetector detector;
    String message = "欢迎使用帮助，您可以在任何界面下左滑进入菜单界面，随后上滑进入帮助界面。在语音输入时，说输入完成即完成输入，说退格可删去一个字符，右滑退出帮助界面";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        SpeechUtility.createUtility(HelpActivity.this, SpeechConstant.APPID+"=58593564");
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
            //向右滑的操作，返回
            finish();
        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作,无
        }else if(motionEvent.getY()-motionEvent1.getY()<-100) {
            //向下滑的操作，无
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

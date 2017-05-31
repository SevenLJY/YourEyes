package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;

import com.example.menfer.byoureyes.FixedValue;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Menfer on 2017/5/30.
 */
public class MenuActivity extends Activity implements GestureDetector.OnGestureListener {
    GestureDetector detector;
    String message = "菜单界面，上滑帮助，下滑修改信息，右滑返回，长按切换是否需要帮助";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SpeechUtility.createUtility(MenuActivity.this, SpeechConstant.APPID+"=58593564");
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
            //向上滑的操作，进入帮助界面
            Intent intent2 = new Intent(MenuActivity.this, HelpActivity.class);
            startActivity(intent2);
        }else if(motionEvent.getY()-motionEvent1.getY()<-100) {
            //向下滑的操作，进入修改信息界面
            Intent intent3 = new Intent(MenuActivity.this, ModifyInfoActivity.class);
            startActivity(intent3);
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        //切换是否需要帮助
        if(FixedValue.needHelp == false || FixedValue.needHelp == null){
            FixedValue.needHelp = true;
            VoiceUtil.speak(MenuActivity.this, "已切换到需要帮助模式");

        }else {
            FixedValue.needHelp = false;
            VoiceUtil.speak(MenuActivity.this, "已切换到独立出行模式");
        }

    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }
}

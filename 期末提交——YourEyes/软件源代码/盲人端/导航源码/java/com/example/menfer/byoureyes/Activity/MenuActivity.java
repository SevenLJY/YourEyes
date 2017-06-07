package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;

import com.example.menfer.byoureyes.Activity.ModifyInfo.AgeModifyInfoActivity;
import com.example.menfer.byoureyes.FixedValue;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.ModifyPassword;
import com.example.menfer.byoureyes.YEUtils.NeedHelp;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;

/**
 * Created by Menfer on 2017/5/30.
 */
public class MenuActivity extends Activity implements GestureDetector.OnGestureListener {
    GestureDetector detector;
    VoiceUtil voiceUtil;
    String message = "菜单界面，上滑帮助，下滑返回，右滑修改信息，左滑切换是否需要帮助";
    int resultNeedHelp = 0;
    int resultSendLocation = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){
                test1(resultNeedHelp);
            }
            if (msg.what==0x124){
                //test2(resultSendLocation);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SpeechUtility.createUtility(MenuActivity.this, SpeechConstant.APPID+"=58593564");
        voiceUtil = new VoiceUtil(MenuActivity.this);
        detector = new GestureDetector(this, this);
        voiceUtil.stopSpeak();
        voiceUtil.speak(message);
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
            //切换是否需要帮助
            if (FixedValue.username == null){
                voiceUtil.stopSpeak();
                voiceUtil.speak("请登录后执行此操作");
                return false;
            }
            if(FixedValue.needHelp == false || FixedValue.needHelp == null){
                FixedValue.needHelp = true;
                new Thread(){
                    @Override
                    public void run() {
                        resultNeedHelp = NeedHelp.setNeedHelp(FixedValue.username,true);
                        handler.sendEmptyMessage(0x123);

                    }
                }.start();
            }else {
                FixedValue.needHelp = false;
                new Thread(){
                    @Override
                    public void run() {
                        resultNeedHelp = NeedHelp.setNeedHelp(FixedValue.username,false);
                        handler.sendEmptyMessage(0x123);

                    }
                }.start();
            }
        }else if(motionEvent.getX()-motionEvent1.getX()<-100){
            //向右滑的操作，修改信息
            Intent intent = new Intent(MenuActivity.this, AgeModifyInfoActivity.class);
            startActivity(intent);
        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作，进入帮助界面
            Intent intent2 = new Intent(MenuActivity.this, HelpActivity.class);
            startActivity(intent2);
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
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    /**
     * test1()方法根据更新是否需要帮助返回的结果进行相应提示
     * */
    private void test1(int result) {
        switch (result) {
            case 0:
                voiceUtil.stopSpeak();
                if (FixedValue.needHelp) {
                    voiceUtil.stopSpeak();
                    voiceUtil.speak("已切换至需要帮助模式");
                } else {
                    voiceUtil.stopSpeak();
                    voiceUtil.speak("已切换至独立出行模式");
                }
                break;
            case 1:
                voiceUtil.stopSpeak();
                voiceUtil.speak("网络连接失败，请稍后重试。");
                break;
            case 2:
                voiceUtil.stopSpeak();
                voiceUtil.speak("出现未知异常");
            default:
                break;
        }
    }
}

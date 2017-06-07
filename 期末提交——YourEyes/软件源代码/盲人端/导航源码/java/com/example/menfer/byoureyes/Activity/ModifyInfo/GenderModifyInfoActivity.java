package com.example.menfer.byoureyes.Activity.ModifyInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.menfer.byoureyes.Activity.Login.PasswordLoginActivity;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;

/**
 * Created by Menfer on 2017/6/1.
 */
public class GenderModifyInfoActivity extends Activity implements GestureDetector.OnGestureListener{
    GestureDetector detector;
    VoiceUtil voiceUtil;
    String gender = null;
    String age;
    String message = "修改性别，左滑选择男，右滑选择女，下滑返回.强烈建议您在家人朋友的帮助下进行修改信息。";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        SpeechUtility.createUtility(GenderModifyInfoActivity.this, SpeechConstant.APPID+"=58593564");
        voiceUtil = new VoiceUtil(GenderModifyInfoActivity.this);
        voiceUtil.speak(message);
        Intent intent = getIntent();
        age = intent.getExtras().getString("age");
        detector = new GestureDetector(this, this);
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
            //向左滑的操作 选择男性
            gender = "male";
            voiceUtil.stopSpeak();
            voiceUtil.speak("已选择男，长按确定。");
        }else if(motionEvent.getX()-motionEvent1.getX()<-100){
            //向右滑的操作 选择女性
            gender = "female";
            voiceUtil.stopSpeak();
            voiceUtil.speak("已选择女，长按确定。");
        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作 无
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
        try{
            if(gender == null){
                voiceUtil.stopSpeak();
                voiceUtil.speak("尚未选择性别，左滑选择男，右滑选择女。");
            }else {
                Bundle data = new Bundle();
                data.putString("gender",gender);
                data.putString("age",age);
                Intent intent = new Intent(GenderModifyInfoActivity.this, PhoneModifyInfoActivity.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        }catch (Exception e){
            voiceUtil.stopSpeak();
            voiceUtil.speak("出现未知异常。请稍后重试。");
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
            voiceUtil = new VoiceUtil(GenderModifyInfoActivity.this);
        }
        voiceUtil.speak(message);
    }
}

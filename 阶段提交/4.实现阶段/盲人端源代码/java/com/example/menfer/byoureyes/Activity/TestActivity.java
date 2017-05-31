package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.menfer.byoureyes.FixedValue;
import com.example.menfer.byoureyes.NewViews.RoundImageView;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.Services.LocationService;
import com.example.menfer.byoureyes.YEUtils.LocationUtils;
import com.example.menfer.byoureyes.YEUtils.PhotoUtils;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;
import com.example.menfer.byoureyes.YEUtils.UrlUtils;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.HashMap;

/**
 * Created by Menfer on 2017/5/3.
 */
public class TestActivity extends Activity {
    Button btn_get;
    Button btn_post;
    TextView tv_show;
    RoundImageView iv_photo;
    Button btn_getPhoto;
    String response;
    TextView tv_gpsinfo;
    Button btn_getGpsInfo;
    Button btn_bindService;
    EditText et_test;
    double longitude;
    public LocationService.MyLocationBinder binder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (LocationService.MyLocationBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){
                tv_show.setText(Double.toString(binder.getLongitude()));
            }
            if (msg.what==0x124){
                tv_show.setText("下载图片");
                PhotoUtils.showPhoto(iv_photo);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn_get = (Button)findViewById(R.id.btn_get);
        btn_post = (Button)findViewById(R.id.btn_post);
        tv_show = (TextView)findViewById(R.id.tv_show);
        iv_photo = (RoundImageView)findViewById(R.id.iv_photo);
        btn_getPhoto = (Button)findViewById(R.id.btn_getPhoto);
        btn_getGpsInfo = (Button)findViewById(R.id.btn_getGpsInfo);
        tv_gpsinfo = (TextView)findViewById(R.id.tv_gpsinfo);
        btn_bindService = (Button)findViewById(R.id.bindService);
        et_test = (EditText)findViewById(R.id.et_test);
        SpeechUtility.createUtility(TestActivity.this, SpeechConstant.APPID+"=58593564");

        final Intent serviceIntent = new Intent(TestActivity.this, LocationService.class);

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoiceUtil.speak(TestActivity.this, "你好");
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoiceUtil.voiceInput(TestActivity.this, et_test);
            }
        });

        btn_getPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        PhotoUtils.downloadPhoto(FixedValue.serverIP+"photo.jpg");
                        handler.sendEmptyMessage(0x124);
                    }
                }.start();
            }
        });

        btn_getGpsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationUtils.startLocation(TestActivity.this);
                longitude = LocationUtils.getLongitude();
                tv_gpsinfo.setText(Double.toString(longitude));
            }
        });

        btn_bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService(serviceIntent, conn, Service.BIND_AUTO_CREATE);
            }
        });


    }
}

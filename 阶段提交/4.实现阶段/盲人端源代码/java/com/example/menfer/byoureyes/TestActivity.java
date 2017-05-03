package com.example.menfer.byoureyes;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.menfer.byoureyes.YEUtils.GetPostUtils;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;

import org.w3c.dom.Text;

/**
 * Created by Menfer on 2017/5/3.
 */
public class TestActivity extends Activity {
    Button btn_get;
    Button btn_post;
    TextView tv_show;
    String response;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){
                tv_show.setText(response);
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

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        response = GetPostUtils.sendGet(FixedValue.serverIP,null);
                        handler.sendEmptyMessage(0x123);
                    }
                }.start();
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        response = GetPostUtils.sendPost(FixedValue.serverIP,"name=menfer");
                        handler.sendEmptyMessage(0x123);
                    }
                }.start();
            }
        });
    }
}

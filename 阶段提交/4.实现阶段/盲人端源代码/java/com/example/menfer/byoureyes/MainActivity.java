package com.example.menfer.byoureyes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button toLogin;
    Button toTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toLogin = (Button)findViewById(R.id.toLogin);
        toLogin.setOnClickListener(this);
        toTest = (Button)findViewById(R.id.btn_test);
        toTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toLogin:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_test:
                Intent intent1 = new Intent(MainActivity.this,TestActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}

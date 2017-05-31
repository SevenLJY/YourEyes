package com.example.menfer.byoureyes.Activity;

import android.os.Bundle;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;

/**
 * Created by Menfer on 2017/5/16.
 */
public class NaviActivity extends BaseActivity{
    //目的地位置
    NaviLatLng destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_navi);
        destination = new NaviLatLng(103.0,30.0);
        mAMapNaviView = (AMapNaviView)findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }

    @Override
    public void onInitNaviSuccess() {
        mAMapNavi.calculateWalkRoute(destination);
    }

}

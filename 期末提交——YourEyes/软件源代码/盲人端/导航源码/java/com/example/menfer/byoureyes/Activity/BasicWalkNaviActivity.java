package com.example.menfer.byoureyes.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;


public class BasicWalkNaviActivity extends BaseActivity {

    /**
     * 导航起始点和目的地的经纬度
     * */
    private double myLongitude;
    private double myLatitude;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();
        longitude = intent.getDoubleExtra("longitude", 0.0);
        latitude = intent.getDoubleExtra("latitude", 0.0);
        myLongitude = intent.getDoubleExtra("myLongitude", 0.0);
        myLatitude = intent.getDoubleExtra("myLatitude", 0.0);
        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        ToastUtil.show(BasicWalkNaviActivity.this, Double.toString(latitude)+"   "+Double.toString(longitude)+"   "+Double.toString(myLongitude)+"   "+Double.toString(myLatitude));
    }


    @Override
    public void onInitNaviSuccess() {
        if (latitude == 0){
            ToastUtil.show(BasicWalkNaviActivity.this, "出现未知异常 请重启应用");
        }else {
            NaviLatLng myLatLng = new NaviLatLng(myLatitude, myLongitude);
            NaviLatLng latLng = new NaviLatLng(latitude, longitude);
            mAMapNavi.calculateWalkRoute(myLatLng, latLng);
        }
    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        mAMapNavi.startNavi(NaviType.GPS);
    }
}

package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.menfer.byoureyes.NewViews.RoundImageView;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.ModifyInfo;

import java.util.Map;

/**
 * Created by Menfer on 2017/5/14.
 */
public class MapActivity extends Activity implements SensorEventListener{
    //地图显示所需对象
    private MapView mapView;
    private AMap aMap;
    private UiSettings uiSettings;        //地图界面设置
    private SensorManager sensorManager;
    private MyLocationStyle myLocationStyle;  //定位蓝点设置
    //传感器对象，使用方向传感器，用户位置图标箭头自动转动
    private Sensor sensor;
    private RoundImageView img_toNavi;     //跳转到导航界面图标
    private RoundImageView img_toHelp;      //跳转到帮助界面图标


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        img_toNavi = (RoundImageView)findViewById(R.id.img_toNavi);
        img_toHelp = (RoundImageView)findViewById(R.id.img_toHelp);
        //初始化传感器
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //初始化地图对象
        mapView = (MapView)findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        //初始化AMap对象
        init();

        /**
         * 给定位图标添加点击事件
         * 点击后简单的跳转到输入目的地界面
         * */
        img_toNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, PoiSearchActivity.class);
                startActivity(intent);
            }
        });
        img_toHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化AMap对象
     * */
    private void init(){
        if(aMap == null){
            aMap = mapView.getMap();      //获取地图对象
            aMap.setMyLocationEnabled(true);  //显示定位层并可以显示定位
            aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    //位置变化时不做任何事
                }
            });
            //对地图显示UI进行初始化设置
            uiSettings = aMap.getUiSettings();       //初始化设置
            uiSettings.setCompassEnabled(false);  //指南针可见
            uiSettings.setLogoBottomMargin(5);   //以下三行为logo位置
            uiSettings.setLogoLeftMargin(5);
            uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
            uiSettings.setMyLocationButtonEnabled(true);   //定位按钮可见
            uiSettings.setRotateGesturesEnabled(true);  //设置可手势旋转
            uiSettings.setScaleControlsEnabled(true);    //比例尺控件可见
            uiSettings.setScrollGesturesEnabled(true);     //手势滑动不可用
            uiSettings.setTiltGesturesEnabled(false);    //倾斜手势不可用
            uiSettings.setZoomControlsEnabled(true);    //缩放按钮可见
            uiSettings.setZoomGesturesEnabled(true);    //手势缩放可用
            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM);  //缩放按钮位置
            //创建一个设置放大级别的CameraUpdate
            CameraUpdate cuZoom = CameraUpdateFactory.zoomTo(18);
            //设置地图默认放大级别并实施
            aMap.moveCamera(cuZoom);

            //定位蓝点设置
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);   //只定位一次
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        }
    }

    /**
     * updatePosition():更新地图位置
     * 在更新位置加上marker
     * */
    private void updatePosition(LatLng location){
        LatLng pos = location;
        //创建一个设置经纬度的CameraUpdate
        CameraUpdate cuLoc = CameraUpdateFactory.changeLatLng(pos);
        //更新地图显示区域
        aMap.moveCamera(cuLoc);
        //清除地图所有覆盖物
        aMap.clear();
        //创建一个MarkerOptions对象并使用自定义图标
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.smallplaceholder));
        markerOptions.draggable(true);
        //添加marker
        Marker marker = aMap.addMarker(markerOptions);
    }

    /**
     * showMarker():在指定位置显示一个marker
     * 参数：latLng：要加marker的位置
     * marker暂时已指定
     * */
    private void showMarker(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.smallplaceholder));
        markerOptions.draggable(true);
        //添加marker
        Marker marker = aMap.addMarker(markerOptions);
    }

    /**
     * 重写MapView的生命周期
     * */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        //注册传感器
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), sensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /**
     * 实现地图指示箭头随方向改变而改变
     * 部分方法可能已过时
     * */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(Sensor.TYPE_ORIENTATION == sensorEvent.sensor.getType()){
            float degree = sensorEvent.values[0];
            aMap.setMyLocationRotateAngle(360-degree);
        }
    }

}

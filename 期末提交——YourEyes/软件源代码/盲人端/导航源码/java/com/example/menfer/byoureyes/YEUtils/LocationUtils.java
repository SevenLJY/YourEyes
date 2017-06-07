package com.example.menfer.byoureyes.YEUtils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by Menfer on 2017/5/10.
 */
public class LocationUtils {
    private static AMapLocationClient locationClient = null;
    private static AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private static Boolean notNull = false;       //获取的经纬度是否为空
    private static double longitude=0;
    private static double altitude = 0;
    private static double latitude = 0;
    private static String address;
    private static String city;
    private static String street;
    private static String province;
    private static String country;
    private static String detail;
    private static float accuracy;

    //初始化定位
    private static void initLocation(Context context){
        locationClient = new AMapLocationClient(context);
        locationClient.setLocationOption(getDefaultOption());
        //设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    //默认参数对象
    private static AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(15000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    //定位监听
    static AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(null != aMapLocation){
                //解析定位结果
                notNull = true;
                longitude = aMapLocation.getLongitude();
                address = aMapLocation.getAddress();
                city = aMapLocation.getCity();
                altitude = aMapLocation.getAltitude();
                street = aMapLocation.getStreet();
                latitude = aMapLocation.getLatitude();
            }else {
                notNull = false;
            }
        }
    };

    //开始定位
    public static void startLocation(Context context){
        initLocation(context);
        locationClient.startLocation();
    }

    //停止定位
    public static void stopLocation(){
        locationClient.stopLocation();
    }

    //销毁定位
    private static void destroyLocation(){
        if(null != locationClient){
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    //获取定位信息
    public static double getLongitude(){
        return longitude;
    }
    public static double getAltitude(){
        return altitude;
    }

}

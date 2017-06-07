package com.example.menfer.byoureyes.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by Menfer on 2017/5/12.
 */
public class LocationService extends Service{
    //定位所需对象
    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    public AMapLocationClient mLocationClient = null;

    //经纬度及位置信息
    private double longitude;  //经度
    private double latitude;   //纬度
    private String country;     //国家
    private String address;     //具体地址
    private String street;       //街道
    private String province;       //省份
    private String poiName;         //POI名称
    private String city;             //城市
    private String detail;         //地址细节
    private float accuracy;          //精确度
    //private double dest_longitude;     //目的地经度
    //private double dest_latitude;     //目的地纬度
    private boolean notNull;   //标志获取的定位对象是否为空

    private MyLocationBinder binder = new MyLocationBinder();

    /**
     * 内部类，用于使用该类
     * */
    public class MyLocationBinder extends Binder{
        //属性的get方法
        public double getLongitude(){
            return longitude;
        }
        public double getLatitude(){
            return latitude;
        }
        public String getCountry(){
            return country;
        }
        public String getAddress(){
            return address;
        }
        public String getProvince(){
            return province;
        }
        public String getDetail(){
            return detail;
        }
        public String getStreet(){
            return street;
        }
        public String getPoiName(){
            return poiName;
        }
        public float getAccuracy(){
            return accuracy;
        }
//        public double getDestLongitude(){
//            return dest_longitude;
//        }
//        public double getDestLatitude(){
//            return dest_latitude;
//        }
        public String getCity(){
            return city;
        }
        /**
         * 强调，在使用此服务时，要获取定位信息之前必须访问notNull属性
         * 当notNull为true时才可取其他属性，否则程序可能崩溃
         * */
        public boolean getNotNull(){
            return notNull;
        }
    }

    @Nullable
    @Override
    /**
     * 绑定时回调
     * */
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * Service被创建时回调
     * */
    @Override
    public void onCreate() {
        super.onCreate();
        //需要进行的定位操作
        //初始化定位
        initLocation();
        //初始化位置属性中各属性
        initLocationInfo();
        startLocation();
    }

    private void initLocation(){
        mLocationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        mLocationClient.setLocationOption(getDefaultOption());
        //设置定位监听
        mLocationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的初始化位置属性
     * */
    private void initLocationInfo(){
        address = "";
        city = "";
        country = "";
        province = "";
        street = "";
        detail = "";
        longitude = 0.0;
        latitude = 0.0;
        poiName = "";
        accuracy = 100;
    }

    /**
     * 默认的定位参数
     * */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
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

    /**
     * 定位监听
     * */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(null!=aMapLocation && aMapLocation.getLongitude() != 0){    //只在国内使用，经度为0时意味着有错误
                //解析定位结果
                notNull = true;
                longitude = aMapLocation.getLongitude();
                latitude = aMapLocation.getLatitude();
                street = aMapLocation.getStreet();
                address = aMapLocation.getAddress();
                country = aMapLocation.getCountry();
                poiName = aMapLocation.getPoiName();
                city = aMapLocation.getCity();
                detail = aMapLocation.getLocationDetail();
                accuracy = aMapLocation.getAccuracy();
                province = aMapLocation.getProvince();
            }else {
                notNull = false;
            }
        }
    };

    /**
     * 开始定位
     * */
    private void startLocation(){
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     * */
    private void stopLocation() {
        mLocationClient.stopLocation();
    }

    /**
     * 销毁定位
     * */
    private void destroyLocation(){
        if(null != mLocationClient){
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationClient = null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocation();
        destroyLocation();
    }
}

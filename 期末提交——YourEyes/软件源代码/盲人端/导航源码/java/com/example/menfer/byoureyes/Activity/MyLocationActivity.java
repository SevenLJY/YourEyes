package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;

/**
 * Created by Menfer on 2017/5/10.
 */
public class MyLocationActivity extends Activity {
    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    public AMapLocationClient mLocationClient = null;
    private Button btn_getLocation;
    private TextView tv_location;
    private EditText et_destinationName;
    private Button btn_toNavi;
    private GeocodeSearch search;
    private TextView tv_destination;

    String destinationName;

    //经纬度及位置信息
    private double longitude;
    private double latitude;
    private String country;
    private String address;
    private String street;
    private String province;
    private String poiName;
    private String city;
    private String detail;
    private float accuracy;
    private double dest_longitude;
    private double dest_latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylocation);
        longitude = -1;
        btn_getLocation = (Button)findViewById(R.id.btn_getLocation);
        tv_location = (TextView)findViewById(R.id.tv_location);
        et_destinationName = (EditText)findViewById(R.id.et_destinationName);
        btn_toNavi = (Button)findViewById(R.id.btn_toNavi);
        tv_destination = (TextView)findViewById(R.id.tv_destination);
        dest_longitude = 0;
        dest_latitude = 0;

        //初始化定位和地址解析
        initLocation();
        search = new GeocodeSearch(this);
        search.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
               // RegeocodeAddress addr = regeocodeResult.getRegeocodeAddress();
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                GeocodeAddress addr = geocodeResult.getGeocodeAddressList().get(0);
                LatLonPoint latlng = addr.getLatLonPoint();
                dest_longitude = latlng.getLongitude();
                dest_latitude = latlng.getLatitude();
                String temp = "";
                temp += "经度："+dest_longitude+"\n";
                temp += "纬度："+dest_latitude+"\n";
                tv_destination.setText(temp);
            }
        });

        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocation();
            }
        });

        btn_toNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinationName = et_destinationName.getText().toString().trim();
                if(destinationName.equals("")){
                    ToastUtil.show(MyLocationActivity.this, "请输入有效地址");
                }else {
                    if(city!=null){
                        GeocodeQuery query = new GeocodeQuery(destinationName, city);
                        search.getFromLocationNameAsyn(query);
                    }
                }
            }
        });
    }

    private void initLocation(){
        mLocationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        mLocationClient.setLocationOption(getDefaultOption());
        //设置定位监听
        mLocationClient.setLocationListener(locationListener);
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
            if(null!=aMapLocation){
                //解析定位结果
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
                if(longitude!=0){
                    String temp = "";
                    temp += "经度："+Double.toString(longitude)+"\n";
                    temp += "纬度："+Double.toString(latitude)+"\n";
                    temp += "国家："+country+"\n";
                    temp += "省份："+province+"\n";
                    temp += "城市："+city+"\n";
                    temp += "精确度："+Float.toString(accuracy)+"\n";
                    temp += "地址："+address+"\n";
                    tv_location.setText(temp);
                }
            }else {
                ToastUtil.show(MyLocationActivity.this, "定位失败，获取值为空");
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
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
}

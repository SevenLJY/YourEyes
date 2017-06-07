package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.menfer.byoureyes.Activity.ModifyInfo.PhoneModifyInfoActivity;
import com.example.menfer.byoureyes.R;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;
import com.example.menfer.byoureyes.YEUtils.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Menfer on 2017/5/21.
 * 该界面完成POI的搜索
 * 在输入框输入地名，下方列表显示附近结果
 */
public class PoiSearchActivity extends Activity implements GestureDetector.OnGestureListener{
    VoiceUtil voiceUtil;
    GestureDetector detector;
    private SearchView sv_destination;
    private ListView lv_result;
    //定位辅助，保证查询的地点在同一城市
    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    public AMapLocationClient mLocationClient = null;
    //定位属性
    private double longitude;
    private double latitude;
    private double latitudeStatus;     //避免latitude在错误情况下被更改
    private String city;
    //POI搜索对象
    PoiSearch.Query query;
    PoiSearch poiSearch;
    //POI搜索结果
    List<PoiItem> poiItems;
    //Map列表
    List<Map<String, String>> listItems;
    //列表Adapter
    SimpleAdapter adapter;
    //存储地点信息的HashMap
    Map<String, String> listItem;
    boolean hasResult;                 //表示是否有搜索结果
    int index = -1;                 //当前用户在查看哪一条结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisearch);
        detector = new GestureDetector(this, this);
        sv_destination = (SearchView)findViewById(R.id.sv_destination);
        lv_result = (ListView)findViewById(R.id.lv_result);
        poiItems = new ArrayList<PoiItem>();
        listItems = new ArrayList<Map<String, String>>();
        SpeechUtility.createUtility(PoiSearchActivity.this, SpeechConstant.APPID+"=58593564");
        voiceUtil = new VoiceUtil(PoiSearchActivity.this);
        String message = "目的地搜索，左滑语音输入，下滑返回。";
        voiceUtil.speak(message);
        //对搜索框的设置
        sv_destination.setIconifiedByDefault(false);  //设置不自动缩小为图标
        sv_destination.setSubmitButtonEnabled(true);   //显示搜索按钮
        longitude = 0;
        latitude = 0;
        latitudeStatus = 0;
        city = "";
        hasResult = false;
        //初始化定位
        initLocation();
        startLocation();
        //搜索框搜索监听
        sv_destination.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                hasResult = false;
                //点击搜索时触发，将搜索结果展示在ListView中
                for(int num = 0; num < listItems.size(); num++){
                    listItems.get(num).clear();
                }
                listItems.clear();
                if(city.length()!=0){           //city值仍为""时无意义，搜索所得位置不在本城市，该处处理为不予显示
                    query = new PoiSearch.Query(s, "", city);
                    query.setPageSize(10);   //设置每页展示10个结果
                    query.setPageNum(0);   //设置查询页码
                    poiSearch = new PoiSearch(PoiSearchActivity.this, query);
                    //设置周边搜索的中心点以及半径,以定位的当前位置为中心点
                    poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 30000));
                    poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {

                        @Override
                        public void onPoiSearched(PoiResult poiResult, int i) {
                           //搜索结果在列表中显示
                            if(i == 1000 && poiResult != null){
                                poiItems = poiResult.getPois();
                                for(PoiItem item :poiItems){
                                    hasResult = true;
                                    listItem = new HashMap<String, String>();
                                    listItem.put("title", item.toString());
                                    listItem.put("content", item.getCityName()+item.getAdName()+item.getSnippet());
                                    listItem.put("latitude",Double.toString(item.getLatLonPoint().getLatitude()));
                                    listItem.put("longitude", Double.toString(item.getLatLonPoint().getLongitude()));
                                    listItems.add(listItem);
                                    item.toString();
                                }
                                if (hasResult){
                                    adapter = new SimpleAdapter(PoiSearchActivity.this, listItems,
                                            R.layout.array_poiitems,
                                            new String[]{"title", "content"},
                                            new int[]{R.id.tv_title, R.id.tv_content});
                                    //lv_result.setAdapter(adapter);

                                    //语音询问用户地点是否正确
                                    voiceUtil.stopSpeak();
                                    voiceUtil.speak("已搜索到结果，右滑开始选择，过程中上滑确认，左滑重新输入，右滑查看下一个，下滑返回。");
                                }else {
                                    //无搜索结果时的提示
                                    voiceUtil.stopSpeak();
                                    voiceUtil.speak("无搜索结果 请检查关键字是否有错误或者距离关键字超过三十公里");
                                }
                            }else{
                                ToastUtil.show(PoiSearchActivity.this, "无搜索结果 请检查关键字");
                            }
                        }

                        @Override
                        public void onPoiItemSearched(PoiItem poiItem, int i) {
                            //搜索结果回调，暂时不需使用
                        }
                    });
                    poiSearch.searchPOIAsyn();
                }else {
                    ToastUtil.show(PoiSearchActivity.this, "正在获取定位信息  请稍候");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //搜索框文字改变时触发，暂不做优化
                return false;
            }
        });

        /**
         * 为列表项单击事件添加监听
         * 单击相应列表项时跳转到导航界面并将目的地传至导航界面
         * */
        lv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle data = new Bundle();
                data.putDouble("latitude", Double.parseDouble(listItems.get(i).get("latitude")));
                data.putDouble("longitude", Double.parseDouble(listItems.get(i).get("longitude")));
                data.putDouble("myLatitude", latitude);
                data.putDouble("myLongitude", longitude);

                if(latitude == 0){
                    ToastUtil.show(PoiSearchActivity.this, "您的定位信息出错，请检查是否打开了GPS");
                }else {
                    Intent intent = new Intent(PoiSearchActivity.this, BasicWalkNaviActivity.class);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            }
        });
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
            //向左滑的操作 语音输入 原为查看上一个
/*            if (hasResult) {
                if (index == 0) {
                    voiceUtil.stopSpeak();
                    voiceUtil.speak("已是第一个结果");
                } else if (index == -1) {
                    voiceUtil.stopSpeak();
                    voiceUtil.speak("您尚未开始查看结果");
                } else {
                    index--;
                    ask(listItems,index);
                }
            }else {
                voiceUtil.stopSpeak();
                voiceUtil.speak("尚无查询结果");
            }*/
            voiceUtil.voiceInputInSearchView(sv_destination);
        }else if(motionEvent.getX()-motionEvent1.getX()<-100){
            //向右滑的操作 查看下一个
            if (hasResult) {
                if (index == 10) {
                    voiceUtil.stopSpeak();
                    voiceUtil.speak("已是最后一个结果");
                } else {
                    index++;
                    ask(listItems,index);
                }
            }else {
                voiceUtil.stopSpeak();
                voiceUtil.speak("尚无查询结果");
            }

        }else if(motionEvent.getY()-motionEvent1.getY()>100){
            //向上滑的操作 无
            if(hasResult){
                if(index == -1){
                    voiceUtil.stopSpeak();
                    voiceUtil.speak("尚未选择");
                }else {
                    voiceUtil.stopSpeak();
                    voiceUtil.speak("您选择的地点为，"+listItems.get(index).get("content")+",确认请长按屏幕。");
                }
            }else {
                voiceUtil.stopSpeak();
                voiceUtil.speak("尚无查询结果");
            }
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

    private void ask(List<Map<String, String>> items, int i){
        voiceUtil.stopSpeak();
        voiceUtil.speak("是否为，"+items.get(i).get("content")+",确认请上滑。");
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        //长按开始导航
        if (hasResult){
            if (index != -1){
                Bundle data = new Bundle();
                data.putDouble("latitude", Double.parseDouble(listItems.get(index).get("latitude")));
                data.putDouble("longitude", Double.parseDouble(listItems.get(index).get("longitude")));
                data.putDouble("myLatitude", latitude);
                data.putDouble("myLongitude", longitude);

                if(latitude == 0){
                    ToastUtil.show(PoiSearchActivity.this, "您的定位信息出错，请检查是否打开了GPS");
                }else {
                    Intent intent = new Intent(PoiSearchActivity.this, BasicWalkNaviActivity.class);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            }else {
                voiceUtil.stopSpeak();
                voiceUtil.speak("尚未选择目的地");
            }
        }else {
            voiceUtil.stopSpeak();
            voiceUtil.speak("尚无结果");
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    /**
     * 初始化定位
     * */
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
                latitudeStatus = aMapLocation.getLatitude();
                if(latitudeStatus!=0){
                    //解析正确时的操作，此处设定为解析正确才更新经纬度和city值
                    longitude = aMapLocation.getLongitude();
                    latitude = aMapLocation.getLatitude();
                    city = aMapLocation.getCity();
                }
            }else {
                ToastUtil.show(PoiSearchActivity.this, "定位失败，获取值为空");
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

    /**
     * 重写生命周期
     *当Activity销毁时停止并销毁定位
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocation();
        destroyLocation();
    }

}
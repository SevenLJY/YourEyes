package com.example.menfer.byoureyes.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.example.menfer.byoureyes.YEUtils.ErrorInfo;
import com.example.menfer.byoureyes.YEUtils.TTSController;
import com.example.menfer.byoureyes.YEUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Menfer on 2017/5/10.
 */
public class BaseActivity extends Activity implements AMapNaviListener,AMapNaviViewListener{
    protected AMapNaviView mAMapNaviView;
    protected AMapNavi mAMapNavi;
    protected TTSController mTtsManager;
    //两位置为模拟导航使用，实际中无用处
//    protected NaviLatLng mEndLatlng = new NaviLatLng(40.084894,116.603039);
//    protected NaviLatLng mStartLatlng = new NaviLatLng(39.825934,116.342972);
//    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
//    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
//    protected List<NaviLatLng> mWayPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //实例化语音引擎
        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();


        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addAMapNaviListener(mTtsManager);
        //设置模拟导航行车速度
        mAMapNavi.setEmulatorNaviSpeed(75);
//        sList.add(mStartLatlng);
//        eList.add(mEndLatlng);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

        //停止当前的话
        mTtsManager.stopSpeaking();
        //停止后有底层回调
        //mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        mTtsManager.destroy();
    }

    @Override
    public void onInitNaviFailure() {
        ToastUtil.show(this, "init navi Failed");
    }

    @Override
    public void onInitNaviSuccess() {
        //初始化成功
    }

    @Override
    public void onStartNavi(int i) {
        //开始导航回调
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        //当前位置回调
    }

    @Override
    public void onGetNavigationText(int i, String s) {
        //播报类型和播报文字回调
    }

    @Override
    public void onEndEmulatorNavi() {
        //结束模拟导航
    }

    @Override
    public void onArriveDestination() {
        //到达目的地
    }

    @Override
    public void onCalculateRouteSuccess() {
        //路线计算成功
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        //路线计算失败
        ToastUtil.show(this, "errorInfo：" + errorInfo + ",Message：" + ErrorInfo.getError(errorInfo));
    }

    @Override
    public void onReCalculateRouteForYaw() {
        //偏航后重新计算路线回调
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
    }

    @Override
    public void onArrivedWayPoint(int i) {
        //到达途径点
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        //GPS开关状态回调
    }

    @Override
    public void onNaviSetting() {
        //底部导航设置点击回调
    }

    @Override
    public void onNaviMapMode(int i) {
        //地图模式，锁屏或锁车
    }

    @Override
    public void onNaviCancel() {
        finish();
    }

    @Override
    public void onNaviTurnClick() {
        //转弯view的点击回调
    }

    @Override
    public void onNextRoadClick() {
        //下一个道路view点击回调
    }

    @Override
    public void onScanViewButtonClick() {
        //全览按钮点击回调
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        //过时
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        //导航信息更新
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        //已过时
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        //已过时
    }


    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //显示转弯回调
    }

    @Override
    public void hideCross() {
        //隐藏转弯回调
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        //显示车道信息
    }

    @Override
    public void hideLaneInfo() {
        //隐藏车道信息
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        //多路径算路成功回调
    }



    @Override
    public void notifyParallelRoad(int i) {
        if(i==0){
            ToastUtil.show(this, "主辅路过渡");
        }
        if(i==1){
            ToastUtil.show(this, "主路");
        }
        if(i==2){
            ToastUtil.show(this, "辅路");
        }
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        //更新交通设施信息
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        //更新巡航模式的统计信息
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        //更新巡航模式的拥堵信息
    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onLockMap(boolean b) {
        //所地图状态发生变化时回调
    }

    @Override
    public void onNaviViewLoaded() {
        ToastUtil.show(this, "导航页面加载成功");
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }
}

package com.example.menfer.byoureyes;

import android.app.Application;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.navi.AMapNavi;
import com.amap.api.services.core.ServiceSettings;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Menfer on 2017/5/10.
 */
public class BYourEyes extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AMapNavi.setApiKey(this, "b0257f79b0d1d4a63919a4dd8e354012");
        MapsInitializer.setApiKey("b0257f79b0d1d4a63919a4dd8e354012");
        AMapLocationClient.setApiKey("b0257f79b0d1d4a63919a4dd8e354012");
    }
}

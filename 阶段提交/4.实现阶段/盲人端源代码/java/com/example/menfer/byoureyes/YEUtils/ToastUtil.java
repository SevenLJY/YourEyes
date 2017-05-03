package com.example.menfer.byoureyes.YEUtils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Menfer on 2017/4/27.
 */
public class ToastUtil {
    public static void show(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

package com.example.menfer.byoureyes.YEUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.menfer.byoureyes.NewViews.RoundImageView;
import com.example.menfer.byoureyes.R;

/**
 * Created by Menfer on 2017/5/6.
 * PhotoUtils为工具类
 * 类中方法为和用户头像操作有关的方法
 * 包括：获取本地文件夹下的头像、上传用户头像到服务器、从服务器获取用户头像
 * */
public class PhotoUtils {
    /**
     * showPhoto() 在指定组件上显示用户的头像
     * 参数：photoImageView:用于显示头像的组件，
     *                 在本应用中一律使用RoundImageView，
     *                 该组件继承ImageView，
     *                 定义与初始化在NewViews包下
     * */

    public static void showPhoto(RoundImageView photoImageView){
        Bitmap photo = BitmapFactory.decodeFile("photo.jpg");
        if(null == photo){
            //无文件时显示默认头像
            photoImageView.setImageResource(R.drawable.photo);
        }else{
            photoImageView.setImageBitmap(photo);
        }
    }

    /**
     * downloadPhoto() 从服务器下载用户的头像并存入文件，命名为photo.jpg
     *参数：fileUrl：图片Url
     * */
    public static void downloadPhoto(String fileUrl){
        try{
            UrlUtils.downloadFIle(fileUrl,"photo.jpg");
        }catch (Exception e){

        }
    }
}

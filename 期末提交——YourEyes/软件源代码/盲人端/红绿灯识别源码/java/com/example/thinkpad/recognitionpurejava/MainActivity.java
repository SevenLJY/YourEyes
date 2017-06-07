package com.example.thinkpad.recognitionpurejava;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.example.thinkpad.recognitionpurejava.R.id.textView3;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener {

    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    private int absoluteFaceSize;
    private int maxFaceSize;
    private String result;
    private Mat target;
    private TextView tv;
    private RefreshThread mRefreshThread = null;//刷新线程
    private boolean isStop = true;// 线程控制标志

    private static final String TAG = "OCVSample::Activity";

    //加载C++
    static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI(Object bmp);
    public native String matStraightToJNI(long matAddrRgba);


    //刷新线程
    class RefreshThread implements Runnable {
        public void run() {
            while (isStop) {
                try {
                    handler.sendMessage(handler.obtainMessage());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //实时刷新界面,接收消息(并修改停止/开始标志位)
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           /*
            if (result != null) {
                isStop = false;
                try {

                    Thread.sleep(3000);
                    isStop = true;
                    new Thread(mRefreshThread).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            tv.setText(result);

        }
    };

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    initializeOpenCVDependencies();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    private void initializeOpenCVDependencies() {


        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.cascade_1000);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "cascade_1000.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();


            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }

        // And we are ready to go
        openCvCameraView.enableView();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_manipulations_surface_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.image_manipulations_activity_surface_view);
        tv = (TextView)findViewById(textView3);
        mRefreshThread = new RefreshThread();
        //tv.setText(result);
        //openCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        //openCvCameraView = new JavaCameraView(this, -1);
        //setContentView(openCvCameraView);
        //openCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);//后置摄像头
        openCvCameraView.setCvCameraViewListener(this);
        new Thread(mRefreshThread).start();// 开始进程
    }



    @Override
    public void onPause()
    {
        super.onPause();
        if (openCvCameraView != null)
            openCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (openCvCameraView != null) {
            openCvCameraView.disableView();
        }
    }




    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);


        // The faces will be a 10% of the height of the screen
        absoluteFaceSize = (int) (height * 0.05);
        maxFaceSize= (int) (height * 0.2);
    }


    @Override
    public void onCameraViewStopped() {
        grayscaleImage.release();
    }


    //@Override
    public Mat onCameraFrame(Mat aInputFrame) {
        // Create a grayscale image

        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
       // Imgproc.equalizeHist(grayscaleImage, grayscaleImage);



        MatOfRect faces = new MatOfRect();


        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 10, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size(maxFaceSize,maxFaceSize));
        }

        Rect[] facesArray = faces.toArray();

        for (int i = 0; i <facesArray.length; i++) {
            Imgproc.rectangle(aInputFrame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 0, 0, 255), 2);
        }

        if(facesArray.length!=0){
           target = aInputFrame.submat(facesArray[0]);
           result = matStraightToJNI(target.getNativeObjAddr());
           //Bitmap bmp = Bitmap.createBitmap(target.cols(), target.rows(), Bitmap.Config.ARGB_8888);
           //Utils.matToBitmap(target, bmp);
           //result = stringFromJNI(bmp);
        }else{
            result=null;
        }

        Core.flip(aInputFrame, aInputFrame, -1);

        return aInputFrame;
    }




    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            //initializeOpenCVDependencies();
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }



}







/*
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.opencv.imgproc.Imgproc.cvtColor;

public class MainActivity extends AppCompatActivity {

    private CascadeClassifier cascadeClassifier;

    private int absoluteFaceSize;

    Button start;
    ImageView img;
    Bitmap srcBitmap = null;
    private static boolean flag = true;

    private static final String TAG = "OCVSample::Activity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

/*

public native String stringFromJNI(Object bmp);

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    initializeOpenCVDependencies();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };


    private void initializeOpenCVDependencies() {


        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.cascade_1000);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "cascade_1000.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();


            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        start.setOnClickListener(new ProcessClickListener());
    }
    public void initUI(){
        start = (Button)findViewById(R.id.button);
        img = (ImageView)findViewById(R.id.imageView);
        Log.i(TAG, "initUI success...");
    }

    private class ProcessClickListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub


            srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
            Mat rgbMat = new Mat();
            Utils.bitmapToMat(srcBitmap, rgbMat); //convert to rgb image

            Mat grayMat = new Mat();
            cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY); //convert to gray

            Imgproc.equalizeHist(grayMat, grayMat);


            MatOfRect faces = new MatOfRect();


            // Use the classifier to detect faces
            if (cascadeClassifier != null) {
                cascadeClassifier.detectMultiScale(grayMat, faces, 1.1, 2, 15,
                        new Size(grayMat.width()*0.05, grayMat.height()*0.05), new Size());
            }

            Rect[] facesArray = faces.toArray();

            for (int i = 0; i <facesArray.length; i++) {
                Imgproc.rectangle(rgbMat, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 0, 0, 255), 3);
            }                                                                                          //(0,255,0,255)green

            if(facesArray.length!=0){
                Mat target = rgbMat.submat(facesArray[0]);
                Bitmap bmp = Bitmap.createBitmap(target.cols(), target.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(target, bmp);
                img.setImageBitmap(bmp);
                TextView tv = (TextView) findViewById(R.id.result);
                String result = stringFromJNI(bmp);
                tv.setText(result);

            }else{
                Bitmap bmp = Bitmap.createBitmap(rgbMat.cols(), rgbMat.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(rgbMat, bmp);
                img.setImageBitmap(bmp);
            }



        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }




}*/

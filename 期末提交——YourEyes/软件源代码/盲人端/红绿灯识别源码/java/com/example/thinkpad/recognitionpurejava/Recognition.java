package com.example.thinkpad.recognitionpurejava;

import android.graphics.Bitmap;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * Created by thinkpad on 2017/5/22.
 */

public class Recognition {

    double PI = 3.1416;
          //  #define min(a,b) (a < b ? a : b)


    public void targetDetect(Bitmap srcBitmap,ImageView img){

        //img.setImageBitmap(srcBitmap);

        Mat rgbMat = new Mat();
        Utils.bitmapToMat(srcBitmap, rgbMat); //convert to rgb image

        Mat grayMat = new Mat();
        cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY); //convert to gray



        CascadeClassifier cascade;//创建级联分类器对象
        String cascadeName = "/storage/sdcard0/cascade_1000.xml";//训练数据
        cascade = new CascadeClassifier(cascadeName);

        MatOfRect rects = new MatOfRect();

        //vector<Rect>::const_iterator pRect;


        //将图片缩小，加快检测速度

       // Mat smallImg = new Mat(rgbMat.rows(), rgbMat.cols(), CV_8UC1);


            
       // resize(grayMat, smallImg, smallImg.size(), 0, 0, INTER_LINEAR);//将尺寸缩小到1/scale,用线性插值
      //  equalizeHist(smallImg, smallImg);//直方图均衡

        //detectMultiScale函数中smallImg表示的是要检测的输入图像为smallImg，rects表示检测到的目标序列，1.1表示
        //每次图像尺寸减小的比例为1.1，2表示每一个目标至少要被检测到3次才算是真的目标(因为周围的像素和不同的窗口大
        //小都可以检测到目标),CV_HAAR_SCALE_IMAGE表示不是缩放分类器来检测，而是缩放图像，Size(30, 30)为目标的
        //最小最大尺寸
        //rects.clear();


        cascade.detectMultiScale(rgbMat, rects);
       // cascade.detectMultiScale(grayMat, rects, 1.1, 15, Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size());
        //0| CV_HAAR_SCALE_IMAGE
        //|CV_HAAR_FIND_BIGGEST_OBJECT
        //|CV_HAAR_DO_ROUGH_SEARCH

            Rect[] rectArray = rects.toArray();

            for (int i = 0; i < rectArray.length; i++) {
                Imgproc.rectangle(rgbMat, rectArray[i].tl(), rectArray[i].br(), new Scalar(0, 255, 0), 3);
            }


            //rects = rects.begin();
            //Mat target = new Mat();
            //target= rgbMat.submat(rectArray[0].clone());*/
        Bitmap bmp = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);

        Utils.matToBitmap(rgbMat, bmp);

        rgbMat.release();
       // grayMat.release();
       // smallImg.release();

        img.setImageBitmap(bmp);
    }

  /*  int rgb2hsi(Mat image, Mat hsi){
        if (!image.data){
            cout << "Miss Data" << endl;
            return -1;
        }
        int nRows = image.rows();               //图像矩阵的行数
        int nCols = image.cols();               //图形矩阵的列数
        if (image.isContinuous()){     //判断图像是否连续，连续则可以把图像展成一行
            nCols = nCols*nRows;
            nRows = 1;
        }
        for (int i = 0; i < nRows; i++){
            uchar *src = image.ptr<uchar>(i);
            uchar *dst = hsi.ptr<uchar>(i);
            for (int j = 0; j < nCols; j++){
                float b = src[3 * j] / 255.0;
                float g = src[3 * j + 1] / 255.0;
                float r = src[3 * j + 2] / 255.0;
                float num = (float)(0.5*((r - g) + (r - b)));
                float den = (float)sqrt((r - g)*(r - g) + (r - b)*(g - b));
                float H, S, I;
                if (den == 0){           //分母不能为零
                    H = 0;
                }
                else{
                    double theta = acos(num / den);
                    if (b <= g)
                        H = theta;
                    else
                        H = 2 * PI - theta;
                }

                if (H < 0.167*PI || (1.75*PI < H && H < 2 * PI)) tnRed++;
                else if (0.417*PI < H < 0.548*PI) tnGreen++;

                double minRGB = min(min(r, g), b);
                den = r + g + b;
                if (den == 0)         //分母不能为零
                    S = 0;
                else
                    S = 1 - 3 * minRGB / den;

                I = den / 3.0;
                //将S分量和H分量都扩充到[0,255]区间以便于显示;  
                //一般H分量在[0,2pi]之间，S在[0,1]之间  
                dst[3 * j] = H * 255;
                dst[3 * j + 1] = S * 255;
                dst[3 * j + 2] = I * 255;
            }
        }
        return 0;
    }

    void colorRecogize(Mat target){
        int sum = target.channels()*target.rows()*target.cols();

        Mat img_hsi = new Mat(target.rows(), target.cols(), CV_8UC3);
        vector <Mat> vecReb, vecHsi;



        rgb2hsi(target, img_hsi);
        split(img_hsi, vecHsi);*/

	/*
	cout << "pixel(0,0) in RGB" << endl;
	for (int i = 0; i < 3; i++){
	cout << (int)img.at<Vec3b>(0, 0)[i] << " ";
	}*/

        //cout << tnRed << endl;
        //cout << tnGreen << endl;
        //cout << sum << endl;

        //判断颜色
        //参数依据：
    /*    float pRed = (float)tnRed / sum;
        float pGreen = (float)tnGreen / sum;*/

        //cout << "红色比例：" << pRed << endl;
        //cout << "绿色比例：" << pGreen << endl;
	/*
	if (0.3 <= pRed && pRed <= 0.55){
	if (pGreen<0.3 || pGreen> 0.55){
	cout << "Red" << endl;
	}
	}
	else if (0.3 <= pGreen && pGreen <= 0.55){
	if (pRed<0.3 || pRed> 0.55){

	cout << "Green" << endl;
	}
	}*/
    /*    if (pRed > pGreen){
            color = 1;                                             //cout << "Red" << endl;
        }
        else{
            color = 2;
        }//cout << "Green" << endl;
    }
*/

}




#include <jni.h>
#include <string>


#include "opencv2/objdetect/objdetect.hpp"

#include "opencv2/opencv.hpp"


#include <android/bitmap.h>


#define PI 3.1416
#define min(a,b) (a < b ? a : b)

using namespace std;
using namespace cv;

int tnRed, tnGreen;

extern "C"{


int rgb2hsi(Mat& image, Mat& hsi){

    int nRows = image.rows;               //图像矩阵的行数
    int nCols = image.cols;               //图形矩阵的列数
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

int colorRecogize(Mat target){
    int color;
    int sum = target.channels()*target.rows*target.cols;

    Mat img_hsi;
    vector <Mat> vecReb, vecHsi;

    img_hsi.create(target.rows, target.cols, CV_8UC3);

    rgb2hsi(target, img_hsi);
    split(img_hsi, vecHsi);

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
    float pRed = (float)tnRed / sum;
    float pGreen = (float)tnGreen / sum;

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



    if (pRed > pGreen){
        return color = 1;            //cout << "Red" << endl;
    }
    else{
        return color = 2;           //cout << "Green" << endl;
    }
}

JNIEXPORT
jstring
Java_com_example_thinkpad_recognitionpurejava_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */,jobject jbmp) {

/*
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
    */

    AndroidBitmapInfo bmpInfo;
    void *bmpPixels;
    int height, width, ret;
    if ((ret = AndroidBitmap_getInfo(env, jbmp, &bmpInfo)) < 0) {
        string noBitmap = "No bitmap";
        return env->NewStringUTF(noBitmap.c_str());
    }
    if (bmpInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        string badFormat = "No bitmap";
        return env->NewStringUTF(badFormat.c_str());
    }
    if ((ret = AndroidBitmap_lockPixels(env, jbmp, &bmpPixels)) < 0) {
        string failPixel = "Fail pixels";
        return env->NewStringUTF(failPixel.c_str());
    }
    AndroidBitmap_unlockPixels(env, jbmp);

    height = bmpInfo.height;
    width = bmpInfo.width;
    Mat src(height, width, CV_8UC4, bmpPixels);
    if (!src.data) {
        string failConvert = "Fail convert to Mat";
        return env->NewStringUTF(failConvert.c_str());
    } else {
        int color = colorRecogize(src);
        if (color == 1) {
            string red = "Red";
            return env->NewStringUTF(red.c_str());
        }
        else {
            string green = "Green";
            return env->NewStringUTF(green.c_str());
        }


    }
}

JNIEXPORT
jstring
Java_com_example_thinkpad_recognitionpurejava_MainActivity_matStraightToJNI(JNIEnv *env, jobject /* this */,jlong addrRgba){
    Mat& mRgb=*(Mat*)addrRgba;
    int color = colorRecogize(mRgb);
    if (color == 1) {
        string red = "Red";
        return env->NewStringUTF(red.c_str());
    }
    else {
        string green = "Green";
        return env->NewStringUTF(green.c_str());
    }
}


}


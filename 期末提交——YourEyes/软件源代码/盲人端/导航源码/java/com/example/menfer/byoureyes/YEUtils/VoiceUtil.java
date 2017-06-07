package com.example.menfer.byoureyes.YEUtils;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SearchView;

import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Menfer on 2017/5/24.
 */
public class VoiceUtil {
    public VoiceUtil(Context mContext){
        context = mContext;
    }
    //语音合成对象
    private SpeechSynthesizer mTts;
    //设置发音人，该发音人支持简单标记进行多音字和数值的指定，比较方便，默认xiaoyan暂不支持
    private String voicer = "vixy";
    //引擎类型 云端
    final private String mEngineType = SpeechConstant.TYPE_CLOUD;
    //发音语速
    final private String mSpeed = "50";
    //发音音调
    final private String mPitch = "50";
    //发音音量
    final private String mVolume = "50";
    //初始化合成对象
    private Context context;

    //语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    //用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    //语音输入的EditText
    static EditText myEditText;
    static SearchView mySearchView;
    boolean isEditText = true;

    /**
     * voiceInput():开始语音识别输入
     * 参数：context:Activity上下文
     *       editText：要输入的输入框
     * */
    public void voiceInput(EditText editText){
        isEditText = true;
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mIatDialog = new RecognizerDialog(context, mInitListener);
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
        //setIatParam();
        setIatDialogParam();
        myEditText = editText;
        /*int res = mIat.startListening(mRecognizerListener);
        if(res != ErrorCode.SUCCESS){
            //听写失败
            ToastUtil.show(context, "听写失败");
        }*/
    }

    /**
     * 在搜索框语音输入
     * */
    public void voiceInputInSearchView(SearchView searchView){
        isEditText = false;
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mIatDialog = new RecognizerDialog(context, mInitListener);
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
        //setIatParam();
        setIatDialogParam();
        mySearchView = searchView;
        /*int res = mIat.startListening(mRecognizerListener);
        if(res != ErrorCode.SUCCESS){
            //听写失败
            ToastUtil.show(context, "听写失败");
        }*/
    }


    /**
     * speak（）：开始合成音频并播放
     * 参数：text：将要合成的文字
     *       context:Activity上下文
     * */
    public void speak(String text){
        //设置参数
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        setParam();
        int res = mTts.startSpeaking(text, mTtsListener);
        if (res != ErrorCode.SUCCESS){
            ToastUtil.show(context, "语音合成失败");
        }
    }

    /**
     * stopSpeak():停止发音
     * */
    public void stopSpeak(){
        if(mTts != null){
            if (mTts.isSpeaking()){
                mTts.stopSpeaking();
            }
        }
    }

    /**
     * 语音合成参数设置
     * */
    private void setParam(){
        //清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置相关参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);    //引擎设置
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);           //发音人设置
        mTts.setParameter(SpeechConstant.SPEED, mSpeed);                //设置语速
        mTts.setParameter(SpeechConstant.PITCH, mPitch);                //设置音调
        mTts.setParameter(SpeechConstant.VOLUME, mVolume);              //设置音量
    }

    /**
     * 初始化语音合成监听
     * */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS){
                //初始化失败的监听
            }else{
                //初始化监听成功，startSpeaking（）需在此之后，暂不做优化

            }
        }
    };

    /**
     * 初始化语音识别监听
     * */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            if(i != ErrorCode.SUCCESS){
                //初始化失败，暂不做处理
            }
        }
    };

    /**
     * 合成回调监听
     * */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            //开始播放
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            //合成进度
        }

        @Override
        public void onSpeakPaused() {
            //暂停播放
        }

        @Override
        public void onSpeakResumed() {
            //继续播放
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            //播放进度
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            //播放完成
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            //反馈给云端信息，项目中不使用
        }
    };

    /**
     * 听写监听器
     * */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            //音量变化
        }

        @Override
        public void onBeginOfSpeech() {
            //开始说话
        }

        @Override
        public void onEndOfSpeech() {
            //说话结束
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            //识别结果
            if (!b){
                printResult(recognizerResult, myEditText);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            //出现错误，可能录音机权限不够，可提示用户，暂不做优化
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            //回馈云端数据，项目中不使用
        }
    };

    /**
     * 将results信息输入EditText
     * 参数：results：所得结果
     *       editText:输入框
     * */
    private void printResult(RecognizerResult results, EditText editText){
        String oldText = editText.getText().toString();
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        //读取json中sn字段
        try{
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        }catch(JSONException e){
            //出现转化异常
        }
        mIatResults.put(sn, text);
        StringBuffer resultBuffer = new StringBuffer();
        for(String key:mIatResults.keySet()){
            resultBuffer.append(mIatResults.get(key));
        }
        String newText = resultBuffer.toString();
        editText.setText(oldText+newText);
        editText.setSelection(editText.length());
    }

    /**
     * 将results信息输入SearchView
     * 参数：results：所得结果
     *       editText:输入框
     * */
    private void printResultInSearchView(RecognizerResult results, SearchView searchView){
        String oldText = searchView.getQuery().toString();
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        //读取json中sn字段
        try{
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        }catch(JSONException e){
            //出现转化异常
        }
        mIatResults.put(sn, text);
        StringBuffer resultBuffer = new StringBuffer();
        for(String key:mIatResults.keySet()){
            resultBuffer.append(mIatResults.get(key));
        }
        String newText = resultBuffer.toString();
        searchView.setQuery(newText,true);     //设置为搜索框只接受新输入，清空原输入，方便查询
    }

    /**
     * 听写UI监听器
     * */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if(!b){
                if (isEditText){
                    printResult(recognizerResult, myEditText);
                }else {
                    printResultInSearchView(recognizerResult, mySearchView);
                }
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            //识别错误
        }
    };

    /**
     * 设置听写参数
     * */
    private void setIatParam(){
        //清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        //设置参数
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);   //设置引擎类型
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");        //设置返回结果形式
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");          // 设置语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");                 // 设置语言区域

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
    }

    /**
     * 设置听写UI参数
     * */
    private void setIatDialogParam(){
        //清空参数
        mIatDialog.setParameter(SpeechConstant.PARAMS, null);
        //开始设置
        mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE,mEngineType);  //设置引擎类型
        mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");        //设置返回结果形式
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");          // 设置语言
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");                 // 设置语言区域

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIatDialog.setParameter(SpeechConstant.ASR_PTT, "0");
    }


}

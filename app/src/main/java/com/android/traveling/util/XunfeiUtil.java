package com.android.traveling.util;

import android.content.Context;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.util
 * 文件名：XunfeiUtil
 * 创建者：HY
 * 创建时间：2019/2/1 20:42
 * 描述：  讯飞SDK工具
 */

@SuppressWarnings("unused")
public class XunfeiUtil {

    /**
     * 语音转文字
     */
    public static void showSpeechDialog(Context context, onRecognizerResult onRecognizerResult) {

        //1. 创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, code -> {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(context, "初始化失败 ", Toast.LENGTH_SHORT).show();
            }
        });
        //2. 设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");// 设置中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解
        // 结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener(onRecognizerResult));
        //4. 显示dialog，接收语音输入
        mDialog.show();
    }

    /**
     * 语音转文字相关接口
     */
    static class MyRecognizerDialogListener implements RecognizerDialogListener {

        // 用HashMap存储听写结果
        private HashMap<String, String> mIatResults = new LinkedHashMap<>();

        private onRecognizerResult onRecognizerResult;

        MyRecognizerDialogListener(onRecognizerResult onRecognizerResult) {
            this.onRecognizerResult = onRecognizerResult;
        }

        /**
         * @param results results
         * @param isLast  是否说完了
         */
        @Override
        public void onResult(com.iflytek.cloud.RecognizerResult results, boolean isLast) {
            String result = results.getResultString(); //未解析的
            //            showTip(result);
            System.out.println(" 没有解析的 :" + result);

            String text = parseIatResult(result);//解析过后的
            System.out.println(" 解析后的 :" + text);

            String sn = null;
            // 读取json结果中的 sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
                onRecognizerResult.onFaild(e);
            }

            mIatResults.put(sn, text);//没有得到一句，添加到

            StringBuilder resultBuffer = new StringBuilder();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            onRecognizerResult.onSuccess(resultBuffer.toString());

        }

        @Override
        public void onError(SpeechError speechError) {
            onRecognizerResult.onError(speechError);
        }
    }

    /**
     * 结果处理好之后的回调接口
     */
    public interface onRecognizerResult {
        void onSuccess(String result);

        void onFaild(JSONException e);

        void onError(SpeechError speechError);
    }

    private static String parseIatResult(String json) {
        StringBuilder ret = new StringBuilder();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
                //                  如果需要多候选结果，解析数组其他字段
                //                 for(int j = 0; j < items.length(); j++)
                //                 {
                //                      JSONObject obj = items.getJSONObject(j);
                //                      ret.append(obj.getString("w"));
                //                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public static String parseGrammarResult(String json) {
        StringBuilder ret = new StringBuilder();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject obj = items.getJSONObject(j);
                    if (obj.getString("w").contains("nomatch")) {
                        ret.append("没有匹配结果.");
                        return ret.toString();
                    }
                    ret.append("【结果】").append(obj.getString("w"));
                    ret.append("【置信度】 ").append(obj.getInt("sc"));
                    ret.append("\n ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret.append(" 没有匹配结果 .");
        }
        return ret.toString();
    }

    public static String parseLocalGrammarResult(String json) {
        StringBuilder ret = new StringBuilder();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject obj = items.getJSONObject(j);
                    if (obj.getString("w").contains("nomatch")) {
                        ret.append("没有匹配结果.");
                        return ret.toString();
                    }
                    ret.append("【结果】").append(obj.getString("w"));
                    ret.append("\n ");
                }
            }
            ret.append("【置信度】 ").append(joResult.optInt("sc"));
        } catch (Exception e) {
            e.printStackTrace();
            ret.append(" 没有匹配结果 .");
        }
        return ret.toString();
    }

}

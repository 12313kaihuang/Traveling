package com.android.traveling.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 项目名：XunFeiTest
 * 包名：  com.android.xunfeitest
 * 文件名：JsonParser
 * 创建者：HY
 * 创建时间：2019/1/30 20:18
 * 描述：  讯飞SDK 解析结果
 */

@SuppressWarnings("unused")
public class JsonParser {

    static String parseIatResult(String json) {
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

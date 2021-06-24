package com.yu.hu.libnetwork.encrypt;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Base64加密方式
 * 随机一个前缀与字符
 * <p>
 * Created by Hy on 2019/11/22 16:09
 *
 * @see IEncrypt
 */
@SuppressLint("ObsoleteSdkInt")
@SuppressWarnings({"unused", "CharsetObjectCanBeUsed"})
public class RandomEncryptImpl implements IEncrypt {

    private static final String TAG = "RandomEncryptImpl";

    private static int PREFIX = 'A';
    private static int PREFIX_MAX = 'z' - PREFIX;

    private Random mRandom;

    public RandomEncryptImpl() {
        mRandom = new Random(System.currentTimeMillis());
    }

    @Override
    public String encrypt(String value) {
        String result;
        //Base64.NO_WRAP 解决编码时换行问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            result = Base64.encodeToString(value.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
        } else {
            try {
                result = Base64.encodeToString(value.getBytes("UTF-8"), Base64.NO_WRAP);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                result = Base64.encodeToString(value.getBytes(), Base64.NO_WRAP);
            }
        }
        return random(result);
    }

    @NonNull
    @Override
    public String decrypt(String value) {
        byte[] decryptBytes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decryptBytes = restore(value).getBytes(StandardCharsets.UTF_8);
        } else {
            try {
                decryptBytes = restore(value).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                decryptBytes = restore(value).getBytes(StandardCharsets.UTF_8);
            }
        }

        try {
            //Base64.URL_SAFE 解决base64解码过程中特殊字符（+ - 等）出错
            return new String(Base64.decode(decryptBytes, Base64.URL_SAFE));
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("RandomEncryptImpl", "decrypt: ", e);
            return "";
        }
    }

    /**
     * 二次加密方式：
     * 假设base64编码后的串为J-W8oDIz5LiJMTNhdic=
     * 先生成一个随机数，最大为26(即'A'-'Z'的值)与原串长度的最小值，，假设是15
     * 1. 生成一个字符，值为'A'+15 = 'P'
     * 2. 生成一个数字，值为随机数%10  即5
     * 3. 先将生成的字符拼接到原串上，然后将生成的数字加到到随机数所对应的位置上，
     * 所以得出结果为PJ-W8oDIz5LiJMTNh5dic=
     *
     * @param encodeString 加密后的串
     * @return 二次加密后的串
     */
    private String random(String encodeString) {
        int prefix = mRandom.nextInt(encodeString.length());
        prefix = Math.min(prefix, PREFIX_MAX);
        char pChar = (char) (PREFIX + prefix);
        StringBuilder sBuilder = new StringBuilder().append(pChar);
        for (int i = 0; i < encodeString.length(); i++) {
            sBuilder.append(encodeString.charAt(i));
            if (prefix == i) {
                sBuilder.append(prefix % 10);
            }
        }
        return sBuilder.toString();
    }

    /**
     * @param decryptString 被二次加密的串
     * @return 待解密串
     */
    private String restore(String decryptString) {
        int prefix = decryptString.charAt(0) - PREFIX;
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < decryptString.length(); i++) {
            if (i == prefix + 2) continue;
            builder.append(decryptString.charAt(i));
        }
        return builder.toString();
    }
}

package com.yu.hu.common.utils.encrypt;

import android.os.Build;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.yu.hu.common.utils.LogUtil;

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
@SuppressWarnings({"unused", "CharsetObjectCanBeUsed"})
public class RandomEncryptImpl implements IEncrypt {

    private static int PREFIX = 'A';
    private static int PREFIX_MAX = 'z' - PREFIX;

    private Random mRandom;

    public RandomEncryptImpl() {
        mRandom = new Random(System.currentTimeMillis());
    }

    @Override
    public String encrypt(String value) {
        String result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            result = Base64.encodeToString(value.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        } else {
            try {
                result = Base64.encodeToString(value.getBytes("UTF-8"), Base64.DEFAULT);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                result = Base64.encodeToString(value.getBytes(), Base64.DEFAULT);
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
                decryptBytes = restore(value).getBytes();
            }
        }

        try {
            return new String(Base64.decode(decryptBytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.warn(e.getMessage());
            return "";
        }
    }

    /**
     * @param encodeString 加密后的串
     * @return 二次加密后的串
     */
    private String random(String encodeString) {
        int prefix = mRandom.nextInt(encodeString.length());
        prefix = prefix > PREFIX_MAX ? PREFIX_MAX : prefix;
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

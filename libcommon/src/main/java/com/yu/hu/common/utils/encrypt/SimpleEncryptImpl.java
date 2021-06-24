package com.yu.hu.common.utils.encrypt;

import android.os.Build;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.yu.hu.common.utils.Common;
import com.yu.hu.common.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


/**
 * base64加密
 * 加密解密的一个简单实现：固定前缀
 * Created by Hy on 2019/11/22 16:06
 *
 * @see IEncrypt
 */
@SuppressWarnings({"unused", "CharsetObjectCanBeUsed"})
public class SimpleEncryptImpl implements IEncrypt {

    private static final String PREFIX = Common.SIMPLE_ENCRYPT_PREFIX;

    @Override
    public String encrypt(String value) {
        StringBuilder builder = new StringBuilder(PREFIX);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            builder.append(Base64.encodeToString(value.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT));
        } else {
            try {
                builder.append(Base64.encodeToString(value.getBytes("UTF-8"), Base64.DEFAULT));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                builder.append(Base64.encodeToString(value.getBytes(), Base64.DEFAULT));
            }
        }
        return builder.toString();
    }

    @NonNull
    @Override
    public String decrypt(String value) {
        byte[] decryptBytes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decryptBytes = value.substring(PREFIX.length()).getBytes(StandardCharsets.UTF_8);
        } else {
            try {
                decryptBytes = value.substring(PREFIX.length()).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                decryptBytes = value.substring(PREFIX.length()).getBytes();
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
}


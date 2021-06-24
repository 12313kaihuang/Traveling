package com.yu.hu.libnetwork.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import java.security.SecureRandom;

import android.util.Base64;

/**
 * @author Hy
 * created on 2020/04/29 14:37
 * <p>
 * Des加密
 **/
public class DesEncryptUtil {

    /**
     * 加密解密方式
     */
    private final static String DES = "DES";
    /**
     * 秘钥
     */
    private final static String DEFAULT_KEY = "mOAmKLeRIFz15XOTE1PWjg==";
    /**
     * 字符编码
     */
    private final static String ENCODE = "UTF-8";


    /**
     * 加密 并用base64编码
     *
     * @param data data
     */
    public static String encrypt(String data) {
        try {
            byte[] bt = encrypt(data.getBytes(ENCODE), DEFAULT_KEY.getBytes(ENCODE));
            return Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * 解密 后并用base64解码
     */
    public static String decrypt(String data) {
        if (data == null)
            return null;
        try {
            byte[] buf = Base64.decode(data, Base64.URL_SAFE);
            byte[] bt = decrypt(buf, DEFAULT_KEY.getBytes(ENCODE));
            return new String(bt, ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    //测试 加密解密
    public static void main(String[] args) throws Exception {
        String data = "测试加密解密";
        // System.err.println(encrypt(data, key));
        // System.err.println(decrypt(encrypt(data, key), key));
        System.out.println(encrypt(data));
        System.out.println(decrypt(encrypt(data)));
    }
}

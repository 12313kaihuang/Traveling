package com.example.common;

import com.yu.hu.common.utils.EncryptUtils;

import org.junit.Test;

/**
 * create by hy on 2019/11/18 21:44
 */
public class RequestParamsTest {

    @Test
    public void test1() {
        String encrypt = EncryptUtils.getSimpleEncrypt().encrypt("你是da猪头");
        System.out.println("encrypt = " + encrypt);
        String decrypt = EncryptUtils.getSimpleEncrypt().decrypt(encrypt);
        System.out.println("decrypt = " + decrypt);
    }
}
package com.yu.hu.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.RequiresPermission;

import com.blankj.utilcode.util.NetworkUtils;

/**
 * Created by Hy on 2019/11/18 13:44
 * <p>
 * 网络相关Util
 *
 * @see NetworkUtils
 **/
@SuppressWarnings("unused")
public class NetWorkUtil {

    /**
     * 判断是否是移动网络
     *
     * @return true or false
     * @see NetworkUtils#isMobileData()
     */
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean isMobileData(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return null != activeNetwork
                && activeNetwork.isAvailable()
                && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

    }

}

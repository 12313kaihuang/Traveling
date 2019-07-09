package com.yu.hu.library.mvp;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.yu.hu.library.activity.BaseActivity;
import com.yu.hu.library.fragment.BaseFragment;
import com.yu.hu.library.impl.SubscriptionImpl;
import com.yu.hu.library.util.AppUtil;
import com.yu.hu.library.util.GsonUtil;

import rx.Subscription;


/**
 * 文件名：BasePresenter
 * 创建者：HY
 * 创建时间：2019/6/10 18:15
 * 描述：  Presenter基类
 * 有一个继承于{@link Presence}的泛型接口，用于Activity继承
 * 两个成员属性：
 * {@link #mContext} context
 * {@link #mInterface} impl接口
 * <p>
 * 封装了三个比较常用的方法：
 * {@link #getFromAssets(String)}  从Asserts去读取测试数据
 * {@link #fromJson(String, Class),#fromJson(JsonElement, Class)} fromJson
 * {@link #toJson(Object)} toJson
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BasePresenter<T extends Presence> {

    /**
     * 上下文
     * 一般就是继承了BaseImpl接口的那个Activity
     */
    protected final Context mContext;

    /**
     * 接口实例
     */
    protected T mInterface;

    public BasePresenter(T impl) {
        if (impl instanceof BaseFragment) {
            this.mContext = ((BaseFragment) impl).getContext();
        } else if (impl instanceof BaseActivity) {
            this.mContext = (Context) impl;
        } else {
            throw new RuntimeException("initialize faild:error impl");
        }
        this.mInterface = impl;
    }

    /**
     * 将Subscription加入{@link #mInterface}中
     */
    protected final void addSubscription(Subscription s) {
        ((SubscriptionImpl) mInterface).addSubscription(s);
    }

    /**
     * 从Assets中获取本地的数据
     *
     * @param fileName fileName
     */
    protected final String getFromAssets(String fileName) {
        return AppUtil.getFromAssets(fileName, mContext);
    }

    /**
     * fromJson
     */
    protected final <S> S fromJson(String json, Class<S> classOfT) throws JsonSyntaxException {
        return GsonUtil.fromJson(json, classOfT);
    }

    /**
     * fromJson
     */
    protected final <S> S fromJson(JsonElement json, Class<S> classOfT) throws JsonSyntaxException {
        return GsonUtil.fromJson(json, classOfT);
    }

    /**
     * toJson
     */
    protected final String toJson(Object o) {
        return GsonUtil.toJson(o);
    }
}

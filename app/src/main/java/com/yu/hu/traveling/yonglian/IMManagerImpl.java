package com.yu.hu.traveling.yonglian;

import android.content.Context;
import android.content.Intent;

import com.yu.hu.traveling.application.TravelingApplication;
import com.yu.hu.traveling.db.Controller.UserDaoController;
import com.yu.hu.traveling.entity.user.User;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.ToastUtil;
import com.yuntongxun.plugin.im.manager.bean.RETURN_TYPE;
import com.yuntongxun.plugin.im.manager.port.OnIMBindViewListener;
import com.yuntongxun.plugin.im.manager.port.OnMessagePreproccessListener;
import com.yuntongxun.plugin.im.manager.port.OnNotificationClickListener;
import com.yuntongxun.plugin.im.manager.port.OnReturnIdsCallback;
import com.yuntongxun.plugin.im.manager.port.OnReturnIdsClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 配置容联云的一些回调事件
 *
 * @see TravelingApplication#initIMPlugin()
 */

public class IMManagerImpl implements OnReturnIdsClickListener, OnIMBindViewListener, OnMessagePreproccessListener, OnNotificationClickListener {

    private final String TAG = LogUtil.getLogUtilsTag(getClass());

    private static List<String> HeadList = new ArrayList<>();

    private static IMManagerImpl instance;

    private OnReturnIdsCallback monReturnIdsCallback;

    private IMManagerImpl() {
    }

    private static String getNameById(String id) {
        return "User_" + id;
    }

    public static IMManagerImpl getInstance() {
        if (instance == null) {
            synchronized (IMManagerImpl.class) {
                instance = new IMManagerImpl();
            }
            HeadList.add("http://new-img4.ol-img.com/moudlepic/199_module_images/201612/5861ddf858113_790.jpg");
        }

        return instance;
    }

    public static void setResult(String... ids) {
        if (getInstance() == null) {
            return;
        }
        if (getInstance().monReturnIdsCallback == null) {
            return;
        }
        getInstance().monReturnIdsCallback.returnIds(ids);
        getInstance().monReturnIdsCallback = null;
    }


    /**
     * 此方法是为 设置聊天对象昵称
     *
     * @param userId 聊天对象的userId
     */
    @Override
    public String onBindNickName(Context context, String userId) {
        LogUtil.d("traveling", "onBindNickName userId = " + userId);
        User byId = UserDaoController.getInstance().findById(Long.valueOf(userId));
        if (byId != null) {
            return byId.getNickName();
        }
        return getNameById(userId);
    }

    /**
     * 此方法是为 配置聊天对象的头像点击事件
     *
     * @param id 聊天对象的userId
     */
    @Override
    public void OnAvatarClickListener(Context context, String id) {
        ToastUtil.showMessage("点击了" + getNameById(id) + "的头像");
    }

    /**
     * 此方法是为 配置聊天对象的头像
     *
     * @param userId 聊天对象的userId
     * @return 返回值为头像的 url 地址
     */
    @Override
    public String onBindAvatarByUrl(Context context, String userId) {
        LogUtil.d("traveling", "onBindAvatarByUrl userId = " + userId);
        User byId = UserDaoController.getInstance().findById(Long.valueOf(userId), false);
        if (byId != null) {
            return byId.getImgUrl();
        }
        return HeadList.get(0);
    }

    /**
     * 此方法为 设置 返回id点击操作 （如添加成员 和 转发事件）点击事件
     *
     * @param return_type RETURN_TYPE.ADDMEMBER_USERID 为 添加成员点击事件
     *                    RETURN_TYPE.TRANSMIT_CONTACTID 为 转发消息点击事件
     * @param callback    返回id 的回调
     * @param ids         可变数组 加号点击时，单聊抛出聊天对象的id
     *                    群聊抛出群组的所有成员的ids
     */
    @Override
    public void onReturnIdsClick(Context context, RETURN_TYPE return_type, OnReturnIdsCallback callback, String... ids) {
        monReturnIdsCallback = callback;
        //当转发和增加群成员用于同个界面时，用当前枚举做判断
        if (return_type == RETURN_TYPE.ADDMEMBER_USERID) {
            //跳转到开发者指定的添加成员的页面
            //context.startActivity(new Intent(context, AddOthersIntoGroup.class));
        } else if (return_type == RETURN_TYPE.TRANSMIT_CONTACTID) {
            //跳转到开发者指定的转发消息的页面
            //context.startActivity(new Intent(context, AddOthersIntoGroup.class));
        }
    }

    //IM消息转发接口
    @Override
    public boolean dispatchMessage(ECMessage ecMessage) {
        // 返回true意义是消费当前这条消息不交给插件内部处理,返回false意义是交给插件进行处理
        if (ecMessage.getForm().equals("10086")) {
            LogUtil.d(TAG, "dispatchMessage 10086...");
            return true;
        }
        return false;
    }

    /**
     * 此方法为 设置IM通知消息点击事件,如果不配置默认跳转到聊天页面
     *
     * @param context   context
     * @param contactId 如果是单聊通知消息则是聊天对象的userID，若是群组消息则是groupID
     * @param intent    用来跳转页面的intent 开发者来指定跳转的页面
     */
    @Override
    public void onNotificationClick(Context context, String contactId, Intent intent) {
        // 代码示例，开发者只需要配置好intent，指定跳转的页面及传递的数据 开发者不需要调用startActivity（）
        //        intent.setClassName(context, "com.ccpress.ronglianim_test.activity.MainActivity");
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        intent.putExtra("contactId", contactId);
    }
}

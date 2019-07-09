package com.yu.hu.traveling.application;

import com.tencent.bugly.crashreport.CrashReport;
import com.yu.hu.library.application.BaseApplication;
import com.yu.hu.library.util.RetrofitUtil;
import com.yu.hu.traveling.entity.Const;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.db.GreenDaoOpenHelper;
import com.yu.hu.traveling.yonglian.IMManagerImpl;
import com.yu.hu.traveling.yonglian.custom.OnBindViewHolderListenerImpl;
import com.yuntongxun.plugin.common.SDKCoreHelper;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.greendao3.helper.DaoHelper;
import com.yuntongxun.plugin.im.dao.helper.IMDao;
import com.yuntongxun.plugin.im.manager.IMPluginHelper;
import com.yuntongxun.plugin.im.manager.IMPluginManager;
import com.yuntongxun.plugin.im.manager.bean.IMConfiguration;

import androidx.multidex.MultiDex;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.application
 * 文件名：TravelingApplication
 * 创建者：HY
 * 创建时间：2019/6/15 16:29
 * 描述：  TravelingApplication
 */
public class TravelingApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化RetrofitUtil baseUrl
        RetrofitUtil.init(Const.URL);

        //GreenDao
        GreenDaoOpenHelper.initDatabase(this);

        //Bugly 第三个参数为SDK调试模式开关
        CrashReport.initCrashReport(getApplicationContext(), Const.APP_ID_BUGLY, false);

        //容联云
        initRongLian();
    }

    /**
     * 容联云的一些初始化操作
     */
    private void initRongLian() {

        if (!IMPluginHelper.shouldInit(this)) {
            //防止多进程初始化多次
            return;
        }

        MultiDex.install(this);
        //初始化插件上下文
        SDKCoreHelper.setContext(this);
        // 插件日志开启(放在setContext之后)
        LogUtil.setDebugMode(true);
        //初始化数据库
        DaoHelper.init(this, new IMDao());
        //DaoHelper.init(this, new IMDao(),new IMDao());

        initIMPlugin();
    }

    private void initIMPlugin() {

        IMManagerImpl instance = IMManagerImpl.getInstance();
        IMConfiguration imConfiguration1 = new IMConfiguration.IMConfigBuilder(this)
                //此方法为 初始化头像，昵称，头像点击事件的
                .setOnIMBindViewListener(instance)
                //设置IM通知消息点击事件,如果不配置默认跳转到聊天页面
                .setOnNotificationClickListener(instance)
                // 沟通列表和聊天列表的UI自定义接口（私有云接口）
                .setOnBindViewHolderListener(new OnBindViewHolderListenerImpl())
                // IM消息转发接口 （私有云接口）
                .setOnMessagePreproccessListener(instance)
                //此方法为 设置 返回id点击操作 （如添加成员 和 转发事件）点击事件
                // 单聊或群聊那个加号点击事件
                .setOnReturnIdsClickListener(instance)
                // (私有云功能)
                .showMsgState(true)
                .notifyIcon(R.mipmap.ic_launcher)
                .build();

        IMPluginManager.getManager().init(imConfiguration1);
    }

    @Override
    protected String getApplicationName() {
        return getResources().getString(R.string.app_name);
    }
}

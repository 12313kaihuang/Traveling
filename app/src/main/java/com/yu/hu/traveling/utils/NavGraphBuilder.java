package com.yu.hu.traveling.utils;

import android.content.ComponentName;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.blankj.utilcode.util.AppUtils;
import com.yu.hu.common.navigation.FixFragmentNavigator;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.traveling.model.Destination;

import java.util.HashMap;

/**
 * @author Hy
 * created on 2020/04/15 10:52
 * <p>
 * 通过自定义的Destination构建页面导航
 **/
@SuppressWarnings("unused")
public class NavGraphBuilder {

    private static final String TAG = NavGraphBuilder.class.getSimpleName();

    /**
     * 构建NavGraph
     *
     * @param fragment 要传的是navHostFragment实例，因为navigation是其他控制fragment的显示的
     */
    public static void buildTabGraph(Fragment fragment, NavController controller) {
        int containerId = fragment.getId();
        NavigatorProvider provider = controller.getNavigatorProvider();

        //NavGraphNavigator也是页面路由导航器的一种，只不过他比较特殊。
        //它只为默认的展示页提供导航服务,但真正的跳转还是交给对应的navigator来完成的
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        //FragmentNavigator fragmentNavigator = provider.getNavigator(FragmentNavigator.class);
        //fragment的导航此处使用我们定制的FixFragmentNavigator，底部Tab切换时 使用hide()/show(),而不是replace()
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(fragment.requireContext(), fragment.getChildFragmentManager(), containerId);
        provider.addNavigator(fragmentNavigator);
        ActivityNavigator activityNavigator = provider.getNavigator(ActivityNavigator.class);

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        for (Destination dest : destConfig.values()) {
            if (!dest.tabPage) {
                continue;
            }
            LogUtil.i(TAG, "-build - " + dest.className);
            if (dest.isFragment) {
                FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setId(dest.id);
                destination.setClassName(dest.className);
                destination.addDeepLink(dest.pageUrl);
                navGraph.addDestination(destination);
            } else {
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setId(dest.id);
                destination.setComponentName(new ComponentName(AppUtils.getAppPackageName(), dest.className));
                destination.addDeepLink(dest.pageUrl);
                navGraph.addDestination(destination);
            }

            //给APP页面导航结果图 设置一个默认的展示页的id
            if (dest.asStarter) {
                navGraph.setStartDestination(dest.id);
            }
        }

        controller.setGraph(navGraph);
    }

    /**
     * 构建NavGraph
     *
     * @param fragment 要传的是navHostFragment实例，因为navigation是其他控制fragment的显示的
     *                 构建NavGraph  不包含tab几个页面
     */
    public static void buildMainNavGraph(Fragment fragment, NavController controller) {
        int containerId = fragment.getId();
        NavigatorProvider provider = controller.getNavigatorProvider();
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        //FragmentNavigator fragmentNavigator = provider.getNavigator(FragmentNavigator.class);
        //fragment的导航此处使用我们定制的FixFragmentNavigator，底部Tab切换时 使用hide()/show(),而不是replace()
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(fragment.requireContext(), fragment.getChildFragmentManager(), containerId);
        provider.addNavigator(fragmentNavigator);
        ActivityNavigator activityNavigator = provider.getNavigator(ActivityNavigator.class);

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        for (Destination dest : destConfig.values()) {
            if (dest.tabPage) {
                //过滤掉tabPage
                continue;
            }

            LogUtil.i(TAG, "- build -" + dest.className);
            if (dest.isFragment) {
                FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setId(dest.id);
                destination.setClassName(dest.className);
                destination.addDeepLink(dest.pageUrl);
                navGraph.addDestination(destination);
            } else {
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setId(dest.id);
                destination.setComponentName(new ComponentName(AppUtils.getAppPackageName(), dest.className));
                destination.addDeepLink(dest.pageUrl);
                navGraph.addDestination(destination);
            }

            //给APP页面导航结果图 设置一个默认的展示页的id
            if (dest.asStarter) {
                navGraph.setStartDestination(dest.id);
            }
        }

        controller.setGraph(navGraph);
    }

    /**
     *
     */
    private static void buildMainNavGraph(Context context, FragmentManager fragmentManager,
                                          NavController controller, int containerId) {
        NavigatorProvider provider = controller.getNavigatorProvider();

        //NavGraphNavigator也是页面路由导航器的一种，只不过他比较特殊。
        //它只为默认的展示页提供导航服务,但真正的跳转还是交给对应的navigator来完成的
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        //FragmentNavigator fragmentNavigator = provider.getNavigator(FragmentNavigator.class);
        //fragment的导航此处使用我们定制的FixFragmentNavigator，底部Tab切换时 使用hide()/show(),而不是replace()
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(context, fragmentManager, containerId);
        provider.addNavigator(fragmentNavigator);
        ActivityNavigator activityNavigator = provider.getNavigator(ActivityNavigator.class);

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        for (Destination dest : destConfig.values()) {
            LogUtil.i("NavGraphBuilder.build - " + dest.className);
            if (dest.isFragment) {
                FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setId(dest.id);
                destination.setClassName(dest.className);
                destination.addDeepLink(dest.pageUrl);
                navGraph.addDestination(destination);
            } else {
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setId(dest.id);
                destination.setComponentName(new ComponentName(AppUtils.getAppPackageName(), dest.className));
                destination.addDeepLink(dest.pageUrl);
                navGraph.addDestination(destination);
            }

            //给APP页面导航结果图 设置一个默认的展示页的id
            if (dest.asStarter) {
                navGraph.setStartDestination(dest.id);
            }
        }

        controller.setGraph(navGraph);
    }
}

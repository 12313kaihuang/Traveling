package com.yu.hu.common.navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

import com.yu.hu.common.utils.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;

/**
 * @author Hy
 * created on 2020/04/14 22:40
 * <p>
 * 自定义FragmentNavigator，
 * 切换fragment时将通过hide/show的方式先隐藏而不是原生的replace
 **/
@SuppressWarnings("unused")
@Navigator.Name("fixfragment")
public class FixFragmentNavigator extends FragmentNavigator {

    private static final String TAG = FixFragmentNavigator.class.getSimpleName();

    @NonNull
    private final Context mContext;
    @NonNull
    private final FragmentManager mManager;
    private final int mContainerId;

    public FixFragmentNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        this.mContext = context;
        this.mManager = manager;
        this.mContainerId = containerId;
    }

    /**
     * 重写此方法
     * 将Fragment切换方式改为hide/show
     */
    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        LogUtil.d("navigate - destination = " + destination.getClassName());
        if (mManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already"
                    + " saved its state");
            return null;
        }
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = mContext.getPackageName() + className;
        }
        //final Fragment frag = instantiateFragment(mContext, mManager, className, args);
        //获取需要显示的fragment
        String tag = className.substring(className.lastIndexOf(".") + 1);
        Fragment frag = mManager.findFragmentByTag(tag);
        if (frag == null) {
            //noinspection deprecation
            frag = instantiateFragment(mContext, mManager, className, args);
        }
        frag.setArguments(args);

        final FragmentTransaction ft = mManager.beginTransaction();

        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = enterAnim != -1 ? enterAnim : 0;
            exitAnim = exitAnim != -1 ? exitAnim : 0;
            popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
            popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }

        //隐藏当前显示的fragment 隐藏所有fragment 避免出现fragment重叠问题
        List<Fragment> fragments = mManager.getFragments();
        for (Fragment fragment : fragments) {
            ft.hide(fragment);
        }

        if (!frag.isAdded()) {
            ft.add(mContainerId, frag, tag);
        }
        ft.show(frag);
        //ft.replace(mContainerId, frag);
        ft.setPrimaryNavigationFragment(frag);

        final @IdRes int destId = destination.getId();

        //通过反射 获取到mBackStack
        ArrayDeque<Integer> mBackStack = null;
        try {
            Field field = FragmentNavigator.class.getDeclaredField("mBackStack");
            field.setAccessible(true);
            //noinspection unchecked
            mBackStack = (ArrayDeque<Integer>) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


        //noinspection ConstantConditions
        final boolean initialNavigation = mBackStack.isEmpty();
        //noinspection ConstantConditions
        final boolean isSingleTopReplacement = navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId;

        boolean isAdded;
        if (initialNavigation) {
            isAdded = true;
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size() > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                //noinspection ConstantConditions
                mManager.popBackStack(
                        generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.addToBackStack(generateBackStackName(mBackStack.size(), destId));
            }
            isAdded = false;
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size() + 1, destId));
            isAdded = true;
        }
        if (navigatorExtras instanceof Extras) {
            Extras extras = (Extras) navigatorExtras;
            for (Map.Entry<View, String> sharedElement : extras.getSharedElements().entrySet()) {
                ft.addSharedElement(sharedElement.getKey(), sharedElement.getValue());
            }
        }
        ft.setReorderingAllowed(true);
        ft.commit();
        // The commit succeeded, update our view of the world
        if (isAdded) {
            mBackStack.add(destId);
            return destination;
        } else {
            return null;
        }
    }

    /**
     * 当A -> B -> C，C返回时，会同时触发A和B的onHiddenChanged方法且都会显示出来，
     * 即Fragment重叠问题，源码暂时没找到根源在哪里，所以暂时通过{@link #hideExtraFragment()}手动隐藏
     */
    @Override
    public boolean popBackStack() {
        boolean b = super.popBackStack();
        List<Fragment> fragments = mManager.getFragments();
        Fragment primaryNavigationFragment = mManager.getPrimaryNavigationFragment();
        printFragments(fragments);
        LogUtil.d("popBackStack1 - size = " + fragments.size() + ", primary = " + primaryNavigationFragment.getClass().getName());
        hideExtraFragment();
        return b;
    }

    /**
     * 应该{@link FragmentManager#popBackStack()}这个方法会导致其中所有Fragment显示出来，
     * 所以这里手动hide一下
     */
    private void hideExtraFragment() {
        List<Fragment> fragments = mManager.getFragments();
        if (fragments.size() < 2) return;
        final FragmentTransaction ft = mManager.beginTransaction();
        for (int i = 0; i < fragments.size() - 2; i++) {
            Fragment fragment = fragments.get(i);
            LogUtil.d(" hide - " + fragment.getClass().getSimpleName());
            ft.hide(fragment);
        }
        ft.commit();
    }

    //    private Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            List<Fragment> fragments = mManager.getFragments();
//            printFragments(fragments);
//            Fragment primaryNavigationFragment = mManager.getPrimaryNavigationFragment();
//            LogUtil.d("popBackStack" + msg.what + " - size = " + fragments.size() + ", primary = " + primaryNavigationFragment.getClass().getName());
//        }
//    };
//
    private void printFragments(List<Fragment> fragments) {
        StringBuilder builder = new StringBuilder();
        for (Fragment fragment : fragments) {
            builder.append(fragment.getClass().getSimpleName())
                    .append(" - ");
        }
        LogUtil.d("popBackStack fragments - " + builder.toString());
    }

    //父类中是private类型的 所以这里重写一遍
    private String generateBackStackName(int backStackIndex, int destid) {
        return backStackIndex + "-" + destid;
    }
}

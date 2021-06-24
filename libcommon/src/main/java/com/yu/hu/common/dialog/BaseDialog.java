package com.yu.hu.common.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.yu.hu.common.R;
import com.yu.hu.common.application.BaseApplication;
import com.yu.hu.common.exception.DialogShowErrorException;
import com.yu.hu.common.utils.LogUtil;

import java.lang.reflect.InvocationTargetException;


/**
 * Created by Hy on 2019/11/18 19:51
 * <p>
 * dialog基类
 * <p>
 * 有一些属性是预先设置的，如{@link #title}，
 * 没有对外暴露，由具体实现类选择是否需要对外暴露。
 * <p>
 * 注意：
 * {@link #show()}该方法需要使用{@link BaseApplication}或者继承该类才有用，否则会抛出异常。
 *
 * @param <DF> 具体的BaseDialog实现类
 * @param <DB> Dialog对应的DataBinding
 * @see #show() 该方法需要使用{@link BaseApplication}或者继承该类才有用，否则会抛出异常。
 * @see LoadingDialog
 */
@SuppressWarnings({"WeakerAccess", "unused", "unchecked"})
public abstract class BaseDialog<DF extends BaseDialog, DB extends ViewDataBinding> extends DialogFragment {

    protected Context mContext;
    protected DB mDataBinding;
    protected LayoutInflater mLayoutInflater;

    /* *****************基础属性***************** */

    protected int mWidth = WindowManager.LayoutParams.WRAP_CONTENT;

    protected int mHeight = WindowManager.LayoutParams.WRAP_CONTENT;

    protected int mGravity = Gravity.CENTER;

    @StyleRes
    protected int mTheme = R.style.BaseDialog;

    @StyleRes
    protected int mAnimation = R.style.PopStyle;

    /* *****************自定义属性***************** */
    /**
     * 点击按钮后是否自动dismiss
     */
    protected boolean autoDismiss = true;

    //title
    protected String title;

    @StringRes
    protected int titleRes;

    @ColorRes
    protected int titleColorRes;


    //content
    protected String content;

    @StringRes
    protected int contentRes;

    @ColorRes
    protected int contentColorRes;


    //左边按钮
    protected String negativeBtnText;

    @StringRes
    protected int negativeBtnTextRes;

    @ColorRes
    protected int negativeBtnColorRes;

    protected BtnClickListener negativeBtnClickListener;


    //右边按钮
    protected String positiveBtnText;

    @StringRes
    protected int positiveBtnTextRes;

    @ColorRes
    protected int positiveBtnColorRes;

    protected BtnClickListener positiveBtnClickListener;

    protected ShowErrorListener showErrorListener;

    /**
     * 需要注册{@link BaseApplication}才能使用
     * <p>
     * 通过反射获取到Application然后获取到栈顶Activity
     *
     * @throws DialogShowErrorException when application or topActivity is not needed
     */
    public void show() {
        String tag = getClass().getSimpleName();
        String errorInfo = null;
        Application applicationByReflect = getApplicationByReflect();
        LogUtil.info(tag, "show: Application = " + (applicationByReflect == null ? "null" : applicationByReflect.getClass().getName()));
        if (applicationByReflect instanceof BaseApplication) {
            Activity topActivity = ((BaseApplication) applicationByReflect).getTopActivity();
            LogUtil.info(tag, "show: topActivity = " + (topActivity == null ? "null" : topActivity.getClass().getName()));
            //这里也需要注意 fragmentManager是从FragmentActivity中获取的
            if (topActivity instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) topActivity;
                show(fragmentActivity.getSupportFragmentManager());
                return;
            }
        }

        if (showErrorListener != null) {
            showErrorListener.onShowError();
        }else {
            throw new DialogShowErrorException("Application or TopActivity is error");
        }
    }

    /**
     * 简化show方法
     *
     * @param fragmentManager Activity传{@link AppCompatActivity#getSupportFragmentManager()} ；
     *                        Fragment传{@link Fragment#getChildFragmentManager()}
     */
    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, null);
    }

    /**
     * show with showErrorListener
     * @param showErrorListener showErrorListener
     */
    public void show(ShowErrorListener showErrorListener) {
        setShowErrorListener(showErrorListener);
        show();
    }

    /**
     * 获取layoutId
     * 也可以直接通过DataBinding做：eg:DialogLoadingBinding.inflate(mLayoutInflater, container, false)
     *
     * @see #onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @LayoutRes
    protected abstract int getLayoutId();

    @CallSuper
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onInit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(mLayoutInflater, getLayoutId(), container, false);
        onInitView(savedInstanceState);
        return mDataBinding.getRoot();
    }

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInitEvents();
    }

    /**
     * 第一步执行
     * 一些初始化操作
     * <p>
     * 注意这些方法都是在{@link #show(FragmentManager)}方法调用后才执行的，
     * 及在Fragment创建时才一一开始执行
     *
     * @see #onCreate(Bundle)
     */
    @CallSuper
    protected void onInit() {

        this.mLayoutInflater = LayoutInflater.from(mContext);

        this.title = titleRes == 0 ? title
                : getResources().getString(titleRes);

        this.content = contentRes == 0 ? content
                : getResources().getString(contentRes);

        this.positiveBtnText = positiveBtnTextRes == 0 ? positiveBtnText
                : getResources().getString(positiveBtnTextRes);

        this.negativeBtnText = negativeBtnTextRes == 0 ? negativeBtnText
                : getResources().getString(negativeBtnTextRes);

        if (getArguments() != null) {
            onInitArguments(getArguments());
        }
    }


    /**
     * 第二步执行
     * arguments的一些初始化操作
     *
     * @see #onInit()
     */
    protected void onInitArguments(@NonNull Bundle arguments) {

    }


    /**
     * 第三步执行
     * 初始化View
     * 尽量不要执行耗时操作，否则会影响dialog的显示，
     * 点击事件可以放在{@link #onInitEvents()}中执行
     *
     * @param savedInstanceState savedInstanceState
     * @see #onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @CallSuper
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        setStyle();
    }

    /**
     * 第四步执行
     * 初始化事件如点击事件等
     *
     * @see #onViewCreated(View, Bundle)
     */
    protected void onInitEvents() {

    }

    /**
     * 有关style的一些初始化操作
     * 如果设置背景透明等
     */
    private void setStyle() {
        //无title  自定义主题
        setStyle(DialogFragment.STYLE_NO_TITLE, mTheme);

        Dialog dialog = getDialog();
        if (dialog == null) {
            LogUtil.warn(getClass().getSimpleName(), "dialog == null");
            return;
        }

        //dialog无title
        dialog.requestWindowFeature(STYLE_NO_TITLE);

        Window window = dialog.getWindow();
        if (window == null) {
            LogUtil.warn(getClass().getSimpleName(), "dialog.window == null");
            return;
        }

        //透明背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent, null)));
        } else {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        }

        //设置宽高
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = mWidth;
        attributes.height = mHeight;
        attributes.gravity = mGravity;
        //设置动画
        window.setWindowAnimations(mAnimation);
        window.setAttributes(attributes);

    }


    /* ***************protected 类型  可通过子类重写暴露给调用者***************** */

    /**
     * 设置标题
     *
     * @param titleRes StringRes
     */
    @CallSuper
    protected DF setTitle(@StringRes int titleRes) {
        this.titleRes = titleRes;
        return (DF) this;
    }

    /**
     * 设置标题
     *
     * @param title content
     */
    @CallSuper
    protected DF setTitle(String title) {
        this.titleRes = 0;
        this.title = title;
        return (DF) this;
    }

    /**
     * 设置提示内容
     *
     * @param contentRes StringRes
     */
    @CallSuper
    protected DF setContent(@StringRes int contentRes) {
        this.contentRes = contentRes;
        return (DF) this;
    }

    /**
     * 设置提示内容
     *
     * @param content content
     */
    @CallSuper
    protected DF setContent(String content) {
        this.contentRes = 0;
        this.content = content;
        return (DF) this;
    }

    /**
     * 设置NegativeBtn 左边按钮
     *
     * @param textRes StringRes
     */
    @CallSuper
    protected DF setNegativeBtnText(@StringRes int textRes) {
        this.negativeBtnTextRes = textRes;
        return (DF) this;
    }

    /**
     * 设置NegativeBtn 左边按钮
     *
     * @param text text
     */
    @CallSuper
    protected DF setNegativeBtnText(String text) {
        this.negativeBtnTextRes = 0;
        this.negativeBtnText = text;
        return (DF) this;
    }

    /**
     * 设置PositiveBtn 右边按钮
     *
     * @param textRes StringRes
     */
    @CallSuper
    protected DF setPositiveBtnText(@StringRes int textRes) {
        this.positiveBtnTextRes = textRes;
        return (DF) this;
    }

    /**
     * 设置PositiveBtn 右边按钮
     *
     * @param text text
     */
    @CallSuper
    protected DF setPositiveBtnText(String text) {
        this.positiveBtnTextRes = 0;
        this.positiveBtnText = text;
        return (DF) this;
    }


    /**
     * 设置show失败时的错误
     *
     * @param showErrorListener showErrorListener
     */
    @SuppressWarnings("UnusedReturnValue")
    public DF setShowErrorListener(ShowErrorListener showErrorListener) {
        this.showErrorListener = showErrorListener;
        return (DF) this;
    }
    /* **************************通用的setter方法****************************** */

    public DF setCancelAbility(boolean cancelable) {
        setCancelable(cancelable);
        return (DF) this;
    }

    public DF setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
        return (DF) this;
    }

    public DF setWidth(int mWidth) {
        this.mWidth = mWidth;
        return (DF) this;
    }

    public DF setHeight(int mHeight) {
        this.mHeight = mHeight;
        return (DF) this;
    }

    public DF setGravity(int mGravity) {
        this.mGravity = mGravity;
        return (DF) this;
    }

    public DF setTheme(@StyleRes int mTheme) {
        this.mTheme = mTheme;
        return (DF) this;
    }

    public DF setAnimation(@StyleRes int mAnimation) {
        this.mAnimation = mAnimation;
        return (DF) this;
    }

    /**
     * 通过反射获取Application
     *
     * @return Application
     */
    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    /**
     * button点击事件
     */
    public interface BtnClickListener {
        void onBtnClicked(View v);
    }

    /**
     * showError
     */
    public interface ShowErrorListener {
        void onShowError();
    }
}

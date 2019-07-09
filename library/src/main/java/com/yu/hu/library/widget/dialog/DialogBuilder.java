package com.yu.hu.library.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yu.hu.library.R;
import com.yu.hu.library.util.LogUtil;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;


/**
 * 文件名：DialogBuilder
 * 创建者：HY
 * 创建时间：2019/6/26 21:24
 * 描述：  DialogBuilder  抽象类，
 * 主要用于构建自定义Dialog
 *
 * @param <D> Dialog类型
 * @see DialogFactory
 * @see HLoadingDialog
 * @see #onCreateDialog()
 * @see #onInitParams()
 * @see #onCreated(Dialog)
 * @see #onCanceled(Dialog)
 * @see #findViewById(int)
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class DialogBuilder<T, D extends Dialog>
        implements LifeCycleListener {
    protected Context mContext;

    protected D mDialog;

    //布局文件id
    @LayoutRes
    protected int mLayoutResId;

    //主题id
    @StyleRes
    protected int mThemeResId;

    //动画id
    @StyleRes
    protected int mAnimResId;

    //宽高
    protected int mDialogHeight, mDialogWidth;

    //Gravity
    protected int mDialogGravity;

    //可否取消
    protected boolean mDialogCancelable;

    //监听器
    protected LifeCycleListener mLifeCycleListener;

    public DialogBuilder(Context context, @LayoutRes int layoutRes) {
        this.mContext = context;
        this.mLayoutResId = layoutRes;

        //默认属性值
        mThemeResId = R.style.TransparentDialog;
        mAnimResId = R.style.pop_anim_style;
        mDialogWidth = mDialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialogGravity = Gravity.CENTER;
        mDialogCancelable = true;
    }

    /**
     * 构建Dialog实例，
     * 一般调用对应的{@link Dialog#Dialog(Context, int)}这个方法返回就行
     */
    protected abstract D onCreateDialog();

    public final D build() {
        mDialog = onCreateDialog();
        initParams();
        return mDialog;
    }

    /**
     * 用于给子类新增的属性设置默认值
     */
    protected void onInitParams() {

    }

    /**
     * 构建Dialog属性
     * 扩展方法可在{@link #onCreated(Dialog)}中添加
     */
    private void initParams() {

        if (mDialog == null) {
            throw new RuntimeException("Dialog has not been initialized");
        }

        mDialog.setContentView(mLayoutResId);
        mDialog.setCancelable(mDialogCancelable);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams layoutParams;
        if (window != null) {
            layoutParams = window.getAttributes();

            layoutParams.width = mDialogWidth;
            layoutParams.height = mDialogHeight;
            layoutParams.gravity = mDialogGravity;

            window.setAttributes(layoutParams);
            window.setWindowAnimations(mAnimResId);
        } else {
            LogUtil.e("DialogFactory init faild : getWindow() == null");
        }

        //用于给子类新增的属性设置默认值
        onInitParams();

        //create
        onCreated(mDialog);
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onCreated(mDialog);
        }

        //cancel
        mDialog.setOnCancelListener(dialog -> {
            onCanceled(mDialog);
            if (mLifeCycleListener != null) mLifeCycleListener.onCanceled(mDialog);
        });

    }

    @Override
    public void onCreated(Dialog dialog) {

    }

    @Override
    public void onCanceled(Dialog dialog) {

    }

    /**
     * 返回子类builder的实例对象
     * 一般{@code  return this} 就行
     */
    protected abstract T returnThis();

    /**
     * 设置高度
     */
    public T setHeight(int height) {
        this.mDialogHeight = height;
        return returnThis();
    }

    /**
     * 设置宽度
     */
    public T setWidth(int width) {
        this.mDialogWidth = width;
        return returnThis();
    }

    /**
     * 设置Gravity
     * <p>
     * {@link Gravity}
     */
    public T setGravity(int gravity) {
        this.mDialogGravity = gravity;
        return returnThis();
    }

    /**
     * 设置主题
     */
    public T setTheme(@StyleRes int themeResId) {
        this.mThemeResId = themeResId;
        return returnThis();
    }

    /**
     * 设置动画
     */
    public T setAnimation(@StyleRes int animResId) {
        this.mAnimResId = animResId;
        return returnThis();
    }

    /**
     * 可否点击外部取消
     */
    public T setCancelable(boolean cancelable) {
        this.mDialogCancelable = cancelable;
        return returnThis();
    }

    /**
     * 生命周期监听
     */
    public T setLifeCycleListener(LifeCycleListener listener) {
        this.mLifeCycleListener = listener;
        return returnThis();
    }

    /**
     * findViewById
     */
    protected <V extends View> V findViewById(@IdRes int id) {

        if (mDialog == null) {
            throw new RuntimeException("Dialog has not been initialized");
        }

        return mDialog.findViewById(id);
    }
}

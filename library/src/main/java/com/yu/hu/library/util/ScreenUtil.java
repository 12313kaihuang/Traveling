package com.yu.hu.library.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

@SuppressWarnings("unused")
public class ScreenUtil {

	public static int dip2px(Context context, int size) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources()
				.getDisplayMetrics());
	}

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

	public static int sp2px(Context context, int size) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, context.getResources()
				.getDisplayMetrics());
	}

	public static int getScreenWidth(Context context) {
		if (context != null)
			return context.getResources().getDisplayMetrics().widthPixels;
		else
			return 1;
	}

	public static int getScreenHeight(Context context) {
		if (context != null)
			return context.getResources().getDisplayMetrics().heightPixels;
		else
			return 1;
	}

	public static float getScreenDensity(Context context) {
		if (context != null)
			return context.getResources().getDisplayMetrics().density;
		else
			return 1f;
	}

	public static int getScreenDensityDpi(Context context) {
		if (context != null)
			return context.getResources().getDisplayMetrics().densityDpi;
		else
			return 1;
	}

	/**
	 * 将Dip转换为px
	 *
	 */
	public static int dip2px(Context context, float dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

    /**
     * 获取虚拟键盘（虚拟导航栏）的高度，
     * @return px值
     */
    public static int getNavigationBarHeight(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            Point point = new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
            return point.y;
        }

        // navigation bar is not present
        return 0;
    }

    private static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {
//                MonitorUtil.reportError(ScreenUtil.class, e);
                e.printStackTrace();
            }
        }

        return size;
    }

}

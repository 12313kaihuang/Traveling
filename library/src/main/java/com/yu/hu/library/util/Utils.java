package com.yu.hu.library.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.Random;

/**
 * 文件名：Utils
 * 创建者：HY
 * 创建时间：2019/6/21 11:58
 * 描述：  无处安放的Util
 */
@SuppressWarnings("unused")
public class Utils {

    /*AndroidUtilsCode 的一些说明
     * https://blankj.com/2016/07/31/android-utils-code/
     * https://blog.csdn.net/qq_33847549/article/details/73840901
     * 正则相关→[RegexUtils.java][regex.java]→[Test][regex.test]
     * Activity相关→[ActivityUtils.Java][activity.java]
     * 屏幕相关→[ScreenUtils.java][screen.java]
     * 常量相关→[ConstUtils.java][const.java]
     * 判空相关→[EmptyUtils.java][empty.java]→[Test][empty.test]
     * 编码解码相关→[EncodeUtils.java][encode.java]→[Test][encode.test]
     * 加密解密相关→[EncryptUtils.java][encrypt.java]→[Test][encrypt.test]
     * 文件相关→[FileUtils.java][file.java]→[Test][file.test]
     * 图片相关→[ImageUtils.java][image.java]
     * SP相关→[SPUtils.java][sp.java]→[Test][sp.test]
     * 字符串相关→[StringUtils.java][string.java]→[Test][string.test]
     * 时间相关→[TimeUtils.java][time.java]→[Test][time.test]
     * 吐司相关→[ToastUtils.java][toast.java]
     * 尺寸相关→[SizeUtils.java][size.java]
     * 转换相关→[ConvertUtils.java][convert.java]→[Test][convert.test]  dp2px
     */


    private static Random random = new Random(System.currentTimeMillis());


    //设置字体
    public static void setFont(Context context, TextView textView, String fontId) {
        //eg : "fonts/splash.TTF"
        Typeface fontType = Typeface.createFromAsset(context.getAssets(), fontId);
        textView.setTypeface(fontType);
    }

    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     * <p>
     * 如果需要转换成{@link @ColorInt}类型用{@code Color.parseColor()}
     *
     * @return String
     */
    public static String getRandomColor() {
        String r, g, b;
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        //Log.d("getRandomColor", "R = " + r + ",g=" + g + ",b=" + b);
        return "#" + r + g + b;
    }
}

package com.yu.hu.traveling.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.model.BottomBar;
import com.yu.hu.traveling.model.Destination;
import com.yu.hu.traveling.utils.AppConfig;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/15 11:26
 * <p>
 * 自定义底部导航栏
 **/
public class AppBottomBar extends BottomNavigationView {

    private static final String TAG = AppBottomBar.class.getSimpleName();
    //private static int[] sIcons = new int[]{R.drawable.icon_tab_home, R.drawable.icon_tab_friends, R.drawable.icon_tab_publish, R.drawable.icon_tab_message, R.drawable.icon_tab_my};
    private final BottomBar barConfig;

    public AppBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        barConfig = AppConfig.getBottomBarConfig();

        //设置颜色
        setBarItemColor();
        //设置item文字、图标
        setItemInfo();
        //设置item大小
        setItemSize();
        //底部导航栏默认选中项
        setDefaultSelectTab();
    }

    /**
     * 设置默认选中项
     */
    private void setDefaultSelectTab() {
        if (barConfig.selectTab != 0) {
            BottomBar.TabsBean tab = barConfig.tabs.get(barConfig.selectTab);
            if (tab.enable) {
                int itemId = getItemId(tab.pageUrl);
                //这里需要延迟一下 再定位到默认选中的tab
                //因为 咱们需要等待内容区域,也就是NavGraphBuilder解析数据并初始化完成，
                //否则会出现 底部按钮切换过去了，但内容区域还没切换过去
                post(() -> setSelectedItemId(itemId));
            }
        }
    }

    /**
     * 设置item大小
     */
    @SuppressLint("RestrictedApi")
    private void setItemSize() {
        int index = 0;  //记录能显示出的item的个数 也是下一个要显示的item的下标
        for (BottomBar.TabsBean tab : barConfig.tabs) {
            if (!tab.enable) {
                continue;
            }

            int itemId = getItemId(tab.pageUrl);
            if (itemId < 0) {
                continue;
            }

            int iconSize = ConvertUtils.dp2px(tab.size);
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(index);
            itemView.setIconSize(iconSize);

            //item标题为空 即为中间那个按钮
            if (TextUtils.isEmpty(tab.title)) {
                int tintColor = TextUtils.isEmpty(tab.tintColor) ?
                        Utils.getApp().getResources().getColor(R.color.theme_color)
                        : Color.parseColor(tab.tintColor);
                itemView.setIconTintList(ColorStateList.valueOf(tintColor));
                //禁止掉点按时 上下浮动的效果
                itemView.setShifting(false);

                /*
                 * 如果想要禁止掉所有按钮的点击浮动效果。
                 * 那么还需要给选中和未选中的按钮配置一样大小的字号。
                 *
                 *  在MainActivity布局的AppBottomBar标签增加如下配置，
                 *  @style/active，@style/inActive 在style.xml中
                 *  app:itemTextAppearanceActive="@style/active"
                 *  app:itemTextAppearanceInactive="@style/inActive"
                 */
            }
            index++;
        }
    }

    /**
     * 设置item的标题、图标
     */
    private void setItemInfo() {
        List<BottomBar.TabsBean> tabs = barConfig.tabs;
        for (BottomBar.TabsBean tab : tabs) {
            int itemId = getItemId(tab.pageUrl);
            if (!tab.enable) {
                continue;
            }
            if (itemId < 0) {
                continue;
            }
            /*
             *  groupId
             *  itemId 这里对应相应destination的id  这样跳转的时候直接通过menuItem的itemId就可以直接进行跳转了
             *  order 位置
             *  title 标题
             */
            LogUtil.i(TAG, "添加Tab-" + tab.pageUrl);
            MenuItem menuItem = getMenu().add(0, itemId, tab.index, tab.title);
            menuItem.setIcon(AppConfig.getIconRes(tab.pageUrl));
        }
    }

    /**
     * 设置item 选中/未选中的颜色
     */
    private void setBarItemColor() {
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        int[] colors = new int[]{Color.parseColor(barConfig.activeColor),
                Color.parseColor(barConfig.inActiveColor)};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        //LABEL_VISIBILITY_LABELED:设置按钮的文本为一直显示模式
        //LABEL_VISIBILITY_AUTO:当按钮个数小于三个时一直显示，或者当按钮个数大于3个且小于5个时，被选中的那个按钮文本才会显示
        //LABEL_VISIBILITY_SELECTED：只有被选中的那个按钮的文本才会显示
        //LABEL_VISIBILITY_UNLABELED:所有的按钮文本都不显示
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
    }

    private int getItemId(String pageUrl) {
        Destination destination = AppConfig.getDestConfig().get(pageUrl);
        if (destination == null)
            return -1;
        return destination.id;
    }
}

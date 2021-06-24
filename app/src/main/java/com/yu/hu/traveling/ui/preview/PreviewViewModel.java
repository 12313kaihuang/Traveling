package com.yu.hu.traveling.ui.preview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yu.hu.ninegridlayout.entity.GridItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hy
 * created on 2020/04/27 11:33
 **/
@SuppressWarnings("WeakerAccess")
public class PreviewViewModel extends AndroidViewModel {

    private boolean needScrollToTop;
    private List<GridItem> items;

    public PreviewViewModel(@NonNull Application application) {
        super(application);
        items = new ArrayList<>();
        needScrollToTop = false;
    }

    public void setNeedScrollToTop(boolean needScrollToTop) {
        this.needScrollToTop = needScrollToTop;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isNeedScrollToTop() {
        boolean result = needScrollToTop;
        if (needScrollToTop) needScrollToTop = !needScrollToTop;
        return result;
    }

    //当item数量大于1 时需要展示x/max 提示
    public boolean needShowHint() {
        return items.size() > 1;
    }

    public int getItemSize() {
        return items.size();
    }

    public FragmentStateAdapter createAdapter(Fragment fragment) {
        return new FragmentStateAdapter(fragment) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                GridItem item = items.get(position);
                return SingleMediaPreviewFragment.newInstance(item);
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        };
    }

    public void setItems(List<GridItem> items) {
        this.items = items;
    }
}

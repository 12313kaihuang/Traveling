package com.yu.hu.traveling.ui.companion;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.Utils;

/**
 * @author Hy
 * created on 2020/04/24 16:10
 **/
public class CompanionViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private String tag;


    @SuppressWarnings("WeakerAccess")
    public CompanionViewModelFactory(String tag) {
        super(Utils.getApp());
        this.tag = tag;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CompanionViewModel.class)) {
            //noinspection unchecked
            return (T) new CompanionViewModel(Utils.getApp(), tag);
        }
        throw new RuntimeException("unknown class :" + modelClass.getName());
    }
}

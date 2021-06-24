package com.yu.hu.traveling.ui.home;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.Utils;

/**
 * @author Hy
 * created on 2020/04/18 13:38
 * <p>
 * {@code new ViewModelProvider(activity,new NoteViewModelFactory(application,tag)).get(HomeViewModel.class);}
 **/
public class NoteViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private String tag;

    /**
     * Creates a {@code AndroidViewModelFactory}
     */
    @SuppressWarnings("WeakerAccess")
    public NoteViewModelFactory(String tag) {
        super(Utils.getApp());
        this.tag = tag;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NoteViewModel.class)) {
            return (T) new NoteViewModel(Utils.getApp(), tag);
        } else if (modelClass.isAssignableFrom(FocusedNoteViewModel.class)) {
            return (T) new FocusedNoteViewModel(Utils.getApp(), tag);
        }
        throw new RuntimeException("unknown class :" + modelClass.getName());
    }
}

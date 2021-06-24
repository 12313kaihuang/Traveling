package com.yu.hu.traveling.ui.detail;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.Utils;
import com.yu.hu.traveling.model.Comment;

/**
 * @author Hy
 * created on 2020/04/30 19:32
 **/
public class DetailViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private int noteId;

    private int type;


    @SuppressWarnings("WeakerAccess")
    public DetailViewModelFactory(int noteId) {
        this(noteId, Comment.TYPE_NOTE);
    }

    @SuppressWarnings("WeakerAccess")
    public DetailViewModelFactory(int noteId, int type) {
        super(Utils.getApp());
        this.noteId = noteId;
        this.type = type;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CommentViewModel.class)) {
            //noinspection unchecked
            return (T) new CommentViewModel(Utils.getApp(), noteId, type);
        }
        throw new RuntimeException("unknown class :" + modelClass.getName());
    }
}

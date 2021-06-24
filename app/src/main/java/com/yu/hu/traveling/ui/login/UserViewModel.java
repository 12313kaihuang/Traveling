package com.yu.hu.traveling.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yu.hu.traveling.model.User;

/**
 * @author Hy
 * created on 2020/04/16 11:16
 **/
@SuppressWarnings("unused")
public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<User> user;

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<User> getUser() {
        return user;
    }
}

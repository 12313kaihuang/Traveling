package com.yu.hu.common.cache;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * @author Hy
 * created on 2020/04/15 17:21
 **/
@SuppressWarnings("WeakerAccess")
@Keep
@Entity
public class Cache implements Parcelable {

    @PrimaryKey
    @NonNull
    @SerializedName("key")
    public String key;

    @SerializedName("value")
    public String value;

    public Cache() {
    }

    public Cache(Parcel in) {
        //noinspection ConstantConditions
        key = in.readString();
        value = in.readString();
    }

    public static final Creator<Cache> CREATOR = new Creator<Cache>() {
        @Override
        public Cache createFromParcel(Parcel in) {
            return new Cache(in);
        }

        @Override
        public Cache[] newArray(int size) {
            return new Cache[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }
}

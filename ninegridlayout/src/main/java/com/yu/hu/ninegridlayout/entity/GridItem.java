package com.yu.hu.ninegridlayout.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.IntDef;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

/**
 * @author Hy
 * created on 2020/04/17 20:52
 * <p>
 * 对应一个item
 * <p>
 * 不是所有的参数都需要自行构建，不同类型的item只需要几个特定的必要参数即可
 * 必要参数:
 * 1. 图片{@link #GridItem(String)} - cover  (width height 如果只有一张图片需要展示必须传，其他位置可以不传，不过建议都传进来)
 * 2. 视频{@link #GridItem(String, String)} - cover url  （width height 同图片）
 * 3. 提供从json字符串快速构建item的方法{@link #fromJson(String)}
 **/
@SuppressWarnings({"unused", "WeakerAccess"})
@Keep
public class GridItem implements Parcelable {

    public static final int TYPE_IMG = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_ADD_IMG = 2;

    @IntDef({TYPE_IMG, TYPE_VIDEO, TYPE_ADD_IMG})
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface ItemType {
    }


    public int width;  //宽
    public int height; //高

    @ItemType
    public int type;
    public String url;   //对应视频url
    public String cover; //图片url 或者视频的封面图片url

    //9张之后还差多少张的数量 可以不用自己设置，再submitList时会自动计算
    public int moreCount;

    public int index;  //用户排序-后台
    public String coverPath; //拍照后图片本地路径 或者录视频生成的封面本地路径
    public String urlPath;   //录制视频本地路径
    private Uri imageUri; //用于存储本地相册选择图片后传回的uri

    public GridItem(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    private static volatile Gson sGson;

    public GridItem() {
    }

    public static GridItem localItem(String coverPath, String urlPath) {
        GridItem item = new GridItem();
        item.type = TextUtils.isEmpty(urlPath) ? TYPE_IMG : TYPE_VIDEO;
        item.coverPath = coverPath;
        item.urlPath = urlPath;
        return item;
    }

    public static GridItem localItem(String coverPath) {
        return localItem(coverPath, null);
    }

    public GridItem(int type, String cover, String url, int moreCount) {
        this.type = type;
        this.url = url;
        this.cover = cover;
        this.moreCount = moreCount;
    }

    public GridItem(int type) {
        this.type = type;
    }

    protected GridItem(Parcel in) {
        width = in.readInt();
        height = in.readInt();
        type = in.readInt();
        url = in.readString();
        cover = in.readString();
        moreCount = in.readInt();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Nullable
    public static GridItem fromJson(String json) {
        if (sGson == null) {
            synchronized (GridItem.class) {
                if (sGson == null) {
                    sGson = new Gson();
                }
            }
        }
        return sGson.fromJson(json, GridItem.class);
    }

    @SuppressWarnings("UnusedReturnValue")
    public GridItem setMoreCount(int moreCount) {
        this.moreCount = moreCount;
        return this;
    }

    public GridItem setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public boolean showVideoIcon() {
        return (url != null || !TextUtils.isEmpty(urlPath)) && moreCount == 0;
    }

    public String getMoreCountString() {
        return "+" + moreCount;
    }

    public static final Creator<GridItem> CREATOR = new Creator<GridItem>() {
        @Override
        public GridItem createFromParcel(Parcel in) {
            return new GridItem(in);
        }

        @Override
        public GridItem[] newArray(int size) {
            return new GridItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(type);
        dest.writeString(url);
        dest.writeString(cover);
        dest.writeInt(moreCount);
        dest.writeParcelable(imageUri, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridItem item = (GridItem) o;
        return width == item.width &&
                height == item.height &&
                type == item.type &&
                moreCount == item.moreCount &&
                Objects.equals(url, item.url) &&
                Objects.equals(cover, item.cover) &&
                Objects.equals(coverPath, item.coverPath) &&
                Objects.equals(urlPath, item.urlPath) &&
                Objects.equals(imageUri, item.imageUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, type, url, cover, moreCount, coverPath, urlPath, imageUri);
    }
}

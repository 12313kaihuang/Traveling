package com.yu.hu.traveling.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.yu.hu.common.utils.ResourceUtil;
import com.yu.hu.libnetwork.ApiResponse;

import java.util.Objects;

/**
 * @author Hy
 * created on 2020/04/16 10:56
 * <p>
 * 用户对象
 **/
@SuppressWarnings({"unused", "WeakerAccess"})
//@Entity
public class User implements Parcelable {

    public static final String CACHE_KEY = "cache_key_user";

    /**
     * id : 1250
     * userId : 1578919786
     * name : 、蓅哖╰伊人为谁笑
     * avatar : http://qzapp.qlogo.cn/qzapp/101794421/FE41683AD4ECF91B7736CA9DB8104A5C/100
     * description : 这是一只神秘的jetpack
     * likeCount : 7
     * topCommentCount : 0
     * followCount : 3
     * followerCount : 57
     * qqOpenId : FE41683AD4ECF91B7736CA9DB8104A5C
     * expires_time : 1586695789903
     * score : 0
     * historyCount : 4532
     * commentCount : 42
     * favoriteCount : 3
     * feedCount : 0
     * hasFollow : false
     */

    public int id;  //应该是自增id

    //@PrimaryKey
    //public long userId; //id
    public String name; //昵称
    public String avatar;  //头像
    public String description;
    public int likeCount; //获赞
    public int topCommentCount;
    public int followCount; //关注
    public int followerCount; //粉丝
    public String qqOpenId;  //qqOpenId
    public long expires_time;  //过期时间
    public int score; //积分
    public int historyCount; //历史观看记录
    public int commentCount; //评论数
    public int favoriteCount; //收藏数
    public int noteCount;  //帖子数
    public boolean hasFollow;  //是否关注

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        avatar = in.readString();
        description = in.readString();
        likeCount = in.readInt();
        topCommentCount = in.readInt();
        followCount = in.readInt();
        followerCount = in.readInt();
        qqOpenId = in.readString();
        expires_time = in.readLong();
        score = in.readInt();
        historyCount = in.readInt();
        commentCount = in.readInt();
        favoriteCount = in.readInt();
        noteCount = in.readInt();
        hasFollow = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static User getDefaultUserInfo() {
        ApiResponse<User> response = ResourceUtil.getFromAssets("default_user_info.json", new TypeToken<ApiResponse<User>>() {
        }.getType());
        //noinspection ConstantConditions
        return response.data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                likeCount == user.likeCount &&
                topCommentCount == user.topCommentCount &&
                followCount == user.followCount &&
                followerCount == user.followerCount &&
                expires_time == user.expires_time &&
                score == user.score &&
                historyCount == user.historyCount &&
                commentCount == user.commentCount &&
                favoriteCount == user.favoriteCount &&
                noteCount == user.noteCount &&
                hasFollow == user.hasFollow &&
                Objects.equals(name, user.name) &&
                Objects.equals(avatar, user.avatar) &&
                Objects.equals(description, user.description) &&
                Objects.equals(qqOpenId, user.qqOpenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, avatar, description, likeCount, topCommentCount, followCount, followerCount, qqOpenId, expires_time, score, historyCount, commentCount, favoriteCount, noteCount, hasFollow);
    }

    @NonNull
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(description);
        dest.writeInt(likeCount);
        dest.writeInt(topCommentCount);
        dest.writeInt(followCount);
        dest.writeInt(followerCount);
        dest.writeString(qqOpenId);
        dest.writeLong(expires_time);
        dest.writeInt(score);
        dest.writeInt(historyCount);
        dest.writeInt(commentCount);
        dest.writeInt(favoriteCount);
        dest.writeInt(noteCount);
        dest.writeByte((byte) (hasFollow ? 1 : 0));
    }
}

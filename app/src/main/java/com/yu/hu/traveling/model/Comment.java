package com.yu.hu.traveling.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.blankj.utilcode.util.Utils;
import com.yu.hu.emoji.BR;
import com.yu.hu.traveling.R;

import java.util.Objects;

/**
 * @author Hy
 * created on 2020/04/16 19:16
 * <p>
 * 对应一个评论
 **/
@SuppressWarnings({"unused", "WeakerAccess"})
public class Comment extends BaseObservable implements Parcelable {

    public static final int TYPE_NOTE = 1;  //游记或攻略的评论
    public static final int TYPE_COMPANION = 2; //结伴的评论
    public static final int TYPE_NOTE_COMMENT = 3;//游记或攻略的评论的回复
    public static final int TYPE_COMPANION_COMMENT = 4;//结伴的评论的回复

    /**
     * id : 1126
     * itemId : 1578976510452
     * commentId : 1579007787804000
     * userId : 1578919786
     * commentType : 1
     * createTime : 1579007787804
     * commentCount : 0
     * likeCount : 1001
     * commentText : 2020他来了，就在眼前了~Happy New Year
     * imageUrl :
     * videoUrl :
     * width : 0
     * height : 0
     * hasLiked : false
     * author : {"id":1250,"userId":1578919786,"name":"、蓅哖╰伊人为谁笑","avatar":"http://qzapp.qlogo.cn/qzapp/101794421/FE41683AD4ECF91B7736CA9DB8104A5C/100","description":"这是一只神秘的jetpack","likeCount":7,"topCommentCount":0,"followCount":3,"followerCount":57,"qqOpenId":"FE41683AD4ECF91B7736CA9DB8104A5C","expires_time":1586695789903,"score":0,"historyCount":4532,"commentCount":42,"favoriteCount":3,"feedCount":0,"hasFollow":false}
     * ugc : {"likeCount":120,"shareCount":10,"commentCount":10,"hasFavorite":false,"hasLiked":false,"hasdiss":false,"hasDissed":false}
     */

    public int id; //评论id
    public int itemId;  //对应文章/结伴/评论的id
    public int userId;
    public int commentType;
    public long createTime;
    public int commentCount; //回复数
    public int likeCount;
    public int replyId; //回复人的id
    @Bindable
    public int replyCount; //回复数
    public String replyName; //回复人的名称
    public String commentText;
    public String imageUrl;
    public String videoUrl;
    public int width;
    public int height;
    public boolean hasLiked;
    public User author;
    public Ugc ugc;

    private String replyString;

    protected Comment(Parcel in) {
        id = in.readInt();
        itemId = in.readInt();
        userId = in.readInt();
        commentType = in.readInt();
        createTime = in.readLong();
        commentCount = in.readInt();
        likeCount = in.readInt();
        replyId = in.readInt();
        replyCount = in.readInt();
        replyName = in.readString();
        commentText = in.readString();
        imageUrl = in.readString();
        videoUrl = in.readString();
        width = in.readInt();
        height = in.readInt();
        hasLiked = in.readByte() != 0;
        author = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(itemId);
        dest.writeInt(userId);
        dest.writeInt(commentType);
        dest.writeLong(createTime);
        dest.writeInt(commentCount);
        dest.writeInt(likeCount);
        dest.writeInt(replyId);
        dest.writeInt(replyCount);
        dest.writeString(replyName);
        dest.writeString(commentText);
        dest.writeString(imageUrl);
        dest.writeString(videoUrl);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeByte((byte) (hasLiked ? 1 : 0));
        dest.writeParcelable(author, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Bindable
    public Ugc getUgc() {
        if (ugc == null) {
            ugc = new Ugc();
        }
        return ugc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id &&
                itemId == comment.itemId &&
                userId == comment.userId &&
                commentType == comment.commentType &&
                createTime == comment.createTime &&
                commentCount == comment.commentCount &&
                likeCount == comment.likeCount &&
                replyId == comment.replyId &&
                replyCount == comment.replyCount &&
                width == comment.width &&
                height == comment.height &&
                hasLiked == comment.hasLiked &&
                Objects.equals(replyName, comment.replyName) &&
                Objects.equals(commentText, comment.commentText) &&
                Objects.equals(imageUrl, comment.imageUrl) &&
                Objects.equals(videoUrl, comment.videoUrl) &&
                Objects.equals(author, comment.author) &&
                Objects.equals(ugc, comment.ugc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, userId, commentType, createTime, commentCount, likeCount, replyId, replyCount, replyName, commentText, imageUrl, videoUrl, width, height, hasLiked, author, ugc);
    }

    public static String transformReplyCount(int replyCount){
        return Utils.getApp().getResources().getString(R.string.reply_count, replyCount);
    }

    public static String transformCommentText(Comment comment) {
        if (TextUtils.isEmpty(comment.replyName)) {
            return comment.commentText;
        }
        return Utils.getApp().getResources().getString(R.string.comment_reply, comment.replyName, comment.commentText);
    }

    public void addReplyCount() {
        replyCount++;
        notifyPropertyChanged(BR._all);
    }
}

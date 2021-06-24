package com.yu.hu.traveling.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.blankj.utilcode.util.CloneUtils;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.traveling.db.repository.CompanionRepository;

import java.util.Objects;

/**
 * @author Hy
 * created on 2020/04/23 23:24
 * <p>
 * 结伴
 **/
@Entity
public class Companion implements Parcelable {

    @PrimaryKey
    public int companionId;
    public long createTime;
    public int authorId;
    public String title;
    public String content; //文本内容
    public String targetLocation; //预期目的地
    public long targetTime; //预期出发时间
    public User author;
    public Ugc ugc;

    public Companion() {
    }

    public Companion deepClone() {
        return CloneUtils.deepClone(this, Companion.class);
    }

    public void update() {
        CompanionRepository.getInstance().insert(this);
    }

    @Ignore
    protected Companion(Parcel in) {
        companionId = in.readInt();
        createTime = in.readLong();
        title = in.readString();
        content = in.readString();
        targetLocation = in.readString();
        targetTime = in.readLong();
    }

    public static final Creator<Companion> CREATOR = new Creator<Companion>() {
        @Override
        public Companion createFromParcel(Parcel in) {
            return new Companion(in);
        }

        @Override
        public Companion[] newArray(int size) {
            return new Companion[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Companion companion = (Companion) o;
        return companionId == companion.companionId &&
                createTime == companion.createTime &&
                targetTime == companion.targetTime &&
                Objects.equals(title, companion.title) &&
                Objects.equals(content, companion.content) &&
                Objects.equals(targetLocation, companion.targetLocation) &&
                Objects.equals(author, companion.author) &&
                Objects.equals(ugc, companion.ugc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companionId, createTime, title, content, targetLocation, targetTime, author, ugc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(companionId);
        dest.writeLong(createTime);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(targetLocation);
        dest.writeLong(targetTime);
    }

    public Companion toggleLike(boolean hasLiked) {
        if (this.ugc.hasLiked == hasLiked) {
            return null;
        }
        if (ugc.hasLiked) {
            //取消喜欢
            ugc.likeCount--;
        } else {
            //喜欢
            ugc.likeCount++;
        }
        ugc.hasLiked = !ugc.hasLiked;
        LogUtil.d("存入数据库 hasLiked =" + ugc.hasLiked + ", likeCount = " + ugc.likeCount);
        return this;
    }

    public Companion toggleFocus(Boolean focus) {
        if (focus == author.hasFollow) {
            return this;
        }
        author.hasFollow = !author.hasFollow;
        return this;
    }
}

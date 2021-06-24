package com.yu.hu.traveling.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.blankj.utilcode.util.CloneUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.db.repository.NoteRepository;
import com.yu.hu.traveling.ui.detail.AbsNoteDetailFragment;

import java.util.List;
import java.util.Objects;

/**
 * @author Hy
 * created on 2020/04/16 19:21
 * <p>
 * 对应一篇文章
 **/
@Entity
public class Note implements Parcelable {

    public static final int TYPE_NOTE = 1;  //游记
    public static final int TYPE_STRATEGY = 2; //攻略

    /**
     * noteId : 428
     * itemType : noteType
     * createTime : 1578977844500
     * duration : 8
     * title
     * content : 2020他来了，就在眼前了
     * authorId : 1578919786
     * activityIcon : null
     * activityText : 攻略 --文章类型 攻略 还是游记
     * imgs : []
     */

    @PrimaryKey
    public int noteId;
    public int noteType; //用于区分是游记还是攻略
    public long createTime;
    public int duration;
    public String title;
    public String content; //文本内容
    public int authorId;
    public String activityIcon;
    public String activityText;

    @Ignore
    @SerializedName("imgs")
    private List<GridItem> imgItems;

    //用于将图片列表转换成String类型存入数据库
    public String imgsString;

    public User author;
    public Comment topComment;
    public Ugc ugc;

    public Note() {
    }

    protected Note(Parcel in) {
        noteId = in.readInt();
        noteType = in.readInt();
        createTime = in.readLong();
        duration = in.readInt();
        title = in.readString();
        content = in.readString();
        authorId = in.readInt();
        activityIcon = in.readString();
        activityText = in.readString();
    }

    //存入数据库/读取数据库时 图片列表的转换
    public void transform(boolean isSave) {
        if (isSave && imgItems != null) {
            //存入数据库之前
            this.imgsString = GsonUtils.toJson(imgItems);
            return;
        }

        if (!isSave && !TextUtils.isEmpty(imgsString)) {
            //从数据库取出
            this.imgItems = GsonUtils.fromJson(imgsString, new TypeToken<List<GridItem>>() {
            }.getType());
        }
    }

    public List<GridItem> getImgItems() {
        if (imgItems == null) {
            transform(false);
        }
        return imgItems;
    }

    public Note deepClone() {
        return CloneUtils.deepClone(this, Note.class);
    }

    public Note toggleLike(boolean hasLiked) {
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
        if (ugc.hasDissed && !ugc.hasLiked) {
            //当前踩了且没有喜欢状态  需要取消diss
            toggleDiss(false);
        }
        ugc.hasLiked = !ugc.hasLiked;
        LogUtil.d("存入数据库 hasLiked =" + ugc.hasLiked + ", likeCount = " + ugc.likeCount);
        return this;
    }

    //diss 与 like是互斥的  所以得加一个判断
    public Note toggleDiss(boolean hasDiss) {
        if (this.ugc.hasDissed == hasDiss) {
            return null;
        }
        if (ugc.hasLiked && !ugc.hasDissed) {
            //当前是喜欢了且没有踩状态 需要取消喜欢状态
            toggleLike(false);
        }
        ugc.hasDissed = !ugc.hasDissed;
        return this;

    }

    @SuppressWarnings("unused")
    public Note share() {
        ugc.shareCount++;
        return this;
    }

    @SuppressWarnings("unused")
    public Note toggleCollect() {
        ugc.hasFavorite = !ugc.hasFavorite;
        return this;
    }

    public Note toggleFocus(Boolean focus) {
        if (focus == author.hasFollow) {
            return this;
        }
        author.hasFollow = !author.hasFollow;
        return this;
    }

    public void update() {
        NoteRepository.getInstance().insert(this);
    }

    /**
     * 根据图片个数即种类分了三类
     *
     * @return 详情页的展示类别
     * @see AbsNoteDetailFragment#SHOW_TYPE_MULTI
     */
    public int getDetailShowType() {
        if (imgItems == null || imgItems.size() == 0) {
            return AbsNoteDetailFragment.SHOW_TYPE_SINGLE_IMG;
        }
        if (imgItems.size() == 1) {
            GridItem gridItem = imgItems.get(0);
            if (gridItem == null || TextUtils.isEmpty(gridItem.cover)) {
                return AbsNoteDetailFragment.SHOW_TYPE_SINGLE_IMG;
            }
            if (TextUtils.isEmpty(gridItem.url)) {
                return AbsNoteDetailFragment.SHOW_TYPE_SINGLE_IMG;
            } else {
                return AbsNoteDetailFragment.SHOW_TYPE_SINGLE_VIDEO;
            }
        } else {
            //多图模式
            return AbsNoteDetailFragment.SHOW_TYPE_MULTI;
        }
    }

    @SuppressWarnings("unused")
    public void setImgItems(List<GridItem> imgItems) {
        this.imgItems = imgItems;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return noteId == note.noteId &&
                noteType == note.noteType &&
                createTime == note.createTime &&
                duration == note.duration &&
                authorId == note.authorId &&
                Objects.equals(title, note.title) &&
                Objects.equals(content, note.content) &&
                Objects.equals(activityIcon, note.activityIcon) &&
                Objects.equals(activityText, note.activityText) &&
                //Objects.equals(imgItems, note.imgItems) &&
                Objects.equals(author, note.author) &&
                Objects.equals(topComment, note.topComment) &&
                Objects.equals(ugc, note.ugc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, noteType, createTime, duration, title, content, authorId, activityIcon, activityText, imgItems, author, topComment, ugc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(noteId);
        dest.writeInt(noteType);
        dest.writeLong(createTime);
        dest.writeInt(duration);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeInt(authorId);
        dest.writeString(activityIcon);
        dest.writeString(activityText);
    }
}

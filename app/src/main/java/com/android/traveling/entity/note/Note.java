package com.android.traveling.entity.note;

import com.android.traveling.util.DateUtil;
import com.android.traveling.util.StaticClass;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HY
 * 2019/1/20 16:05
 * <p>
 * 游记类
 */
@SuppressWarnings("unused")
public class Note implements Serializable{

    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Integer tag = 1;

    private String imgs;

    private String createTime;

    private Integer likeNum;

    private Integer commentNum;

    private ReleasePeople releasePeople;

    public Note() {

    }

    public Note(NoteList noteList) {
        this.id = noteList.getId();
        this.userId = noteList.getUserId();
        this.title = noteList.getTitle();
        this.content = noteList.getContent();
        this.createTime = DateUtil.transform(noteList.getCreateTime());
        this.imgs = noteList.getImgs();
        this.tag = noteList.getTag();
        this.likeNum = noteList.getLikeNum();
        this.commentNum = noteList.getCommentNum();
        this.releasePeople = new ReleasePeople(noteList);
    }

    /**
     * 视图数据转换成对象
     *
     * @param noteLists List<NoteList>
     * @return List<Note>
     */
    public static List<Note> transform(List<NoteList> noteLists) {
        List<Note> notes = new ArrayList<>();
        for (NoteList noteList : noteLists) {
            notes.add(new Note(noteList));
        }
        return notes;
    }

    public ReleasePeople getReleasePeople() {
        return releasePeople;
    }

    public void setReleasePeople(ReleasePeople releasePeople) {
        this.releasePeople = releasePeople;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getImgList() {
        return parseImages(imgs);
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 将字符串形式imgs转换成List<String>形式
     *
     * @return List<String>
     */
    private List<String> parseImages(String imgs) {
        List<String> imgList = new ArrayList<>();
        if (imgs != null) {
            try {
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonElements = jsonParser.parse(imgs).getAsJsonArray();
                for (JsonElement element : jsonElements) {
                    imgList.add(StaticClass.IMG_URL + gson.fromJson(element, String.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return imgList;
    }
}

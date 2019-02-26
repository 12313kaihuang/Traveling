package com.android.traveling.entity.note;

import android.support.annotation.NonNull;

import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.BinarySearch;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by HY
 * 2019/1/20 16:05
 * <p>
 * 游记类
 */
@SuppressWarnings("unused")
public class Note implements Serializable {

    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Integer tag = 1;

    private String imgs;

    private String createTime;

    private Integer likeNum;

    private String likeList;

    private Integer commentNum;

    private ReleasePeople releasePeople;

    public Note() {

    }

    private Note(NoteList noteList) {
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

    public String getStrLikeList() {
        return likeList;
    }

    public void setLikeList(String list) {
        this.likeList = list;
    }

    private ArrayList<Integer> getLikeList() {
        return parseLikeList(likeList);
    }

    private void setLikeList(ArrayList<Integer> likeList) {
        this.likeList = new Gson().toJson(likeList);
    }

    /**
     * 图片集
     *
     * @return 图片集
     */
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

    /**
     * 将字符串转换成ArrayList
     *
     * @param likeList likeList
     * @return ArrayList
     */
    private static ArrayList<Integer> parseLikeList(String likeList) {
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        return new Gson().fromJson(likeList, type);
    }

    private ArrayList<Integer> mlikeList;

    /**
     * 判断某个用户是否已经喜欢了这篇文章
     *
     * @param userId userId
     * @return 结果
     */
    private boolean isLiked(Integer userId) {
        if (mlikeList == null) {
            mlikeList = getLikeList();
        }
        return mlikeList.size() != 0 && BinarySearch.binarySearch(mlikeList, userId) >= 0;
    }

    /**
     * 判断当前用户是否喜欢了这篇文章
     *
     * @return 返回结果
     */
    public boolean isLiked() {
        User currentUser = TravelingUser.getCurrentUser();
        return currentUser != null && isLiked(currentUser.getUserId());
    }


    /**
     * 点赞/取消点赞
     *
     * @param ilike 回调接口
     */
    public void doLike(Ilike ilike) {
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser == null) {
            ilike.onFailure("未登录");
            return;
        }
        if (isLiked(currentUser.getUserId())) {  //点过赞了
            //            ilike.onFailure("点过赞了");
            ilike.onSuccess();
            return;
        }
        try {
            ArrayList<Integer> likeList = getLikeList();
            likeList.add(currentUser.getUserId());
            Collections.sort(likeList);  //默认为升序排序

            //创建Retrofit对象  注意url后面有一个'/'。
            Retrofit retrofit = UtilTools.getRetrofit();
            // 获取NoteService对象
            NoteService noteService = retrofit.create(NoteService.class);
            Call<Msg> msgCall = noteService.updateLikeNum(id, new Gson().toJson(likeList));
            msgCall.enqueue(new Callback<Msg>() {
                @Override
                public void onResponse(@NonNull Call<Msg> call, @NonNull Response<Msg> response) {
                    Msg msg = response.body();
                    if (msg == null) {
                        ilike.onFailure("msg == null");
                    } else {
                        if (msg.getStatus() == Msg.correctStatus) {
                            setLikeList(likeList);
                            ilike.onSuccess();
                        } else {
                            ilike.onFailure(msg.getInfo());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Msg> call, @NonNull Throwable t) {
                    likeList.remove(currentUser.getUserId());
                    ilike.onFailure("onFailure :" + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Ilike {

        void onFailure(String reason);

        void onSuccess();
    }
}

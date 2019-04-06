package com.android.traveling.entity.note;

import android.support.annotation.NonNull;

import com.android.traveling.entity.comment.Comment;
import com.android.traveling.entity.service.CommentService;
import com.android.traveling.entity.msg.CommentMsg;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.msg.NoteMsg;
import com.android.traveling.entity.service.NoteService;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.BinarySearch;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.LogUtil;
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

    public static final int LIKE = 0;
    public static final int DISLIKE = 1;

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
        this.likeNum = parseLikeList(likeList).size();
    }

    private ArrayList<Integer> getLikeList() {
        return parseLikeList(likeList);
    }

    private void setLikeList(ArrayList<Integer> likeList) {
        this.likeNum = likeList.size();
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


    /**
     * 判断某个用户是否已经喜欢了这篇文章
     *
     * @param userId userId
     * @return 结果
     */
    private boolean isLiked(Integer userId) {
        ArrayList<Integer> likeList = getLikeList();
        return likeList.size() != 0 && BinarySearch.binarySearch(likeList, userId) >= 0;
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
     * @param type 点赞/取消点赞
     */
    public void doLike(int type) {
        String TAG = "Note.doLike";
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser == null) {
            LogUtil.d(TAG, "currentUser == null");
            return;
        }
        ArrayList<Integer> likeList = getLikeList();
        if (type == LIKE) {
            if (isLiked()) {
                LogUtil.d(TAG, "点过赞了");
                return;
            }
            likeList.add(currentUser.getUserId());
            Collections.sort(likeList);  //默认为升序排序
            LogUtil.d(TAG, "点赞：list=" + new Gson().toJson(likeList));
        } else if (type == DISLIKE) {
            likeList.remove(
                    BinarySearch.binarySearch(likeList, currentUser.getUserId()));
            LogUtil.d(TAG, "取消点赞：list=" + new Gson().toJson(likeList));
        } else {
            LogUtil.d(TAG, "type不正确");
            return;
        }
        this.likeNum = likeList.size();
        this.likeList = new Gson().toJson(likeList);
        LogUtil.d(TAG, "");
        //创建Retrofit对象  注意url后面有一个'/'。
        NoteService noteService = getNoteService();
        Call<Msg> msgCall = noteService.updateLikeNum(id, new Gson().toJson(likeList));
        msgCall.enqueue(new retrofit2.Callback<Msg>() {
            @Override
            public void onResponse(@NonNull Call<Msg> call, @NonNull Response<Msg> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Msg> call, @NonNull Throwable t) {

            }
        });
    }

    /**
     * 获取最新的游记文章
     *
     * @param callback 回调接口
     */
    public static void getNewest(Callback callback) {
        Call<NoteMsg> call = getNoteService().getNewest();
        sendRequest(callback, call);
    }


    /**
     * 获取关注的人的最新的文章
     *
     * @param userId   userId
     * @param callback 回调接口
     */
    public static void getFocusedNewest(int userId, Callback callback) {
        Call<NoteMsg> call = getNoteService().getFocusedNewest(userId);
        sendRequest(callback, call);
    }

    /**
     * 加载更多游记文章
     *
     * @param lastId   当前所显示的最旧的一篇文章的id
     * @param callback 回调接口
     */
    public static void loadMore(int lastId, Callback callback) {
        Call<NoteMsg> call = getNoteService().loadMore(lastId);
        sendRequest(callback, call);
    }

    /**
     * 加载更多游记文章（关注的人的）
     *
     * @param userId   userId
     * @param lastId   当前所显示的最旧的一篇文章的id
     * @param callback 回调接口
     */
    public static void loadMoreFocused(int userId, int lastId, Callback callback) {
        Call<NoteMsg> call = getNoteService().loadMoreFocused(userId,lastId);
        sendRequest(callback, call);
    }


    /**
     * 模糊查询最新的标题含有 content 的10篇文章
     *
     * @param content  content
     * @param callback 回调接口
     */
    public static void searchHazily(String content, Callback callback) {
        Call<NoteMsg> call = getNoteService().searchNoteHazily(content);
        sendRequest(callback, call);
    }


    /**
     * 模糊查询最新的标题含有 content 的  更多的10篇文章
     *
     * @param content  content
     * @param noteId   当前所显示的最旧的一篇文章的id
     * @param callback 回调接口
     */
    public static void searchMoreHazily(String content, int noteId, Callback callback) {
        Call<NoteMsg> call = getNoteService().searchMoreNoteHazily(content, noteId);
        sendRequest(callback, call);
    }

    /**
     * 加载评论
     *
     * @param callback callback
     */
    public void loadComments(Callback2 callback) {
        Call<CommentMsg> msgCall = UtilTools.getRetrofit().create(CommentService.class).getComments(id);
        msgCall.enqueue(new retrofit2.Callback<CommentMsg>() {
            @Override
            public void onResponse(@NonNull Call<CommentMsg> call, @NonNull Response<CommentMsg> response) {
                CommentMsg msg = response.body();
                if (msg != null) {
                    if (msg.getComments() == null || msg.getComments().size() == 0) {
                        callback.onFailure(msg.getInfo());
                    } else {
                        commentNum = msg.getComments().size();
                        callback.onSuccess(msg.getComments());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentMsg> call, @NonNull Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
            }
        });
    }

    /**
     * 发送retrofit请求
     *
     * @param callback 回调接口
     * @param call     call
     */
    private static void sendRequest(Callback callback, Call<NoteMsg> call) {
        call.enqueue(new retrofit2.Callback<NoteMsg>() {
            @Override
            public void onResponse(@NonNull Call<NoteMsg> call, @NonNull Response<NoteMsg> response) {
                NoteMsg msg = response.body();
                if (msg == null) {
                    callback.onFailure("msg == null");
                } else {
                    if (msg.getStatus() == Msg.CORRECT_STATUS) {
                        callback.onSuccess(msg.getNotes());
                    } else {
                        callback.onFailure(msg.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NoteMsg> call, @NonNull Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
            }
        });
    }

    @NonNull
    private static NoteService getNoteService() {
        Retrofit retrofit = UtilTools.getRetrofit();
        return retrofit.create(NoteService.class);
    }


    /**
     * 网络请求回调接口
     */
    public interface Callback {
        void onSuccess(List<Note> noteList);

        void onFailure(String reason);
    }

    /**
     * 网络请求回调接口
     */
    public interface Callback2 {
        void onSuccess(List<Comment> commentList);

        void onFailure(String reason);
    }
}

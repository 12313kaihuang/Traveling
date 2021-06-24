package cn.leancloud.chatkit.utils;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationsQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;

import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

/**
 * @author Hy
 * created on 2020/04/26 17:19
 * <p>
 * 封装一些工具类供外部使用
 **/
public class ChatKitUtils {

    private static final String TAG = "ChatKitUtils";

    /**
     * 登录
     *
     * @param userId userId
     */
    public static void login(String userId, final LoginCallback callback) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        LCChatKit.getInstance().open(userId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    Log.d(TAG, "login: LeanClound登录成功");
                    callback.onLoginFailure();
                } else {
                    Log.d(TAG, "LeanClound登录失败");
                    callback.onLoginFailure();
                }
            }
        });
    }

    /**
     * 退出登录
     */
    public static void logout() {
        LCChatKit.getInstance().close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null != e) {
                    e.printStackTrace();
                    Log.d(TAG, "===================LeanCloud退出登录失败===================");
                }
                Log.d(TAG, "===================LeanCloud退出登录===================");
            }
        });
    }

    /**
     * 查询回话列表信息
     */
    public static void setConversationList(String userId, final LCIMConversationListFragment fragment) {
        AVIMClient.getInstance(userId)
                .getConversationsQuery()
                .findInBackground(new AVIMConversationQueryCallback() {
                    @Override
                    public void done(List<AVIMConversation> convs, AVIMException e) {
                        if (e == null) {
                            Log.d(TAG, "setConversationList size=" + convs.size());
                            //convs就是获取到的conversation列表
                            //注意：按每个对话的最后更新日期（收到最后一条消息的时间）倒序排列
                            fragment.setDataList(convs);
                        }
                    }
                });
    }

    public interface LoginCallback {
        void onLoginSuccess();

        void onLoginFailure();
    }
}

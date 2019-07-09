package com.yu.hu.traveling.yonglian.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yu.hu.traveling.R;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.plugin.common.AppMgr;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.UserData;
import com.yuntongxun.plugin.im.dao.bean.RXConversation;
import com.yuntongxun.plugin.im.manager.port.OnBindViewHolderListener;
import com.yuntongxun.plugin.im.ui.chatting.model.BaseChattingRow;
import com.yuntongxun.plugin.im.ui.conversation.ConversationAdapter;


/**
 * Created with Android Studio IDEA.
 * <p>
 * This is the implementation of custom UI
 *
 * @author WJ
 * @version 1.0
 * @since 2017/8/21 9:53
 */

public class OnBindViewHolderListenerImpl implements OnBindViewHolderListener {
    private static final String TAG = LogUtil.getLogUtilsTag(OnBindViewHolderListenerImpl.class);

    /**
     * 处理UI刷新的时候每个消息显示区域的View的回调事件，如果应用层需要对当前的消息UI进行定制
     * 1、处理消息气泡的显示 （通过row.getChatViewBubble() 或者 row.mBaseHolder.getBubbleView()）
     * 2、处理消息内容的显示
     * 3、处理消息气泡高度的自定义
     * ...
     * 如果应用层不需要自定义自己的View,仅仅是在原来的View的基础上进行修改（气泡样式、气泡高度）
     * 那么可以直接返回null即可，否则将自定义的View返回给插件层（需要自己处理页面内容的显示）
     * <p>
     * <p>
     * 插件默认聊天的Row
     * mRowItems.put(1, new ImageRxRow(1)); //不支持定义气泡样式
     * mRowItems.put(2, new ImageTxRow(2));
     * mRowItems.put(3, new FileRxRow(3)); //支持定义气泡样式
     * mRowItems.put(4, new FileTxRow(4));
     * mRowItems.put(5, new VoiceRxRow(5)); //支持定义气泡样式
     * mRowItems.put(6, new VoiceTxRow(6));
     * mRowItems.put(7, new DescriptionRxRow(7)); //支持定义气泡样式
     * mRowItems.put(8, new DescriptionTxRow(8));
     * mRowItems.put(9, new ChattingSystemRow(9)); //不支持定义气泡样式
     * mRowItems.put(10, new LocationRxRow(10));  //不支持定义气泡样式
     * mRowItems.put(11, new LocationTxRow(11));
     * mRowItems.put(12, new SightVideoTxRow(12)); //不支持定义气泡样式
     * mRowItems.put(13, new SightVideoRxRow(13));
     * mRowItems.put(14, new RichImageRxRow(14)); //支持定义气泡样式
     * mRowItems.put(15, new RichImageTxRow(15));
     * mRowItems.put(16, new VoIPCallRxRow(16)); //支持定义气泡样式
     * mRowItems.put(17, new VoIPCallTxRow(17));
     *
     * @param message 当前Cell 显示的消息
     * @param row     当前View 对应的字段id(插件会根据不同的消息类型返回不同的Cell 实现)
     * @return 返回用户自定义的View
     */
    @Override
    public BaseChattingRow onBindView(ECMessage message, BaseChattingRow row) {
        LogUtil.e(TAG, "msgId:" + message.getMsgId() + "message:" + message.getDirection() + ",messageType:" + message.getType() + ",row:" + row);
        BaseChattingRow returnRow;
        boolean isSend = message.getDirection() == ECMessage.Direction.SEND;
        UserData userData = UserData.build(message.getUserData());
        if ("Card".equals(userData.getValueByKey("msgType"))) {
            // 如果需要自定义Item 需要返回BaseChattingRow,如果只修改row里面信息返回null
            returnRow = CustomRowUtil.getRow(isSend ? CustomRowUtil.KEY_100 : CustomRowUtil.KEY_101);
        } else {
            // 聊天row气泡更换
            View view = row.getChatViewBubble();
            if (view != null) {
                view.setBackgroundResource(isSend ? R.drawable.ytx_chating_right_a : R.drawable.ytx_chating_left_a);
            }
            return null;
        }
        LogUtil.e(TAG, "currentRow:" + row + ",returnRow:" + returnRow);
        return returnRow;
    }

    /**
     * 处理沟通列表的Item的UI
     * 如果应用层不需要自定义自己的View,仅仅是在原来的View的基础上进行修改（字体颜色，字体大小）
     * 那么可以直接返回null即可，否则将自定义的View返回给插件层（需要自己处理页面内容的显示）
     *
     * @param conversation 当前Item对应的信息
     * @param convertView  缓存使用的View
     * @param holder       当前Item对应的UI
     * @return
     */
    @Override
    public View onBindView(Context context, View convertView, RXConversation conversation, ConversationAdapter.BaseConversationViewHolder holder) {
        LogUtil.e(TAG, "context:" + context + ",convertView:" + convertView + ",conversatoin:" + conversation + ",holder:" + holder);
        if (AppMgr.getUserId().equals(conversation.getSessionId())) {
            // For example, if you are chatting with yourself, change your name
            ((ConversationAdapter.ViewHolder) holder).nickname_tv.setText("Self");
            LogUtil.e(TAG, "self Account Row");
        } else if ("10086".equals(conversation.getSessionId())) {
            // If 10086 is an administrator, then you can define your own layout directly and display different information
            int viewId = convertView == null ? 0 : convertView.getId();
            ConversationViewHolder viewHolder;
            LogUtil.e(TAG, "viewId:" + viewId + ",Rid:" + R.id.customAdminAccount + ",is eq result:" + (viewId == R.id.customAdminAccount));
            if (viewId == R.id.customAdminAccount) {
                viewHolder = (ConversationViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.ytx_conversation_custom, null);
                convertView.setId(R.id.customAdminAccount);
                viewHolder = new ConversationViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            viewHolder.mTextView.setText("超级管理员");

            return convertView;
        }
        return null;
    }


    /**
     * If you need to define your own ViewHolder, you must extends ConversationAdapter.BaseConversationViewHolder
     */
    public class ConversationViewHolder extends ConversationAdapter.BaseConversationViewHolder {
        public TextView mTextView;

        public ConversationViewHolder(View baseView) {
            mTextView = baseView.findViewById(R.id.textview);
        }
    }


}

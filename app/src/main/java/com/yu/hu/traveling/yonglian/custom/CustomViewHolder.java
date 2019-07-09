package com.yu.hu.traveling.yonglian.custom;

import android.view.View;
import android.widget.TextView;

import com.yu.hu.traveling.R;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.UserData;
import com.yuntongxun.plugin.im.ui.MessagePageAble;
import com.yuntongxun.plugin.im.ui.chatting.holder.BaseHolder;
import com.yuntongxun.plugin.im.ui.chatting.model.ViewHolderTag;

/**
 * Created with Android Studio IDEA.
 *
 * @author WJ
 * @version 1.0
 * @since 2017/8/21 14:15
 */

public class CustomViewHolder extends BaseHolder {
    private static final String TAG = LogUtil.getLogUtilsTag(CustomViewHolder.class);
    private TextView mName;
    private TextView mTel;

    private View mLayoutContent;

    public CustomViewHolder(int type) {
        super(type);
    }

    @Override
    public BaseHolder initBaseHolder(View baseview) {
        super.initBaseHolder(baseview);
        mName = (TextView) baseview.findViewById(R.id.name);
        mTel = baseview.findViewById(R.id.tel);

        mLayoutContent = baseview.findViewById(R.id.layout_content_to);
        return this;
    }


    public void init(MessagePageAble messagePageAble, ECMessage message, int position) {
        if (message != null) {
            UserData userData = UserData.build(message.getUserData());
            String name = userData.getValueByKey("name");
            String tel = userData.getValueByKey("tel");
            mName.setText(name);
            mTel.setText(tel);


            ViewHolderTag holderTag = ViewHolderTag.createTag(message, ViewHolderTag.TagType.TAG_PREVIEW, position, false);
            View.OnLongClickListener onLongClickListener = messagePageAble.getOnLongClickListener();
            // 给设置事件的View添加Tag ，否则插件不会对事件进行响应
            this.mLayoutContent.setTag(holderTag);
            this.mLayoutContent.setOnLongClickListener(onLongClickListener);
        }
    }

}

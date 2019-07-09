package com.yu.hu.traveling.widget;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.library.widget.ICustomView;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.entity.comment.Reply;
import com.yu.hu.traveling.util.SpannableStringUtil;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.widget
 * 文件名：ReplyBox
 * 创建者：HY
 * 创建时间：2019/7/3 10:18
 * 描述：  评论下面的回复列表
 * <p>
 * {@code R.layout.item_comment.xml}
 */
public class ReplyBox extends LinearLayout implements ICustomView {

    private Context mContext;

    private TextView mComment1, mComment2, mComment3;

    public ReplyBox(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_reply_box, this, true);
        initView(context);
    }


    @Override
    public void initView(Context context) {
        setOrientation(VERTICAL);

        mComment1 = findViewById(R.id.tv_comment_1);
        mComment2 = findViewById(R.id.tv_comment_2);
        mComment3 = findViewById(R.id.tv_comment_3);
    }

    /**
     * 将回复/评论显示出来
     */
    public void build(List<Reply> replyList) {

        int num = replyList.size() > 3 ? 3 : replyList.size();
        switch (num) {
            case 3:
                mComment3.setVisibility(VISIBLE);
                mComment3.setText(mContext.getString(R.string.all_comment, replyList.size()));
            case 2:
                mComment2.setVisibility(VISIBLE);
                mComment2.setText(getSpannableString(replyList.get(1)));
                //设置高亮背景颜色为透明色 点击时看不出来
                mComment2.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));   //设置高亮背景颜色为透明色
            case 1:
                mComment1.setVisibility(VISIBLE);
                mComment1.setText(getSpannableString(replyList.get(0)));
                //设置高亮背景颜色为透明色
                mComment1.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));   //设置高亮背景颜色为透明色
                break;
        }
    }

    /**
     * 区别开每条评论的用户名和评论/回复内容
     *
     * @param reply reply
     * @return SpannableString
     */
    private SpannableString getSpannableString(Reply reply) {
        SpannableString spannableString;
        if (reply.getFlag() == Reply.FLAG_COMMENT) {
            //评论
            spannableString = new SpannableString(
                    mContext.getResources().getString(R.string.comment, reply.getNickName(), reply.getContent()));
        } else {
            //回复
            spannableString = new SpannableString(
                    mContext.getResources().getString(R.string.comment_reply, reply.getNickName(), reply.getToName(), reply.getContent()));
            ClickableSpan to = SpannableStringUtil.getClickableReplySpan(mContext, widget -> {
                ToastUtils.showShort("你点击了" + reply.getToId());
                //todo
                //                Intent intent = new Intent(mContext, PersonalActivity.class);
                //                intent.putExtra(PersonalActivity.USER_ID, reply.getToId());
                //                mContext.startActivity(intent);
            });
            int start = reply.getNickName().length() + 4;
            spannableString.setSpan(to, start, start + reply.getToName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        ClickableSpan clickableSpan = SpannableStringUtil.getClickableReplySpan(mContext, widget -> {
            ToastUtils.showShort("你点击了" + reply.getUserId());
            //            Intent intent = new Intent(mContext, PersonalActivity.class);
            //            intent.putExtra(PersonalActivity.USER_ID, reply.getUserId());
            //            mContext.startActivity(intent);
        });
        spannableString.setSpan(clickableSpan, 0, reply.getNickName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}

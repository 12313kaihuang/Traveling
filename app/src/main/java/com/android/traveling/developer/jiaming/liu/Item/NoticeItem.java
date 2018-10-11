package com.android.traveling.developer.jiaming.liu.Item;
/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Item
 * 文件名：NoticeItem
 * 创建者：LJM
 * 创建时间：2018/10/9 19:33
 * 描述：  通知聊天消息列表的子布局
 */
public class NoticeItem {
    private int headPicture;
    private String reply;
    private int ownContentImage;


    public int getHeadPicture() {
        return headPicture;
    }

    public void setHeadPicture(int headPicture) {
        this.headPicture = headPicture;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getOwnContentImage() {
        return ownContentImage;
    }

    public void setOwnContentImage(int ownContentImage) {
        this.ownContentImage = ownContentImage;
    }
}

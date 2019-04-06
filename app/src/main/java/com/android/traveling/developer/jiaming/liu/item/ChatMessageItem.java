package com.android.traveling.developer.jiaming.liu.item;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Item
 * 文件名：ChatMessageItem
 * 创建者：LJM
 * 创建时间：2018/9/29 20:33
 * 描述：  聊天消息列表的子布局
 */
public class ChatMessageItem {
    public static final int RECEIVE_MESSAGE = 0;//接收到的消息
    public static final int SEND_MESSAGE = 1;//发送出去的消息
    private String uerId;
    private String content;
    private int type;

    public ChatMessageItem(){

    }
    public ChatMessageItem(String uerId, String content, int type){
        this.uerId = uerId;
        this.content = content;
        this.type = type;
    }

    public String getUerId() {
        return uerId;
    }

    public void setUerId(String uerId) {
        this.uerId = uerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}

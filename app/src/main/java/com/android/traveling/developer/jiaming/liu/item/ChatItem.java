package com.android.traveling.developer.jiaming.liu.item;
/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Item
 * 文件名：ChatItem
 * 创建者：LJM
 * 创建时间：2018/9/29 19:03
 * 描述：  聊天列表的子布局
 */
public class ChatItem {
    private int headPicture;
    private String txtNewReply;
    private String txtOthersName;
    private String txtChatTime;

    public ChatItem(){

    }
    public ChatItem(int headPicture,String txtNewReply,String txtOthersName,String txtChatTime){
        this.headPicture = headPicture;
        this.txtNewReply = txtNewReply;
        this.txtOthersName = txtOthersName;
        this.txtChatTime = txtChatTime;
    }
    public int getHeadPicture() {
        return headPicture;
    }
    public void setHeadPicture(int headpicture) {
        this.headPicture = headpicture;
    }

    public String getTxtNewReply() {
        return txtNewReply;
    }

    public void setTxtNewReply(String txtNewReply) {
        this.txtNewReply = txtNewReply;
    }

    public String getTxtOthersName() {
        return txtOthersName;
    }

    public void setTxtOthersName(String txtOthersName) {
        this.txtOthersName = txtOthersName;
    }

    public String getTxtChatTime() {
        return txtChatTime;
    }

    public void setTxtChatTime(String txtChatTime) {
        this.txtChatTime = txtChatTime;
    }
}

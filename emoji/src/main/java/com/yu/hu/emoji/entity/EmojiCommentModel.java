package com.yu.hu.emoji.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.yu.hu.emoji.BR;
import com.yu.hu.emoji.EmojiCommentDialog;

/**
 * @author Hy
 * created on 2020/04/22 18:18
 **/
public class EmojiCommentModel extends BaseObservable {

    private boolean emojiListVisible = false;

    private String inputText;
    private EmojiCommentDialog.Builder builder;

    public EmojiCommentModel(EmojiCommentDialog.Builder mBuilder) {
        builder = mBuilder;
    }

    public String getHint() {
        return builder.getHint();
    }

    @Bindable
    public EmojiCommentDialog.Builder getBuilder() {
        return builder;
    }

    @Bindable
    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
        notifyPropertyChanged(BR.inputText);
    }

    @Bindable
    public boolean isEmojiListVisible() {
        return emojiListVisible;
    }

    public void setEmojiListVisible(boolean emojiListVisible) {
        this.emojiListVisible = emojiListVisible;
    }

    public void toggleEmojiListVisibile() {
        emojiListVisible = !emojiListVisible;
        notifyPropertyChanged(BR._all);
    }
}

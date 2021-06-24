package com.yu.hu.ninegridlayout.entity;

/**
 * @author Hy
 * created on 2020/04/19 10:03
 **/
public class SpacingConfig {

    public int left;
    public int top;
    public int right;
    public int bottom;

    public SpacingConfig() {
    }

    public SpacingConfig(int spacing) {
        this.left = this.top = this.right = this.bottom = spacing;
    }
}

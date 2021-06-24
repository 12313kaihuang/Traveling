package com.yu.hu.common.dialog;

/**
 * @author Hy
 * created on 2020/04/19 12:38
 **/
public abstract class DialogBuilder<D extends BaseDialog> {

    public abstract D build();

    public void show() {
        build().show();
    }
}

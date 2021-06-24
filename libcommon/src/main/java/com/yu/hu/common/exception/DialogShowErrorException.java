package com.yu.hu.common.exception;

import com.yu.hu.common.dialog.BaseDialog;

/**
 * Created by Hy on 2019/11/29 18:58
 * <p>
 *
 * @see BaseDialog#show() dialog展示出现异常时会抛出此异常
 **/
public class DialogShowErrorException extends BaseRuntimeException {

    public DialogShowErrorException(String msg) {
        super(msg);
    }
}

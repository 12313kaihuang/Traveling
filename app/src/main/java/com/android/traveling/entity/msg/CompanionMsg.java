package com.android.traveling.entity.msg;

import com.android.traveling.entity.companion.Companion;

import java.util.List;

/**
 * Created by HY
 * 2019/2/25 18:55
 */
@SuppressWarnings("unused")
public class CompanionMsg extends Msg {

    private List<Companion> companions;

    public CompanionMsg() {
    }

    public CompanionMsg(List<Companion> companions) {
        super(CORRECT_STATUS, "");
        this.companions = companions;
    }

    public List<Companion> getCompanions() {
        return companions;
    }

    public void setCompanions(List<Companion> companions) {
        this.companions = companions;
    }
}

package com.android.traveling.entity.msg;


import com.android.traveling.entity.note.Note;
import com.android.traveling.entity.note.NoteList;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HY
 * 2019/1/20 16:41
 */
@SuppressWarnings("unused")
public class NoteMsg extends Msg implements Serializable {

    private List<Note> notes;

    public NoteMsg() {
    }

    public NoteMsg(List<NoteList> noteLists) {
        super(correctStatus,"");
        this.notes = Note.transform(noteLists);
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}



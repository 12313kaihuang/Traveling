package com.yu.hu.traveling.entity.msg;


import com.yu.hu.traveling.entity.note.Note;
import com.yu.hu.traveling.entity.note.NoteList;

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
        super(CORRECT_STATUS,"");
        this.notes = Note.transform(noteLists);
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}



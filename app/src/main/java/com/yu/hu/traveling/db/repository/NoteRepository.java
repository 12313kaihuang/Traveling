package com.yu.hu.traveling.db.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.yu.hu.traveling.db.TravelingDatabase;
import com.yu.hu.traveling.db.dao.NoteDao;
import com.yu.hu.traveling.model.Note;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/20 15:27
 **/
@SuppressWarnings("unused")
public class NoteRepository {

    private static volatile NoteRepository sINSTANCE;
    private final NoteDao noteDao;

    public static NoteRepository getInstance() {
        if (sINSTANCE == null) {
            synchronized (NoteRepository.class) {
                if (sINSTANCE == null) {
                    sINSTANCE = new NoteRepository();
                }
            }
        }
        return sINSTANCE;
    }

    private NoteRepository() {
        noteDao = TravelingDatabase.get().noteDao();
    }

    /**
     * 利用Room对paging的天然支持直接创建Factory
     */
    public static DataSource.Factory<Integer, Note> getDataSourceFactory() {
        return getInstance().noteDao.getFactory();
    }

    public DataSource.Factory<Integer, Note> getNoteFactory() {
        return getInstance().noteDao.getNotesFactory();
    }

    public DataSource.Factory<Integer, Note> getStrategyFactory() {
        return getInstance().noteDao.getStrategiesFactory();
    }

    public List<Note> getAllFuzzily(String searchContent) {
        String content = "%" + searchContent + "%";
        return getInstance().noteDao.getAllFuzzily(content);
    }

    public LiveData<Note> getNoteLiveData(int noteId) {
        return noteDao.getNoteLiveData(noteId);
    }

    public void insert(Note note) {
        noteDao.insert(note);
    }

    public void insertAll(List<Note> notes) {
        for (Note note : notes) {
            note.transform(true);
        }
        noteDao.insertAll(notes);
    }

    public void deleteAll() {
        noteDao.deleteAll();
    }

    public void delete(int noteId) {
        noteDao.delete(noteId);
    }

    public DataSource.Factory<Integer, Note> getTargetNoteFactory(int userId) {
        return noteDao.getFactoryById(Note.TYPE_NOTE, userId);
    }

    public DataSource.Factory<Integer, Note> getTargetStrategyFactory(int userId) {
        return noteDao.getFactoryById(Note.TYPE_STRATEGY, userId);
    }
}

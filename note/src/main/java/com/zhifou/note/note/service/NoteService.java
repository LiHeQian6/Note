package com.zhifou.note.note.service;

import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.repository.NoteRepository;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author : li
 * @Date: 2021-02-08 17:24
 */
@Service
@Transactional
public class NoteService {
    @Resource
    private NoteRepository noteRepository;
    @PersistenceContext
    private EntityManager entityManager;


    public Note getNote(int id) throws NoteException {
        Note note = noteRepository.findNoteByIdAndStatus(id,0);
        if (note ==null) {
            throw new NoteException("没有找到指定笔记！", Status.NOT_FOUND_NOTE);
        }
        return note;
    }

    public void addNote(Note note) {
        noteRepository.save(note);
    }

    public Note getNote(int id, String username) throws NoteException {
        Note note = getNote(id);
        if (note.getUser().getUsername().equals(username)) {
            return note;
        }
        throw new NoteException("没有找到指定笔记！", Status.USER_CREDENTIALS_EXPIRED);
    }

    public void updateNote(Note note) {
        noteRepository.save(note);
    }

    public void deleteNote(int id, String username) throws NoteException {
        Note note = getNote(id, username);
        noteRepository.delete(note);
    }

    public long getNoteCount() {
        return noteRepository.count();
    }

    public HashMap<String,BigInteger> getTagNoteCount() {
        HashMap<String, BigInteger> count = new HashMap<>();
        Query query = entityManager.createNativeQuery("select name,count(name) num from notes_tags left join tag t on t.id = notes_tags.tag_id group by name order by count(name) limit 5;");
        List<Object[]> resultList = query.getResultList();
        for (Object[] objects : resultList) {
            count.put((String) objects[0],(BigInteger)objects[1]);
        }
        return count;
    }

    public DataTablesOutput<NoteVO> getNotes(DataTablesInput input) {
        DataTablesOutput<NoteVO> output = new DataTablesOutput<>();
        ArrayList<NoteVO> noteVOS = new ArrayList<>();
        DataTablesOutput<Note> notes = noteRepository.findAll(input);
        List<Note> data = notes.getData();
        for (Note note : data) {
            NoteVO noteVO = new NoteVO(note);
            noteVOS.add(noteVO);
        }
        output.setData(noteVOS);
        output.setRecordsFiltered(notes.getRecordsFiltered());
        output.setRecordsTotal(notes.getRecordsTotal());
        return output;
    }
}

package com.zhifou.note.note.service;

import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.entity.Type;
import com.zhifou.note.note.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-02-08 17:24
 */
@Service
@Transactional
public class NoteService {
    @Resource
    private NoteRepository noteRepository;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagService;


    public Note getNote(int id) throws NoteException {
        Note note = noteRepository.findNoteById(id);
        if (note ==null) {
            throw new NoteException("没有找到指定笔记！", Status.USER_CREDENTIALS_EXPIRED);
        }
        return note;
    }

    public void addNote(Note note) {
        Type type = note.getType();
        note.setType(typeService.getType(type.getId()));
        Set<Tag> tags = note.getTags();
        note.setTags(tagService.getTags(tags));
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
        Type type = note.getType();
        note.setType(typeService.getType(type.getId()));
        Set<Tag> tags = note.getTags();
        note.setTags(tagService.getTags(tags));
        noteRepository.save(note);
    }

    public void deleteNote(int id, String username) throws NoteException {
        Note note = getNote(id, username);
        noteRepository.delete(note);
    }
}

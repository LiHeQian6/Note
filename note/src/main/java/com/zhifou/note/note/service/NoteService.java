package com.zhifou.note.note.service;

import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-02-08 17:24
 */
@Service
@Transactional
public class NoteService {
    @Resource
    private NoteRepository noteRepository;


    public Note getNote(int noteId) {
        return noteRepository.findNoteById(noteId);
    }

    public void publishNote(Note note) {
        noteRepository.save(note);
    }
}

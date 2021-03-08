package com.zhifou.note.note.repository;

import com.zhifou.note.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : li
 * @Date: 2021-02-08 17:24
 */
@Repository
public interface NoteRepository extends JpaRepository<Note,Integer> {
    Note findNoteByIdAndStatus(int noteId,int status);
}

package com.zhifou.note.note.repository;

import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : li
 * @Date: 2021-02-08 17:24
 */
@Repository
public interface NoteRepository extends DataTablesRepository<Note,Integer>, JpaRepository<Note,Integer> {
    Note findNoteByIdAndStatus(int noteId,int status);

    Page<Note> findNotesByType_Id(int type_id, Pageable pageable);

    Page<Note> findNotesByTagsIsContaining(Tag tag, Pageable pageable);
}

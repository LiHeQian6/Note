package com.zhifou.note.note.service;

import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.repository.NoteRepository;
import com.zhifou.note.note.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public class NoteService implements Constant {
    @Resource
    private NoteRepository noteRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private TagRepository tagRepository;
//    @Resource
//    private LikeService likeService;
//    @Resource
//    private CollectService collectService;

    /**
     * @param: id
     * @return com.zhifou.note.note.entity.Note
     * @description 根据id获取笔记
     * @author li
     * @Date 2021/3/24 22:22
     */
    public Note getNote(int id) throws NoteException {
        Note note = noteRepository.findNoteByIdAndStatus(id,0);
        if (note ==null) {
            throw new NoteException("没有找到指定笔记！", Status.NOT_FOUND_NOTE);
        }
        return note;
    }

    /**
     * @param: note
     * @return void
     * @description 添加笔记
     * @author li
     * @Date 2021/3/24 22:22
     */
    public void addNote(Note note) {
        noteRepository.save(note);
    }

    /**
     * @param: id
     * @param: username
     * @return com.zhifou.note.note.entity.Note
     * @description 查询指定用户的指定笔记
     * @author li
     * @Date 2021/3/24 22:21
     */
    public Note getNote(int id, String username) throws NoteException {
        Note note = getNote(id);
        if (note.getUser().getUsername().equals(username)) {
            return note;
        }
        throw new NoteException("没有找到指定笔记！", Status.USER_CREDENTIALS_EXPIRED);
    }

    /**
     * @param: note
     * @return void
     * @description 修改笔记
     * @author li
     * @Date 2021/3/24 22:21
     */
    public void updateNote(Note note) {
        noteRepository.save(note);
    }

    /**
     * @param: id
     * @param: username
     * @return void
     * @description 删除指定用户的指定笔记
     * @author li
     * @Date 2021/3/24 22:19
     */
    public void deleteNote(int id, String username) throws NoteException {
        Note note = getNote(id, username);
        noteRepository.delete(note);
    }
    /**
     * @param: id
     * @param: username
     * @return void
     * @description 删除指定的笔记
     * @author li
     * @Date 2021/3/24 22:19
     */
    public void deleteNote(int id) throws NoteException {
        Note note = getNote(id);
        noteRepository.delete(note);
    }

    /**
     * @param:
     * @return long
     * @description 获取笔记总数量
     * @author li
     * @Date 2021/3/24 22:19
     */
    public long getNoteCount() {
        return noteRepository.count();
    }

    /**
     * @param:
     * @return java.util.HashMap<java.lang.String,java.math.BigInteger>
     * @description 获取笔记数量前5的标签及具体数量
     * @author li
     * @Date 2021/3/24 22:18
     */
    public HashMap<String,BigInteger> getTagNoteCount() {
        HashMap<String, BigInteger> count = new HashMap<>();
        Query query = entityManager.createNativeQuery("select name,count(name) num from notes_tags left join tag t on t.id = notes_tags.tag_id group by name order by count(name) limit 5;");
        List<Object[]> resultList = query.getResultList();
        for (Object[] objects : resultList) {
            count.put((String) objects[0],(BigInteger)objects[1]);
        }
        return count;
    }

    /**
     * @param: input
     * @return org.springframework.data.jpa.datatables.mapping.DataTablesOutput<com.zhifou.note.bean.NoteVO>
     * @description 后台管理系统分页查询所有笔记
     * @author li
     * @Date 2021/3/24 22:17
     */
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

    /**
     * @param: page
     * @param: size
     * @param: typeId
     * @return org.springframework.data.domain.Page<com.zhifou.note.bean.NoteVO>
     * @description 按类型分页查询笔记
     * @author li
     * @Date 2021/3/24 22:18
     */
    public Page<NoteVO> getNotesByType(int page, int size,int typeId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Note> notes = noteRepository.findNotesByType_Id(typeId,pageRequest);
        ArrayList<NoteVO> noteVOList = new ArrayList<>();
        for (Note note : notes) {
            NoteVO noteVO = new NoteVO(note);
            noteVOList.add(noteVO);
        }
        return new PageImpl<>(noteVOList,pageRequest,noteVOList.size());
    }

    /**
     * @param: page
     * @param: size
     * @param: tagId
     * @return org.springframework.data.domain.Page<com.zhifou.note.bean.NoteVO>
     * @description 按标签分页查询笔记
     * @author li
     * @Date 2021/3/24 22:18
     */
    public Page<NoteVO> getNotesByTag(int page, int size,int tagId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Note> notes = noteRepository.findNotesByTagsIsContaining(tagRepository.getOne(tagId),pageRequest);
        ArrayList<NoteVO> noteVOList = new ArrayList<>();
        for (Note note : notes) {
            NoteVO noteVO = new NoteVO(note);
            noteVOList.add(noteVO);
        }
        return new PageImpl<>(noteVOList, pageRequest, noteVOList.size());
    }

    /**
     * @param: page
     * @param: size
     * @return org.springframework.data.domain.Page<com.zhifou.note.bean.NoteVO>
     * @description 分页获取笔记
     * @author li
     * @Date 2021/4/20 16:49
     */
    public Page<NoteVO> getNotesByPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Note> notes = noteRepository.findAll(pageRequest);
        ArrayList<NoteVO> noteVOList = new ArrayList<>();
        for (Note note : notes) {
            NoteVO noteVO = new NoteVO(note);
            noteVOList.add(noteVO);
        }
        return new PageImpl<>(noteVOList, pageRequest, noteVOList.size());
    }
}

package com.zhifou.note.note.service;

import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.TagException;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-03-01 15:38
 */
@Service
@Transactional
public class TagService implements Constant {
    @Resource
    private TagRepository tagRepository;

    public Page<Tag> getAllTags(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return tagRepository.findTagsByStatus(0,pageRequest);
    }

    public void createTag(Tag tag) throws TagException {
        if (!tagRepository.existsByName(tag.getName())) {
            tagRepository.save(tag);
        }else {
            throw new TagException("标签已经存在！", Status.TAG_ALREADY_EXIST);
        }
    }

    public void disableTag(int id) throws TagException {
        Tag tag = tagRepository.getOne(id);
        tag.setStatus(DISABLE);
        tagRepository.save(tag);
    }

    public Tag findTagByName(String name) {
        return tagRepository.findTagByName(name);
    }

    public void updateTag(Tag tag) throws TagException {
        tagRepository.getOne(tag.getId());
        if (findTagByName(tag.getName())==null) {
            tagRepository.save(tag);
        }else {
            throw new TagException("标签已经存在！", Status.TAG_ALREADY_EXIST);
        }
    }

    public DataTablesOutput<Tag> getTags(DataTablesInput input) {
        return tagRepository.findAll(input);
    }

    public void deleteTag(int id) throws TagException {
        Tag tag = getTag(id);
        tagRepository.delete(tag);
    }

    private Tag getTag(int id) throws TagException {
        Tag tag = tagRepository.findTagById(id);
        if (tag ==null) {
            throw new TagException("没有找到指定标签！", Status.NOT_FOUND_TAG);
        }
        return tag;
    }
}

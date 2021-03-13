package com.zhifou.note.note.service;

import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.TagException;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 15:38
 */
@Service
@Transactional
public class TagService {
    @Resource
    private TagRepository tagRepository;

    public Set<Tag> getAllTags(){
        return tagRepository.findTagsByStatus(0);
    }

    public void createTag(Tag tag) throws TagException {
        if (!tagRepository.existsByName(tag.getName())) {
            tagRepository.save(tag);
        }else {
            throw new TagException("标签已经存在！", Status.TAG_ALREADY_EXIST);
        }
    }
}

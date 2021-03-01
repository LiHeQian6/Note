package com.zhifou.note.note.service;

import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
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

    public Set<Tag> getTags(Set<Tag> tags){
        HashSet<Integer> ids = new HashSet<>();
        for (Tag tag : tags) {
            ids.add(tag.getId());
        }
        return tagRepository.findTagsByIdIn(ids);
    }
}

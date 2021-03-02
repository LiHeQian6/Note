package com.zhifou.note.config;

import com.zhifou.note.bean.Constant;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.entity.Type;
import com.zhifou.note.note.repository.TagRepository;
import com.zhifou.note.note.repository.TypeRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-03-01 16:09
 */
@Component
public class InitialTypeAndTagDataLoader implements ApplicationListener<ContextRefreshedEvent>, Constant {
    boolean alreadySetup = false;
    @Resource
    private TypeRepository typeRepository;
    @Resource
    private TagRepository tagRepository;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup)
            return;
        if (typeRepository.count()==0){
            for (int i = 0; i < TYPE_INIT_DATA.length; i++) {
                Type parent = new Type();
                parent.setName(TYPE_INIT_DATA[i]);
                typeRepository.save(parent);
                for (int j = 0; j < CHILD_TYPE_INIT_DATA[i].length; j++) {
                    Type type = new Type();
                    type.setName(CHILD_TYPE_INIT_DATA[i][j]);
                    type.setParent(parent);
                    typeRepository.save(type);
                }
            }
        }
        if (tagRepository.count()==0){
            for (String tagName : TAG_INIT_DATA) {
                Tag tag = new Tag();
                tag.setName(tagName);
                tagRepository.save(tag);
            }
        }
        alreadySetup = true;
    }
}

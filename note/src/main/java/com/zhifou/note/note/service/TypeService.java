package com.zhifou.note.note.service;

import com.zhifou.note.note.entity.Type;
import com.zhifou.note.note.repository.TypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-03-01 15:25
 */
@Service
@Transactional
public class TypeService {
    @Resource
    private TypeRepository typeRepository;

    public Type getType(int id){
        return typeRepository.findTypeById(id);
    }
}

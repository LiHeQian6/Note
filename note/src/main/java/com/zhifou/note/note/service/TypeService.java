package com.zhifou.note.note.service;

import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.TypeException;
import com.zhifou.note.note.entity.Type;
import com.zhifou.note.note.repository.TypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 15:25
 */
@Service
@Transactional
public class TypeService implements Constant {
    @Resource
    private TypeRepository typeRepository;

    public Type getType(int id) throws TypeException {
        Type type = typeRepository.getById(id);
        if (type==null) {
            throw new TypeException("没有找到指定类型！", Status.NOT_FOUND_TYPE);
        }
        return type;
    }

    public Set<Type> getTypes() {
        return typeRepository.findTypeByParentIsNullAndStatus(0);
    }

    public void addType(Type type) throws TypeException {
        if (typeRepository.existsById(type.getId())) {
            throw new TypeException("类型已经存在！",Status.TYPE_ALREADY_EXIST);
        }
        typeRepository.save(type);
    }

    public void disableType(int id) throws TypeException {
        if (typeRepository.existsById(id)) {
            typeRepository.getById(id).setStatus(DISABLE);
        }else {
            throw new TypeException("没有找到指定类型！", Status.NOT_FOUND_TYPE);
        }
    }
}

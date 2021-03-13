package com.zhifou.note.user.service;

import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.PrivilegeException;
import com.zhifou.note.user.entity.Privilege;
import com.zhifou.note.user.repository.PrivilegeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : li
 * @Date: 2021-03-11 21:27
 */
@Service
@Transactional
public class PrivilegeService {
    @Resource
    private PrivilegeRepository privilegeRepository;

    public List<Privilege> getPrivileges(){
        return privilegeRepository.findAll();
    }

    public void createPrivilege(Privilege privilege) throws PrivilegeException {
        if (!privilegeRepository.existsPrivilegeByName(privilege.getName())) {
            privilegeRepository.save(privilege);
        }else {
            throw new PrivilegeException("角色已经存在！", Status.PRIVILEGE_ALREADY_EXIST);
        }
    }

    public void updatePrivilege(Privilege privilege) throws PrivilegeException {
        if (privilegeRepository.existsById(privilege.getId())) {
            privilegeRepository.save(privilege);
        }else {
            throw new PrivilegeException("角色不存在！", Status.NOT_FOUND_PRIVILEGE);
        }
    }
}

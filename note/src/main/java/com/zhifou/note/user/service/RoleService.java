package com.zhifou.note.user.service;

import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.RoleException;
import com.zhifou.note.user.entity.Role;
import com.zhifou.note.user.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : li
 * @Date: 2021-03-11 21:26
 */
@Service
@Transactional
public class RoleService {
    @Resource
    private RoleRepository roleRepository;

    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public void createRole(Role role) throws RoleException {
        if (!roleRepository.existsRoleByName(role.getName())) {
            roleRepository.save(role);
        }else {
            throw new RoleException("角色已经存在！", Status.ROLE_ALREADY_EXIST);
        }
    }

    public void updatePrivilege(Role role) throws RoleException {
        if (roleRepository.existsById(role.getId())) {
            roleRepository.save(role);
        }else {
            throw new RoleException("角色不存在！", Status.NOT_FOUND_ROLE);
        }
    }

    public List<Role> findRoles(List<Integer> ids){
        return roleRepository.findAllById(ids);
    }

    public Role getRole(String roleName) {
        return roleRepository.findByName(roleName);
    }
}

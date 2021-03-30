package com.zhifou.note.admin.controller;

import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.exception.PrivilegeException;
import com.zhifou.note.exception.RoleException;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.user.entity.Certification;
import com.zhifou.note.user.entity.Privilege;
import com.zhifou.note.user.entity.Role;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.CertificationService;
import com.zhifou.note.user.service.PrivilegeService;
import com.zhifou.note.user.service.RoleService;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author : li
 * @Date: 2021-02-08 18:28
 */
@Api("管理系统api")
@RestController
@Validated
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private UserDetailsServiceImp userService;
    @Resource
    private RoleService roleService;
    @Resource
    private NoteService noteService;
    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private CertificationService certificationService;

    @ApiOperation("分页获取所有用户")
    @GetMapping("/users")
    public Page<User> getUsers(int page, int size){
        return userService.getUsers(page,size);
    }

    @ApiOperation("分页获取所有笔记")
    @GetMapping("/notes")
    public DataTablesOutput<NoteVO> getNotes(@Valid DataTablesInput input){
        input.getColumns().remove(input.getColumns().size()-1);
        return noteService.getNotes(input);
    }

    @ApiOperation("禁用用户")
    @DeleteMapping("/user/{id}")
    @ResponseBody
    public void disableUser(@PathVariable int id) throws UserException {
        userService.disableUser(id);
    }

    @ApiOperation("获取所有角色")
    @GetMapping("/roles")
    public List<Role> getRoles(){
        return roleService.getRoles();
    }

    @ApiOperation("创建角色并赋予权限")
    @PostMapping("/role")
    public void createRole(Role role) throws RoleException {
        roleService.createRole(role);
    }

    @ApiOperation("为用户新增角色")
    @PostMapping("/role/{userId}/{roleId}")
    public void addRole(@PathVariable int userId,@PathVariable int roleId) throws UserException {
        //todo 管理员批量添加认证
        userService.addRole(userId,roleId);
    }
    @ApiOperation("为用户移除角色")
    @DeleteMapping("/role/{userId}/{roleId}")
    public void removeRole(@PathVariable int userId,@PathVariable int roleId) throws UserException {
        userService.removeRole(userId,roleId);
    }

    @ApiOperation("修改角色名及角色权限")
    @PutMapping("/role")
    public void updateRole(Role role) throws RoleException {
        roleService.updatePrivilege(role);
    }

    @ApiOperation("获取所有权限")
    @GetMapping("/privileges")
    public List<Privilege> getPrivileges(){
        return privilegeService.getPrivileges();
    }

    @ApiOperation("创建新权限")
    @PostMapping("/privilege")
    public void createPrivilege(Privilege privilege) throws PrivilegeException {
        privilegeService.createPrivilege(privilege);
    }

    @ApiOperation("修改权限")
    @PutMapping("/privilege")
    public void updatePrivilege(Privilege privilege) throws PrivilegeException {
        privilegeService.updatePrivilege(privilege);
    }


    @ApiOperation("添加认证信息")
    @PostMapping("/certification")
    public void addCertificationInfo(@Valid @RequestBody Certification certification){
        certificationService.createCertification(certification);
    }

}

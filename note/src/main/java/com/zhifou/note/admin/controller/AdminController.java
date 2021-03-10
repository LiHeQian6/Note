package com.zhifou.note.admin.controller;

import com.zhifou.note.user.entity.Privilege;
import com.zhifou.note.user.entity.Role;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-02-08 18:28
 */
@Api("管理系统api")
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private DataService dataService;

    @ApiOperation("分页获取所有用户")
    @GetMapping("/user")
    public Set<User> getUsers(int page,int size){
        return null;
    }

    @ApiOperation("禁用用户")
    @DeleteMapping("/user/{id}")
    public void disableUser(@PathVariable int id){

    }

    @ApiOperation("添加管理员并分配角色")
    @PostMapping("/add")
    public void addAdmin(int userId,int roleId){

    }
    @ApiOperation("删除管理员")
    @DeleteMapping("/delete/{id}")
    public void deleteAdmin(@PathVariable int id){

    }

    @ApiOperation("修改管理员角色")
    @PutMapping("/changeRole")
    public void changeRole(int userId,int roleId){

    }

    @ApiOperation("获取所有角色")
    @GetMapping("/roles")
    public Set<Role> getRoles(){
        return null;
    }

    @ApiOperation("创建角色并赋予权限")
    @PostMapping("/role")
    public void createRole(Role role){

    }

    @ApiOperation("修改角色权限")
    @PutMapping("/role")
    public void changeRole(Role role){

    }

    @ApiOperation("获取所有权限")
    @GetMapping("/privileges")
    public Set<Privilege> getPrivileges(){
        return null;
    }

    @ApiOperation("创建新权限")
    @PostMapping("/privilege")
    public void createPrivilege(Privilege privilege){

    }




    @RequestMapping("/{page}")
    public String toPage(@PathVariable String page) {
        return page;
    }


    @ApiOperation("首页")
    @RequestMapping("/")
    public String index(HttpServletRequest request){
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        calendar.after(time);
        dataService.recordUV(request.getRemoteAddr());
        long l = dataService.calculateUV(time,calendar.getTime());
        request.setAttribute("uv",l);
        return "admin-index";
    }
}

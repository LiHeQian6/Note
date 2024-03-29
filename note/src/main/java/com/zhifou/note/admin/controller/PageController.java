package com.zhifou.note.admin.controller;

import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.user.entity.Role;
import com.zhifou.note.user.service.DataService;
import com.zhifou.note.user.service.RoleService;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

/**
 * @author : li
 * @Date: 2021-03-12 17:02
 */
@Controller
@RequestMapping("/admin")
public class PageController {
    @Resource
    private DataService dataService;
    @Resource
    private NoteService noteService;
    @Resource
    private UserDetailsServiceImp userService;
    @Resource
    private RoleService roleService;

    @ApiOperation("首页")
    @RequestMapping("/")
    public String index(HttpServletRequest request){
        long todayUA = dataService.getTodayUA();
        long noteCount = noteService.getNoteCount();
        long userCount = userService.getUserCount();
        List<Long> weekDAU=dataService.getWeekDAU();
        HashMap<String, BigInteger> tagNoteCount = dataService.getTagNoteCount();
        request.setAttribute("uv",todayUA);
        request.setAttribute("noteCount",noteCount);
        request.setAttribute("userCount",userCount);
        request.setAttribute("dau",weekDAU);
        request.setAttribute("tags",tagNoteCount.keySet());
        request.setAttribute("tagsCount",tagNoteCount.values());
        return "admin-index";
    }

    @ApiOperation("用户管理")
    @RequestMapping("/user-management")
    public String userManagement(HttpServletRequest request){
        List<Role> roles = roleService.getRoles();
        String rs="";
        String rid="";
        for (Role role : roles) {
            rs+=role.getName()+",";
            rid+=role.getId()+",";
        }
        request.setAttribute("roles", rs);
        request.setAttribute("rolesId", rid);
        return "user-management";
    }

    @RequestMapping("/{page}")
    public String toPage(@PathVariable String page) {
        return page;
    }
}

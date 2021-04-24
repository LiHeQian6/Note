package com.zhifou.note.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.zhifou.note.exception.CertificationException;
import com.zhifou.note.exception.PrivilegeException;
import com.zhifou.note.exception.RoleException;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.user.entity.Certification;
import com.zhifou.note.user.entity.Privilege;
import com.zhifou.note.user.entity.Role;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.CertificationService;
import com.zhifou.note.user.service.PrivilegeService;
import com.zhifou.note.user.service.RoleService;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : li
 * @Date: 2021-04-08 17:11
 */
@Validated
@RestController
@RequestMapping("/admin")
public class UserAdminController {
    @Resource
    private UserDetailsServiceImp userService;
    @Resource
    private RoleService roleService;
    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private CertificationService certificationService;
    @Resource
    private JwtUtils jwtUtils;
    @ApiOperation("分页获取所有用户")
    @GetMapping("/users")
    public DataTablesOutput<User> getUsers(@Valid DataTablesInput input){
        input.getColumns().remove(input.getColumns().size()-1);
        input.getColumns().remove(input.getColumn("certification"));
        return userService.getUsers(input);
    }

    @ApiOperation("封禁/解封用户")
    @DeleteMapping("/user/{id}")
    @ResponseBody
    public String disableUser(@PathVariable int id) throws UserException {
        User user = jwtUtils.getUserInfo();
        if(id== user.getId()){
            return "不能操作自己的账号";
        }
        try {
            userService.disableUser(id);
        } catch (UserException e) {
            return e.getMessage();
        }
        return "true";
    }

    @ApiOperation("获取所有角色")
    @GetMapping("/roles")
    public List<Role> getRoles(){
        return roleService.getRoles();
    }

    @ApiOperation("创建角色并赋予权限")
    @PostMapping("/role")
    public void createRole(@Valid @RequestBody Role role) throws RoleException {
        roleService.createRole(role);
    }

    @ApiOperation("修改角色名及角色权限")
    @PutMapping("/role")
    public void updateRole(@Valid @RequestBody Role role) throws RoleException {
        roleService.updatePrivilege(role);
    }

    @ApiOperation("为用户新增角色")
    @PostMapping("/role/{userId}/{roleId}")
    public void addRole(@PathVariable int userId,@PathVariable int roleId) throws UserException {
        userService.addRole(userId,roleId);
    }
    @ApiOperation("修改用户角色")
    @PutMapping("/role/{userId}/{roleIds}")
    public String changeUserRoles(@PathVariable int userId,@PathVariable List<Integer> roleIds) throws UserException {
        User user = jwtUtils.getUserInfo();
        if(userId== user.getId()){
            return "不能操作自己的账号";
        }
        try {
            userService.changeUserRole(userId,roleIds);
        } catch (UserException e) {
            return e.getMessage();
        }
        return "true";
    }

    @ApiOperation("为用户移除角色")
    @DeleteMapping("/role/{userId}/{roleId}")
    public void removeRole(@PathVariable int userId,@PathVariable int roleId) throws UserException {
        userService.removeRole(userId,roleId);
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
    public String addCertificationInfo(@Valid @RequestBody Certification certification){
        try {
            certificationService.createCertification(certification);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "true";
    }
    @ApiOperation("批量添加认证信息")
    @PostMapping("/certifications")
    public String addCertificationsInfo(@RequestParam("file") MultipartFile file){
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            List<Certification> certifications = reader.readAll(Certification.class);
            certificationService.createCertifications(certifications);
        } catch (IOException e) {
            return e.getMessage();
        }
        return "true";
    }

    @ApiOperation("获取批量上传模板")
    @GetMapping("/certifications/template")
    public void getTemplate(HttpServletResponse response) throws IOException {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("name", "");
        row.put("num", "");
        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row);
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        //out为OutputStream，需要写出到的目标流

        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=template.xlsx");
        ServletOutputStream out=response.getOutputStream();

        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }



    @ApiOperation("分页获取所有认证信息")
    @GetMapping("/certifications")
    public DataTablesOutput<Certification> getCertifications(@Valid DataTablesInput input){
        input.getColumns().remove(input.getColumns().size()-1);
        input.getColumns().remove(input.getColumn("like"));
        return certificationService.getCertifications(input);
    }

    @ApiOperation("删除认证信息")
    @DeleteMapping("/certification/{id}")
    public String disableCertification(@PathVariable int id) throws CertificationException {
        try {
            certificationService.deleteCertification(id);
        } catch (CertificationException e) {
            return e.getMessage();
        }
        return "true";
    }



}

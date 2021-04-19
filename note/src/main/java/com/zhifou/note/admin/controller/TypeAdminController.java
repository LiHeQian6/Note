package com.zhifou.note.admin.controller;

import com.zhifou.note.exception.TypeException;
import com.zhifou.note.note.entity.Type;
import com.zhifou.note.note.service.TypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author : li
 * @Date: 2021-03-10 17:44
 */
@RestController
@Validated
@RequestMapping("/admin")
public class TypeAdminController {
    @Resource
    private TypeService typeService;


    @ApiOperation("新增类型")
    @PostMapping("/type")
    public void createType(@Valid Type type) throws TypeException {
        typeService.addType(type);
    }
    @ApiOperation("禁用类型")
    @DeleteMapping("/type/{id}")
    public void deleteType(@PathVariable int id) throws TypeException {
        typeService.disableType(id);
    }

    @ApiOperation("修改类型")
    @PutMapping("/type")
    public void changeType(Type type) throws TypeException {
        typeService.updateType(type);
    }

}

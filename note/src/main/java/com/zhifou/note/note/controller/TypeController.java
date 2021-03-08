package com.zhifou.note.note.controller;

import com.zhifou.note.note.entity.Type;
import com.zhifou.note.note.service.TypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 22:12
 */
@RestController
public class TypeController {

    @Resource
    private TypeService typeService;

//    @ApiOperation("新增类型")
//    @PostMapping("/type")//添加到管理员访问里
//    public void publishComment(@Valid @RequestBody @ApiParam("只需要传content,note.id") Comment comment) throws NoteException {
//
//    }

    @ApiOperation("获取所有笔记类型")
    @GetMapping("/types")
    public Set<Type> getTypes(){
        return typeService.getTypes();
    }


}

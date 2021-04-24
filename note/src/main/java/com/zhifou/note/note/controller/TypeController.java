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


    @ApiOperation("获取所有笔记类型")
    @GetMapping("/types")
    public Set<Type> getTypes(){
        return typeService.getTypes();
    }


}

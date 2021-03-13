package com.zhifou.note.note.controller;

import com.zhifou.note.exception.TagException;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.service.TagService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 22:12
 */
@RestController
public class TagController {//todo 添加查询分页
    @Resource
    private TagService tagService;


    @ApiOperation("获取所有笔记标签")
    @GetMapping("/tags")
    public Set<Tag> getTags(int current,int pageSize){
        return tagService.getAllTags();
    }

    @ApiOperation("创建一个标签")
    @PostMapping("/tag")
    public void createTag(@Valid @RequestBody Tag tag) throws TagException {
        tagService.createTag(tag);
    }

}

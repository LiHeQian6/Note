package com.zhifou.note.admin.controller;


import com.zhifou.note.exception.TagException;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.service.TagService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-03-10 17:44
 */
@Controller
@Validated
@RequestMapping("/admin")
public class TagAdminController {
    @Resource
    private TagService tagService;

    @ApiOperation("禁用标签")
    @DeleteMapping("/tag/{id}")
    public void disableTag(@PathVariable int id) throws TagException {
        tagService.disableTag(id);
    }

    @ApiOperation("修改标签")
    @PutMapping("/tag")
    public void changeTag(@RequestBody Tag tag) throws TagException {
            tagService.updateTag(tag);
    }

}

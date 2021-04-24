package com.zhifou.note.admin.controller;


import com.zhifou.note.exception.TagException;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.service.TagService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
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
public class TagAdminController {
    @Resource
    private TagService tagService;

    @ApiOperation("禁用标签")
    @DeleteMapping("/tag/disable/{id}")
    public void disableTag(@PathVariable int id) throws TagException {
        tagService.disableTag(id);
    }

    @ApiOperation("修改标签")
    @PutMapping("/tag")
    public void changeTag(@RequestBody Tag tag) throws TagException {
            tagService.updateTag(tag);
    }

    @ApiOperation("分页获取所有标签")
    @GetMapping("/tags")
    public DataTablesOutput<Tag> getTags(@Valid DataTablesInput input){
        input.getColumns().remove(input.getColumns().size()-1);
        return tagService.getTags(input);
    }

    @ApiOperation("删除标签")
    @DeleteMapping("/tag/{id}")
    public String deleteTag(@PathVariable int id) {
        try {
            tagService.deleteTag(id);
        } catch (TagException e) {
            return e.getMessage();
        }
        return "true";
    }

}

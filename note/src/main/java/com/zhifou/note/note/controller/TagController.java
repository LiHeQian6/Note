package com.zhifou.note.note.controller;

import com.zhifou.note.exception.TagException;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.service.TagService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * @author : li
 * @Date: 2021-03-01 22:12
 */
@RestController
@Validated
public class TagController {//todo 添加（友链表）,笔记价值评估和排行，笔记按热度查询，测试系统消息查询，系统通知查询
    @Resource
    private TagService tagService;


    @ApiOperation("获取所有笔记标签")
    @GetMapping("/tags")
    public Page<Tag> getTags(@ApiParam("第几页") @Min(value = 0,message = "页数最小为0") int page,
                             @ApiParam("页大小") @Min(value = 1,message = "页尺寸最小为1") int size){
        return tagService.getAllTags(page,size);
    }

    @ApiOperation("创建一个标签")
    @PostMapping("/tag")
    public void createTag(@Valid @RequestBody Tag tag) throws TagException {
        tagService.createTag(tag);
    }

}

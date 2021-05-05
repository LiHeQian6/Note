package com.zhifou.note.note.controller;

import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.note.service.SearchService;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.io.IOException;

/**
 * @author : li
 * @Date: 2021-05-05 08:02
 */
@RestController
public class SearchController {
    @Resource
    private SearchService searchService;

    @GetMapping("/search/import")
    public boolean importData() throws IOException {
        return searchService.importMYSQLData();
    }

    @GetMapping("/search")
    public Page<NoteVO> search(@RequestParam String keyword,
                               @ApiParam("第几页") @Min(value = 0, message = "页数最小为0") int page,
                               @ApiParam("页大小") @Min(value = 1, message = "页尺寸最小为1") int size) throws IOException {
        return searchService.searchNote(keyword,page, size);
    }




}

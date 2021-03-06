package com.zhifou.note.admin.controller;

import com.zhifou.note.admin.service.SystemLogService;
import com.zhifou.note.exception.CustomException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;

/**
 * @author : li
 * @Date: 2021-03-04 11:35
 */
@RestController
public class SystemLogController {
    @Resource
    private SystemLogService systemLogService;



    @ApiOperation("获得指定日期的日志总数和第一份或指定份日志")
    @GetMapping("/log/{date}/{num}")
    public HashMap<String,Object> readLog(@PathVariable @ApiParam("yyyy-MM-dd") String date, @PathVariable int num) throws FileNotFoundException, ParseException, CustomException {
        return systemLogService.getLog(date,num);
    }



}

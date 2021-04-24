package com.zhifou.note.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.zhifou.note.admin.service.SystemLogService;
import com.zhifou.note.exception.CustomException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;

/**
 * @author : li
 * @Date: 2021-03-04 11:35
 */
@Controller
@RequestMapping("/admin")
public class SystemLogController {
    @Resource
    private SystemLogService systemLogService;

    @GetMapping("/system-log/{date}/{num}")
    public String toSystemLog(Model model, @PathVariable(required = false) String date, @PathVariable(required = false) int num) throws FileNotFoundException, ParseException, CustomException {
        HashMap<String, Object> result;
        if (date.equals("today")){
            result = systemLogService.getLog(DateUtil.now().substring(0, 10), 1);
            model.addAttribute("logCount",result.get("logCount"));
            model.addAttribute("firstLog",result.get("firstLog"));
            model.addAttribute("date",DateUtil.now().substring(0,10));
        }else {
            result = systemLogService.getLog(date, num);
            model.addAttribute("logCount",result.get("logCount"));
            model.addAttribute("firstLog",result.get("firstLog"));
            model.addAttribute("date",date);
        }
        model.addAttribute("num",num);
        return "system-log";
    }




}

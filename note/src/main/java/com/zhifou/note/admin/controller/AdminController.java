package com.zhifou.note.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.zhifou.note.admin.service.SystemLogService;
import com.zhifou.note.exception.CustomException;
import com.zhifou.note.user.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @author : li
 * @Date: 2021-02-08 18:28
 */
@Api("管理系统api")
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private DataService dataService;

    @Resource
    private SystemLogService systemLogService;

    @GetMapping("/system-log/{date}/{num}")
    public String toSystemLog(Model model, @PathVariable(required = false) String date, @PathVariable(required = false) int num) throws FileNotFoundException, ParseException, CustomException {
        if (date.equals("today")){
            HashMap<String, Object> result = systemLogService.getLog(DateUtil.now().substring(0,10),1);
            model.addAttribute("logCount",result.get("logCount"));
            model.addAttribute("firstLog",result.get("firstLog"));
            model.addAttribute("date",DateUtil.now().substring(0,10));
            model.addAttribute("num",num);
        }else {
            HashMap<String, Object> result = systemLogService.getLog(date,num);
            model.addAttribute("logCount",result.get("logCount"));
            model.addAttribute("firstLog",result.get("firstLog"));
            model.addAttribute("date",date);
            model.addAttribute("num",num);
        }
        return "system-log";
    }


    @RequestMapping("/{page}")
    public String toPage(@PathVariable String page) {
        return page;
    }


    @ApiOperation("首页")
    @RequestMapping("/")
    public String index(HttpServletRequest request){
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        calendar.after(time);
        dataService.recordUV(request.getRemoteAddr());
        long l = dataService.calculateUV(time,calendar.getTime());
        request.setAttribute("uv",l);
        return "admin-index";
    }
}

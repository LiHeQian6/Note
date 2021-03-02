package com.zhifou.note.admin.controller;

import com.zhifou.note.user.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

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

package com.zhifou.note.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : li
 * @Date: 2021-02-08 18:28
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping("/{page}")
    public String toPage(@PathVariable String page) {
        return page;
    }
}

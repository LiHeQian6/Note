package com.zhifou.note.admin.service;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author : li
 * @Date: 2021-03-04 15:18
 */
@Service
@Transactional
public class SystemLogService {

    @Value("${spring.profiles.active}")
    private String action;


    public HashMap<String,Object> getLog(String date,int num) throws ParseException, FileNotFoundException, CustomException {
        if (--num<0) {
            throw new CustomException("参数num从1开始", Status.PARAM_NOT_VALID);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = dateFormat.parse(date);
        String now = DateUtil.now().substring(0,10);
        if (DateUtil.compare(d, new Date()) > 0) {
            throw new DateException("不能查看未来的日志");
        }
        String path = ResourceUtils.getURL("log").getPath();
        File[] ls = FileUtil.ls(path);
        HashMap<String, Object> result = new HashMap<>();
        int logCount=0;
        String content="";
        for (File file : ls) {
            String name = file.getName();
            FileReader reader = FileReader.create(file);
            if (name.contains(date)){
                logCount++;
                if (name.contains(date +"."+num)) {
                    content = reader.readString();
                    result.put("firstLog",content);
                }
            }

        }
        if (now.equals(date)){
            logCount++;
            if (result.get("firstLog")==null) {
                File file = FileUtil.file(path + "note-" + action + ".log");
                FileReader reader = FileReader.create(file);
                content=reader.readString();
                result.put("firstLog",content);
            }
        }
        result.put("logCount",logCount);
        return result;
    }
}

package com.zhifou.note.user.service;

import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.util.RedisKeyUtil;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class DataService {
    @Resource
    private NoteService noteService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    // 将指定的IP计入UV
    public void recordUV(String ip) {
        String redisKey = RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey, ip);
    }

    // 统计指定日期范围内的UV
    public long calculateUV(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 整理该日期范围内的key
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getUVKey(df.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(Calendar.DATE, 1);
        }

        // 合并这些数据
        String redisKey = RedisKeyUtil.getUVKey(df.format(start), df.format(end));
        for (String s : keyList) {
            redisTemplate.opsForHyperLogLog().union(redisKey, redisKey,s);
        }

        // 返回统计的结果
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    //获取当天的UV
    public long getTodayUA(){
        String date = df.format(Calendar.getInstance().getTime());
        String uaKey = RedisKeyUtil.getUVKey(date);
        return redisTemplate.opsForHyperLogLog().size(uaKey);
    }

    // 将指定用户计入DAU
    public void recordDAU(int userId) {
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey, userId, true);
    }

    // 统计指定日期范围内的DAU
    public long calculateDAU(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 整理该日期范围内的key
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE, 1);
        }

        // 进行OR运算
        return (long) redisTemplate.execute((RedisCallback) connection -> {
            String redisKey = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
            connection.bitOp(RedisStringCommands.BitOperation.OR,
                    redisKey.getBytes(), keyList.toArray(new byte[0][0]));
            return connection.bitCount(redisKey.getBytes());
        });
    }

    //获取7天的DAU
    public List<Long> getWeekDAU(){
        List<Long> weekDAU=new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-6);
        while (!calendar.getTime().after(Calendar.getInstance().getTime())) {
            String key = RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));
            long dau=redisTemplate.execute((RedisCallback<Long>)  conn -> conn.bitCount(key.getBytes()));
            weekDAU.add(dau);
            calendar.add(Calendar.DATE, 1);
        }
        return weekDAU;
    }

    //得到笔记数量前5的标签及笔记数量
    public HashMap<String, BigInteger> getTagNoteCount() {
        return noteService.getTagNoteCount();
    }


}

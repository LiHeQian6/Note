package com.zhifou.note.message.service;

import com.zhifou.note.util.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-03-03 17:55
 */
@Service
@Transactional
public class LookService {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * @param: ip
     * @param: noteId
     * @return void
     * @description 给笔记增加浏览量，按IP区分
     * @author li
     * @Date 2021/3/3 18:26
     */
    public void look(String ip,int noteId){
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                boolean isMember = redisTemplate.opsForSet().isMember(RedisKeyUtil.getNoteLookKey(noteId), ip);
                operations.multi();
                if (!isMember) {
                    redisTemplate.opsForSet().add(ip);
                }
                return operations.exec();
            }
        });
    }

    /**
     * @param: id
     * @return int
     * @description 获取笔记的浏览总量
     * @author li
     * @Date 2021/3/3 18:28
     */
    public long lookNum(int id){
        return redisTemplate.opsForSet().size(RedisKeyUtil.getNoteLookKey(id));
    }

}

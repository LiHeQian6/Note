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
 * @Date: 2021-03-02 20:18
 */

@Service
@Transactional
public class LikeService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * @param: userId
     * @param: entityType

     * @param: entityId
     * @param: entityUserId
     * @return void
     * @description 给笔记和评论点赞，同时增加用户被赞数
     * @author li
     * @Date 2021/3/3 15:49
     */
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

                operations.multi();

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }

    /**
     * @param: entityType
     * @param: entityId
     * @return long
     * @description 查询笔记或评论的总赞数
     * @author li
     * @Date 2021/3/3 15:51
     */
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * @param: userId
     * @param: entityType
     * @param: entityId
     * @return boolean
     * @description 查询用户对笔记或评论点赞状态
     * @author li
     * @Date 2021/3/3 15:53
     */
    public boolean findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId);
    }

    /**
     * @param: userId
     * @return int
     * @description 查询用户的总赞数
     * @author li
     * @Date 2021/3/3 15:57
     */
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }

}

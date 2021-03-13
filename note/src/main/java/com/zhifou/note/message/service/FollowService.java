package com.zhifou.note.message.service;


import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.UserVO;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import com.zhifou.note.util.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author : li
 * @Date: 2021-03-02 20:18
 */

@Service
@Transactional
public class FollowService implements Constant {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserDetailsServiceImp userService;

    /**
     * @param: userId
     * @param: entityId
     * @return void
     * @description 关注某个用户，同时把自己加到目标用户的粉丝中
     * @author li
     * @Date 2021/3/3 16:35
     */
    public void follow(int userId, int entityId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId);
                String followerKey = RedisKeyUtil.getFollowerKey(entityId);

                operations.multi();

                operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    /**
     * @param: userId
     * @param: entityId
     * @return void
     * @description 取消对某用户的关注，同时从该用户的粉丝中删除
     * @author li
     * @Date 2021/3/3 16:36
     */
    public void unfollow(int userId, int entityId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId);
                String followerKey = RedisKeyUtil.getFollowerKey(entityId);

                operations.multi();

                operations.opsForZSet().remove(followeeKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });
    }

    /**
     * @param: userId
     * @return long
     * @description 查询用户关注的数量
     * @author li
     * @Date 2021/3/3 16:54
     */
    public long getFolloweeCount(int userId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    /**
     * @param: entityType
     * @param: entityId
     * @return long
     * @description 查询用户粉丝数量
     * @author li
     * @Date 2021/3/3 16:54
     */
    public long getFollowerCount(int userId) {
        String followerKey = RedisKeyUtil.getFollowerKey(userId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    /**
     * @param: userId
     * @param: entityType
     * @param: entityId
     * @return boolean
     * @description 查询是否关注该用户
     * @author li
     * @Date 2021/3/3 16:56
     */
    public boolean hasFollowed(int userId, int targetId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId);
        return redisTemplate.opsForZSet().score(followeeKey, targetId) != null;
    }

    /**
     * @param: userId
     * @param: offset 开始的位置从0开始
     * @param: limit  查询总数
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @description 查询用户关注的人
     * @author li
     * @Date 2021/3/3 17:03
     */
    public List<Map<String, Object>> getFollowee(int userId, int offset, int limit) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);

        if (targetIds == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.loadUserById(targetId);
            UserVO userVO = new UserVO(user,true);
            map.put("user", userVO);
            Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }

    /**
     * @param: userId
     * @param: offset 开始的位置从0开始
     * @param: limit  查询总数
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @description 查询用户的粉丝
     * @author li
     * @Date 2021/3/3 17:03
     */
    public List<Map<String, Object>> getFollowers(int userId, int offset, int limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);

        if (targetIds == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.loadUserById(targetId);
            UserVO userVO = new UserVO(user,hasFollowed(userId,targetId));
            map.put("user", userVO);
            Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }

}

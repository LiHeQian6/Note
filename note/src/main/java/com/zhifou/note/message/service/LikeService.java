package com.zhifou.note.message.service;

import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.util.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-02 20:18
 */

@Service
@Transactional
public class LikeService implements Constant {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private NoteService noteService;

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
                String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
                String userLikedKey = RedisKeyUtil.getUserLikedKey(userId);

                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

                operations.multi();

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    if (entityType==ENTITY_TYPE_NOTE)
                        operations.opsForSet().remove(userLikedKey, entityId);
                    operations.opsForValue().decrement(userLikeKey);

                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    if (entityType==ENTITY_TYPE_NOTE)
                        operations.opsForZSet().add(userLikedKey, entityId,System.currentTimeMillis());
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


    public Set<NoteVO> getUserLikedNote(int userId,int offset,int limit) throws NoteException {
        Set<NoteVO> noteVOs = new HashSet<>();
        String userLikedKey = RedisKeyUtil.getUserLikedKey(userId);
        Set<Integer> notes = (Set<Integer>) redisTemplate.opsForZSet().reverseRange(userLikedKey, offset, offset + limit - 1);
        if (notes != null) {
            for (Integer noteId : notes) {
                Note note = noteService.getNote(noteId);
                NoteVO noteVO = new NoteVO(note);
                noteVOs.add(noteVO);
            }
        }
        return noteVOs;
    }

}

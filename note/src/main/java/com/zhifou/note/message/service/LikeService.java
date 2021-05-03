package com.zhifou.note.message.service;

import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.util.RedisKeyUtil;
import org.springframework.context.annotation.Lazy;
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
    @Lazy
    private NoteService noteService;

    /**
     * @param: userId 点赞者的id
     * @param: entityType 被点赞对象类型；笔记/评论
     * @param: entityId 被点赞对象id
     * @param: entityUserId 被点赞笔记的所属用户的id
     * @return void
     * @description 给笔记和评论点赞，同时增加被赞用户被赞数并记录自己的点赞历史
     * @author li
     * @Date 2021/3/3 15:49
     */
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                String userLikedKey = RedisKeyUtil.getUserLikedKey(userId);

                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

                operations.multi();

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    if (entityType==ENTITY_TYPE_NOTE)
                        operations.opsForZSet().remove(userLikedKey, entityId);
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


    /**
     * @param: userId
     * @param: page
     * @param: size
     * @return java.util.Set<com.zhifou.note.bean.NoteVO>
     * @description 获取用户赞过的笔记
     * @author li
     * @Date 2021/5/2 11:42
     */
    public Set<NoteVO> getUserLikedNotes(int userId, int page, int size) throws NoteException {
        int offset=page*size;
        Set<NoteVO> noteVOs = new HashSet<>();
        String userLikedKey = RedisKeyUtil.getUserLikedKey(userId);
        Set<Integer> notes = (Set<Integer>) redisTemplate.opsForZSet().reverseRange(userLikedKey, offset, offset + size - 1);
        if (notes != null) {
            for (Integer noteId : notes) {
                Note note = noteService.getNote(noteId);
                NoteVO noteVO = noteService.getNoteData(note);
                noteVOs.add(noteVO);
            }
        }
        return noteVOs;
    }

}

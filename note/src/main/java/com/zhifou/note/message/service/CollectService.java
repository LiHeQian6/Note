package com.zhifou.note.message.service;

import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.bean.UserVO;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.service.NoteService;
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
 * @Date: 2021-03-03 18:36
 */
@Service
@Transactional
public class CollectService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private NoteService noteService;
    @Resource
    private UserDetailsServiceImp userService;
    @Resource
    private FollowService followService;


    /**
     * @param: userId
     * @param: noteId
     * @return void
     * @description 收藏或取消收藏笔记
     * @author li
     * @Date 2021/3/3 19:08
     */
    public void collectNote(int userId, int noteId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                boolean isMember = operations.opsForZSet().score(RedisKeyUtil.getUserCollectKey(userId), noteId) != null;
                operations.multi();
                if (isMember) {
                    operations.opsForZSet().remove(RedisKeyUtil.getUserCollectKey(userId), noteId);
                    operations.opsForZSet().remove(RedisKeyUtil.getNoteCollectKey(noteId), userId);
                } else {
                    operations.opsForZSet().add(RedisKeyUtil.getUserCollectKey(userId), noteId,System.currentTimeMillis());
                    operations.opsForZSet().add(RedisKeyUtil.getNoteCollectKey(noteId), userId,System.currentTimeMillis());
                }

                return operations.exec();
            }
        });
    }

    /**
     * @param:
     * @return long
     * @description 获得笔记的收藏数量
     * @author li
     * @Date 2021/3/3 19:51
     */
    public long getNoteCollectCount(int noteId){
        return redisTemplate.opsForZSet().zCard(RedisKeyUtil.getNoteCollectKey(noteId));
    }

    /**
     * @param: userId
     * @param: entityType
     * @param: entityId
     * @return boolean
     * @description 查询是否收藏该笔记
     * @author li
     * @Date 2021/3/3 16:56
     */
    public boolean hasCollected(int userId, int noteId) {
        String collectKey = RedisKeyUtil.getUserCollectKey(userId);
        return redisTemplate.opsForZSet().score(collectKey, noteId) != null;
    }

    /**
     * @param: id
     * @param: offset
     * @param: limit
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @description 获取用户收藏的笔记
     * @author li
     * @Date 2021/3/3 20:05
     */
    public List<Map<String, Object>> getCollects(int userId, int page, int size) throws NoteException {
        int offset=(page-1)*size;
        String userCollectKey = RedisKeyUtil.getUserCollectKey(userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(userCollectKey, offset, offset + size - 1);

        if (targetIds == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            Note note = noteService.getNote(targetId);
            NoteVO noteVO = new NoteVO(note);
            map.put("note", noteVO);
            Double score = redisTemplate.opsForZSet().score(userCollectKey, targetId);
            map.put("collectTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }


    /**
     * @param: noteId
     * @param: offset
     * @param: limit
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @description 获得该笔记的收藏者
     * @author li
     * @Date 2021/3/3 21:35
     */
    public List<Map<String, Object>> getNoteCollect(int noteId, int page, int size) throws NoteException, UserException {
        int offset=(page-1)*size;
        String noteCollectKey = RedisKeyUtil.getNoteCollectKey(noteId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(noteCollectKey, offset, offset + size - 1);

        if (targetIds == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            UserVO userVO = new UserVO(user,followService.hasFollowed(noteService.getNote(noteId).getUser().getId(),targetId));
            map.put("user", userVO);
            Double score = redisTemplate.opsForZSet().score(noteCollectKey, targetId);
            map.put("collectTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }
}

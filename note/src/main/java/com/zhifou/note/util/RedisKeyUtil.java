package com.zhifou.note.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_USER_LIKED = "user:liked";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_VERIFY_CODE = "verifyCode";
    private static final String PREFIX_TOKEN = "token";
//    private static final String PREFIX_FOLLOWERS_NOTES = "follower:note";//用户关注者发布的笔记
    private static final String PREFIX_UV = "uv";
    private static final String PREFIX_DAU = "dau";
    private static final String PREFIX_LOOK = "look";//笔记浏览
    private static final String PREFIX_USER_COLLECT = "collect:user";//用户的笔记列表
    private static final String PREFIX_NOTE_COLLECT = "collect:note";//笔记的收藏者列表

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId) //entity 指笔记和评论
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的笔记总赞数
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 某个用户关注的用户
    // followee:userId:entityType -> zset(entityId,now) //entity 用户
    public static String getFolloweeKey(int userId) {
        return PREFIX_FOLLOWEE + SPLIT + userId;
    }

    // 某个实体拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now) //entity 用户
    public static String getFollowerKey(int userId) {
        return PREFIX_FOLLOWER + SPLIT + userId;
    }

    // 验证码
    public static String getVerifyCodeKey(String owner) {
        return PREFIX_VERIFY_CODE + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTokenKey(String token) {
        return PREFIX_TOKEN + SPLIT + token;
    }

//    // 用户
//    public static String getUserKey(int userId) {
//        return PREFIX_USER + SPLIT + userId;
//    }

    // 单日UV
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    // 区间UV
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    // 单日活跃用户
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    // 区间活跃用户
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    //笔记的浏览量
    public static String getNoteLookKey(int noteId) {
        return PREFIX_LOOK+SPLIT+noteId;
    }

    //用户收藏列表
    public static String getUserCollectKey(int userId) {
        return PREFIX_USER_COLLECT+SPLIT+userId;
    }

    //收藏该笔记的用户列表
    public static String getNoteCollectKey(int noteId) {
        return PREFIX_NOTE_COLLECT+SPLIT+noteId;
    }

    //获取用户点赞历史
    public static String getUserLikedKey(int userId) {
        return PREFIX_USER_LIKED+SPLIT+userId;
    }

    //用户关注的人发布的笔记
//    public static String getUserFollowersNotesKey(int userId){
//        return PREFIX_FOLLOWERS_NOTES+SPLIT+userId;
//    }

}

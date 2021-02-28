package com.zhifou.note.bean;

public interface Constant {

    /**
     * 实体类型: 笔记
     */
    int ENTITY_TYPE_NOTE = 1;

    /**
     * 实体类型: 评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型: 用户
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * 主题: 评论
     */
    String TOPIC_COMMENT = "comment";

    /**
     * 主题: 点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 主题: 关注
     */
    String TOPIC_FOLLOW = "follow";
    /**
     * 主题: 收藏
     */
    String TOPIC_COLLECT = "collect";

    /**
     * 主题: 关注的人发布笔记
     */
    String TOPIC_FOLLOWER_PUBLISH = "follower_publish";

    /**
     * 系统用户ID
     */
    int SYSTEM_USER_ID = 1;



}

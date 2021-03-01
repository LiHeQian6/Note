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

    /**
     * 笔记类别初始化数据
     */
    String[] TYPE_INIT_DATA={"文史哲学","社会科学","理学","工学","经济管理","外语","艺术学"};

    String[][] CHILD_TYPE_INIT_DATA={
        {"文学","哲学","历史学","新闻传播","其他"},
        {"法学","政治学","社会学","心理学","教育学","军事","其他"},
        {"数学","物理学","化学","海洋学","医学","生物学","其他"},
        {"机械","计算机","化工","材料","环境工程","建筑","土木工程","其他"},
        {"经济学","管理学","其他"},
        {"英语","日语","德语","法语","其他"},
        {"美术","音乐","其他"}
    };

    /**
     * 笔记标签初始化数据
     */
    String[] TAG_INIT_DATA={"C语言","LINUX","CENTOS","PYTHON","NGINX","SHELL","神经网络","算法","机器学习","人工智能"};


}

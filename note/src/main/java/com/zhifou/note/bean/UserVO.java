package com.zhifou.note.bean;

import com.zhifou.note.user.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : li
 * @Date: 2021-03-02 08:40
 */
@Getter
@Setter
public class UserVO  {
    private Integer id;
    private String username;
    private String introduction="还没有任何介绍哦！";
    private String nickName="一只笔记君";
    private String photo;
    private String certification;//实名认证，保存真实姓名
//    private List<Role> roles;//角色
    private long like;
    private boolean isFollow=false;
    private boolean enabled=false;
    private long followee;
    private long follower;
    public UserVO(User user,long like,boolean isFollow){
        id=user.getId();
        username=user.getUsername();
        introduction=user.getIntroduction();
        nickName=user.getNickName();
        photo=user.getPhoto();
        certification=user.getCertification()!=null?user.getCertification().getName():null;
        this.like=like;
        this.isFollow=isFollow;
        this.enabled=user.isEnabled();
    }
    public UserVO(User user,boolean isFollow){
        id=user.getId();
        username=user.getUsername();
        introduction=user.getIntroduction();
        nickName=user.getNickName();
        photo=user.getPhoto();
        certification=user.getCertification()!=null?user.getCertification().getName():null;
        this.isFollow=isFollow;
        this.enabled=user.isEnabled();
    }
    public UserVO(User user){
        id=user.getId();
        username=user.getUsername();
        introduction=user.getIntroduction();
        nickName=user.getNickName();
        photo=user.getPhoto();
        certification=user.getCertification()!=null?user.getCertification().getName():null;
        this.isFollow=false;
        this.enabled=user.isEnabled();
    }
}

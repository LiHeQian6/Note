package com.zhifou.note.user.service;

import com.zhifou.note.bean.Status;
import com.zhifou.note.bean.UserVO;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.message.service.FollowService;
import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.user.entity.Role;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.repository.RoleRepository;
import com.zhifou.note.user.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author : li
 * @Date: 2021-01-10 10:54
 */
@Service
@Transactional
public class UserDetailsServiceImp implements UserDetailsService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private RoleService roleService;
    @Resource
    @Lazy
    private FollowService followService;

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private LikeService likeService;

    public void registerUser(User user) throws UserException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (!userRepository.existsByUsername(user.getUsername())) {
            ArrayList<Role> roles = new ArrayList<>();
            Role role = roleRepository.findByName("ROLE_USER");
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }else {
            throw new UserException("账号已经存在！", Status.USER_ACCOUNT_ALREADY_EXIST);
        }
    }

    public void resetPassword(String username, String newPassword) throws UserException {
        User user = findUserByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public DataTablesOutput<User> getUsers(DataTablesInput input) {
        return userRepository.findAll(input);
    }

    public User findUserById(int userId) throws UserException {
        User user = userRepository.findUserById(userId);
        if (user==null) {
            throw new UserException("未找到该用户！",Status.USER_ACCOUNT_NOT_EXIST);
        }
        return user;
    }

    public User findUserByUsername(String username) throws UserException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UserException("未找到该用户！",Status.USER_ACCOUNT_NOT_EXIST);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new InternalAuthenticationServiceException("用户名或密码错误！");
        }
        if (!user.isEnabled()){
            throw new InternalAuthenticationServiceException("该账号已封停！");
        }
        return new User(user.getUsername(), user.getPassword(),user.getRoles(),user.getAuthorities());
    }

    public void updateUserInfo(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void disableUser(int id) throws UserException {
        User user = findUserById(id);
        if (user.isEnabled()) {
            user.setEnabled(false);
        }else {
            user.setEnabled(true);
        }
        userRepository.save(user);
    }

    public void changeUserRole(int userId, List<Integer> roleIds) throws UserException {
        User user = findUserById(userId);
        List<Role> roles = roleService.findRoles(roleIds);
        user.setRoles(roles);
    }

    public void addRole(int userId,int roleId) throws UserException {
        User user = findUserById(userId);
        Role role = roleRepository.getOne(roleId);
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    public void removeRole(int userId, int roleId) throws UserException {
        User user = findUserById(userId);
        Role role = roleRepository.getOne(roleId);
        user.getRoles().remove(role);
        userRepository.save(user);
    }


    public long getUserCount(){
        return userRepository.count();
    }

    public Page<UserVO> getUsersByPopular(int id,int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageRequest);
        ArrayList<UserVO> userVOList = new ArrayList<>();
        for (User user : users) {
            UserVO userVO = new UserVO(user);
            userVO.setFollow(id==0?false:followService.hasFollowed(id,user.getId()));
            userVO.setFollower(followService.getFollowerCount(user.getId()));
            userVO.setFollowee(followService.getFolloweeCount(user.getId()));
            userVO.setLike(likeService.findUserLikeCount(user.getId()));
            userVOList.add(userVO);
        }
        userVOList.sort(new Comparator<UserVO>() {
            @Override
            public int compare(UserVO o1, UserVO o2) {
                return (int) (o1.getLike()-o2.getLike());
            }
        });
        return new PageImpl<>(userVOList, pageRequest, users.getTotalElements());
    }
}

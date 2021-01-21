package com.zhifou.note.user.service;

import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.repository.RoleRepository;
import com.zhifou.note.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-01-10 10:54
 */
@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        return new User(user.getUsername(), user.getPassword(),user.getAuthorities());
    }
}

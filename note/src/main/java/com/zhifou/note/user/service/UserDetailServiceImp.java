package com.zhifou.note.user.service;

import com.zhifou.note.user.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

/**
 * @Author : li
 * @Date: 2021-01-10 10:54
 */
//@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(s);
        if (user==null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new User(s,user.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("normal"));
    }
}

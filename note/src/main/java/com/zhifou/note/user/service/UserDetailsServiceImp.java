package com.zhifou.note.user.service;

import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.user.entity.Role;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.repository.RoleRepository;
import com.zhifou.note.user.repository.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;

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
    private PasswordEncoder passwordEncoder;

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

    public User loadUserById(int userId) {
        return userRepository.findUserById(userId);
    }








    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new InternalAuthenticationServiceException("用户名或密码错误！");
        }
        return new User(user.getUsername(), user.getPassword(),user.getRoles(),user.getAuthorities());
    }
}

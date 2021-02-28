package com.zhifou.note;

import com.zhifou.note.user.entity.Role;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.repository.RoleRepository;
import com.zhifou.note.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author : li
 * @Date: 2021-02-27 22:17
 */
@SpringBootTest
public class SaveTest {
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;

    @Test
    public void saveUser(){
        ArrayList<Role> roles = new ArrayList<>();
        Role role = roleRepository.findByName("ROLE_USER");
        roles.add(role);
        User user = new User("1132808063@qq.com", "asdas1111d",roles, new ArrayList<>());
        user.getAuthorities();
        userRepository.save(user);
    }
}

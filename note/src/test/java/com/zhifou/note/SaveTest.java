package com.zhifou.note;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhifou.note.note.entity.Type;
import com.zhifou.note.user.repository.RoleRepository;
import com.zhifou.note.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashSet;

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
    public void saveUser() throws JsonProcessingException {
//        ArrayList<Role> roles = new ArrayList<>();
//        Role role = roleRepository.findByName("ROLE_USER");
//        roles.add(role);
//        User user = new User("1132808063@qq.com", "asdas1111d",roles, new ArrayList<>());
//        user.getAuthorities();
//        userRepository.save(user);
        Type type = new Type();
        Type typeChild1 = new Type();
        Type typeChild2 = new Type();
        HashSet<Type> children = new HashSet<>();
        children.add(typeChild1);
        children.add(typeChild2);
        type.setChild(children);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(type));
    }
}

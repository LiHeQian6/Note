package com.zhifou.note;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhifou.note.note.repository.NoteRepository;
import com.zhifou.note.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author : li
 * @Date: 2021-02-27 22:17
 */
@SpringBootTest
public class SaveTest {
    @Resource
    private UserRepository userRepository;
    @Resource
    private NoteRepository noteRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void saveUser() throws JsonProcessingException {
//        ArrayList<Role> roles = new ArrayList<>();
//        Role role = roleRepository.findByName("ROLE_USER");
//        roles.add(role);
//        User user = new User("1132808063@qq.com", "asdas1111d",roles, new ArrayList<>());
//        user.getAuthorities();
//        userRepository.save(user);
//        Type type = new Type();
//        Type typeChild1 = new Type();
//        Type typeChild2 = new Type();
//        HashSet<Type> children = new HashSet<>();
//        children.add(typeChild1);
//        children.add(typeChild2);
//        type.setChild(children);
//        ObjectMapper mapper = new ObjectMapper();
//        System.out.println(mapper.writeValueAsString(type));
        Query query = entityManager.createNativeQuery("select name,count(name) num from notes_tags left join tag t on t.id = notes_tags.tag_id group by name order by count(name) limit 5;");
        List resultList = query.getResultList();
    }
}

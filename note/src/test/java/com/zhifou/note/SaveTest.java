package com.zhifou.note;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhifou.note.bean.CommentVO;
import com.zhifou.note.note.repository.NoteRepository;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

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
    @Resource
    private ObjectMapper mapper;

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
//        Query query = entityManager.createNativeQuery("select name,count(name) num from notes_tags left join tag t on t.id = notes_tags.tag_id group by name order by count(name) limit 5;");
//        List resultList = query.getResultList();
//        CommentVO commentVO1 = new CommentVO();
//        commentVO1.setContent("a回复笔记");
//        CommentVO commentVO2 = new CommentVO();
//        commentVO2.setContent("b回复a");
//        CommentVO commentVO3 = new CommentVO();
//        commentVO3.setContent("c回复b");
//        CommentVO commentVO4 = new CommentVO();
//        commentVO4.setContent("d回复b");
//        HashSet<CommentVO> commentVOS1 = new HashSet<>();
//        HashSet<CommentVO> commentVOS2 = new HashSet<>();
//        HashSet<CommentVO> commentVOS3 = new HashSet<>();
//        commentVOS1.add(commentVO2);
//        commentVOS2.add(commentVO3);
//        commentVOS3.add(commentVO4);
//        commentVO1.setChild(commentVOS1);
//        commentVO2.setChild(commentVOS2);
//        commentVO3.setChild(commentVOS3);
//
//        for (CommentVO vo : commentVO1.getChild()) {
//            packageComment(vo);
//        }
//
//
//        System.out.println(mapper.writeValueAsString(commentVO1));

//        HashSet<CommentVO> commentVOS = new HashSet<>();
//        Set<CommentVO> size = commentVO1.getChild();
//        while (size !=null) {
//            for (CommentVO commentVO : size) {
//                size=commentVO.getChild();
//            }
//        }

//        Queue<CommentVO> commentVOS=new LinkedList<>();
//        commentVOS.offer();
//        while(){
//
//        }


        User user = userRepository.findUserById(1);

        user.setPassword(null);
        System.out.println(user);

    }

    public void packageComment(CommentVO comment){
        Set<CommentVO> child = comment.getChild();
        Set<CommentVO> comments = new HashSet<>();
        if (child !=null){
            for (CommentVO vo : child) {
                packageComment(vo);
                if (vo.getChild() != null) {
                    comments.addAll(vo.getChild());
                }
                vo.setChild(null);
                comments.add(vo);
            }
            comment.setChild(comments);
        }
    }
}

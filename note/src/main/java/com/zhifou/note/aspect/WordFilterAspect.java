package com.zhifou.note.aspect;


import com.zhifou.note.annotation.WordFilter;
import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.util.WordFilterUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: li
 * Date: 2020/4/17
 * Time: 13:31
 */
@Aspect
@Component
public class WordFilterAspect {

    @Resource
    private WordFilterUtil wordFilterUtil;


    @Pointcut("@annotation(com.zhifou.note.annotation.WordFilter)")
    public  void filterAspect() {}

    /**
     * @Author li
     * @param joinPoint
     * @return java.lang.Object
     * @Description 字符过滤
     * @Date 11:58 2020/4/17
     **/
    @Around("filterAspect()")
    public Object filter(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean escapeHtml = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(WordFilter.class).escapeHtml();
        String description = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(WordFilter.class).description();
        System.out.println("*****Filter Start*****");
        Object[] args = joinPoint.getArgs();
        Object result = null;
        if (description.equals("note")){
            Note note = (Note) args[0];
            note.setContent(wordFilterUtil.filter(note.getContent()));
            args[0] = note;
        }else if(description.equals("comment")){
            Comment comment = (Comment) args[0];
            comment.setContent(wordFilterUtil.filter(comment.getContent()));
            args[0] = comment;
        }else if (description.equals("tag")){
            Tag tag = (Tag) args[0];
            tag.setName(wordFilterUtil.filter(tag.getName()));
            args[0] = tag;
        }
        result = joinPoint.proceed(args);

        System.out.println("*****Filter End*****");
        return result;
    }
}

package com.zhifou.note.message.repository;

import com.zhifou.note.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : li
 * @Date: 2021-02-08 17:25
 */
@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {

}

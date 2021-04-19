package com.zhifou.note.message.repository;

import com.zhifou.note.message.entity.Message;
import com.zhifou.note.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author : li
 * @Date: 2021-02-08 17:25
 */
@Repository
public interface MessageRepository extends JpaRepository<Message,Integer>, DataTablesRepository<Message,Integer> {

    int countByToAndMsTypeAndCreateTimeAfter(User to,String msType, Date lastReadTime);

    int countByToAndCreateTimeAfter(User user, Date lastReadTime);

    Page<Message> findByToAndMsTypeAndCreateTimeAfterOrderByCreateTimeDesc(User userInfo, String type, Date lastReadTime, Pageable pageable);

    Message findMessageByIdAndStatus(int id, int i);

}

package com.zhifou.note.user.repository;

import com.zhifou.note.user.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : li
 * @Date: 2021-03-23 17:28
 */
public interface CertificationRepository extends JpaRepository<Certification,Integer> {

    Certification findByNameAndNum(String name,String num);


}

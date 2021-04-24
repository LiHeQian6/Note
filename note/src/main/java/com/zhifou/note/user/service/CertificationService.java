package com.zhifou.note.user.service;

import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.CertificationException;
import com.zhifou.note.user.entity.Certification;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.repository.CertificationRepository;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : li
 * @Date: 2021-03-23 17:30
 */
@Service
@Transactional
public class CertificationService {
    @Resource
    private CertificationRepository certificationRepository;


    public void certification(User user,Certification certification) throws CertificationException{
        Certification c = certificationRepository.findByNameAndNum(certification.getName(),certification.getNum());
        if (c==null) {
            throw new CertificationException("认证信息错误！", Status.NOT_FOUND_PRIVILEGE);
        }
        if (user.getCertification()!=null) {
            throw new CertificationException("用户已认证！", Status.CERTIFICATION_ALREADY_EXIST);
        }
        c.setUser(user);
        certificationRepository.save(c);
    }

    public void createCertification(Certification certification){
        certificationRepository.save(certification);
    }

    public Certification findCertificationById(int id) throws CertificationException {
        Certification certification = certificationRepository.findCertificationById(id);
        if (certification==null) {
            throw new CertificationException("认证信息不存在！",Status.NOT_FOUND_CERTIFICATION);
        }
        return certification;
    }




    public DataTablesOutput<Certification> getCertifications(DataTablesInput input) {
        return certificationRepository.findAll(input);
    }

    public void deleteCertification(int id) throws CertificationException {
        Certification certification = findCertificationById(id);
        certificationRepository.delete(certification);
    }

    public void createCertifications(List<Certification> certifications) {
        certificationRepository.saveAll(certifications);
    }
}

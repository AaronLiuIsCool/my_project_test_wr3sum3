package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.BasicInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicInfoRepository extends CrudRepository<BasicInfo, Long> {
    BasicInfo findByResumeId(String resumeId);
}

package com.kuaidaoresume.resume.service;

import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.model.Education;

import java.util.Collection;
import java.util.Optional;

public interface ResumeService {

    BasicInfo saveBasicInfo(String resumeId, BasicInfo basicInfo);

    Optional<BasicInfo> findBasicInfoByResumeId(String resumeId);

    Optional<BasicInfo> findBasicInfoById(Long id);

    Optional<Education> findEducationById(Long id);

    Collection<Education> findEducationsByResumeId(String resumeId);

    Education newEducation(String resumeId, Education education);

    Education saveEducation(Education education);

    Collection<Education> saveEducations(String resumeId, Iterable<Education> educations);

    void deleteEducationById(Long id);

    void deleteAllEducationsByResumeId(String resumeId);
}

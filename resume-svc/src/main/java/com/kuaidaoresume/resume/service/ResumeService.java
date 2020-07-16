package com.kuaidaoresume.resume.service;

import com.kuaidaoresume.resume.dto.BasicInfoDto;
import com.kuaidaoresume.resume.model.BasicInfo;

import java.util.Optional;

public interface ResumeService {

    BasicInfo saveBasicInfo(String resumeId, BasicInfoDto basicInfoDto);

    Optional<BasicInfo> findBasicInfoByResumeId(String resumeId);

    Optional<BasicInfo> findBasicInfoById(Long id);
}

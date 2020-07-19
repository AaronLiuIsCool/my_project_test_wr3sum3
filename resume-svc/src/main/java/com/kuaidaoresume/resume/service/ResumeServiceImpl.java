package com.kuaidaoresume.resume.service;

import com.kuaidaoresume.resume.dto.BasicInfoDto;
import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.repository.BasicInfoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private final BasicInfoRepository basicInfoRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public BasicInfo saveBasicInfo(String resumeId, BasicInfoDto basicInfoDto) {
        Optional<BasicInfo> basicInfoOptional = Optional.ofNullable(basicInfoRepository.findByResumeId(resumeId));
        BasicInfo basicInfo;
        if (!basicInfoOptional.isPresent()) {
            basicInfo = modelMapper.map(basicInfoDto, BasicInfo.class);
        } else {
            basicInfo = basicInfoOptional.get();
            modelMapper.map(basicInfoDto, basicInfo);
        }
        return basicInfoRepository.save(basicInfo);
    }

    @Override
    public Optional<BasicInfo> findBasicInfoByResumeId(String resumeId) {
        return Optional.ofNullable(basicInfoRepository.findByResumeId(resumeId));
    }

    @Override
    public Optional<BasicInfo> findBasicInfoById(Long id) {
        return basicInfoRepository.findById(id);
    }

}

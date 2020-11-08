package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.CertificateDto;
import com.kuaidaoresume.job.dto.EducationDto;
import com.kuaidaoresume.job.dto.ExperienceDto;
import com.kuaidaoresume.job.dto.TailorScoreDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ruichen on 23/10/20.
 */
@Service
public class TailorServiceImpl implements TailorService{
    /**
     * Actual service not implemented, this service is just gonna return default values for now
     */
    public TailorScoreDto education(List<EducationDto> educationDtos){
        return TailorScoreDto.builder()
                .category("DegreeScoreRule")
                .summaryZH("非常吻合")
                .summaryEN("100% match")
                .matchedDegree("硕士")
                .otherDegree("硕士, 专科")
                .score(12)
                .fullMark(12)
                .color(TailorScoreDto.Color.GREEN)
                .build();
    }

    public TailorScoreDto certificate(List<CertificateDto> certificateDtos){
        return TailorScoreDto.builder()
                .category("CertificateScoreRule")
                .summaryZH("CAA, CSA, IMBB, IPPA")
                .score(0)
                .fullMark(12)
                .color(TailorScoreDto.Color.RED)
                .build();
    }

    public TailorScoreDto experience(List<ExperienceDto> experienceDtos){
        return TailorScoreDto.builder()
                .category("ExperienceScoreRule")
                .summaryZH("4年相关经验")
                .requirementZH("岗位是高级类岗位，一般对工作经验年限要求是6-8年，您的工作经验年限不足，不是很吻合。")
                .score(0)
                .fullMark(12)
                .color(TailorScoreDto.Color.RED)
                .build();
    }
}

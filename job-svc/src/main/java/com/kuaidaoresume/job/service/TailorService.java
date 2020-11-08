package com.kuaidaoresume.job.service;


import com.kuaidaoresume.job.dto.ExperienceDto;
import com.kuaidaoresume.job.dto.TailorScoreDto;
import com.kuaidaoresume.job.dto.EducationDto;
import com.kuaidaoresume.job.dto.CertificateDto;
import java.util.List;

public interface TailorService {
    TailorScoreDto education(List<EducationDto> educationDtos);
    TailorScoreDto certificate(List<CertificateDto> certificateDtos);
    TailorScoreDto experience(List<ExperienceDto> experienceDtos);
}

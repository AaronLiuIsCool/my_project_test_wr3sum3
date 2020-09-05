package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.MajorDto;
import java.util.List;

public interface MajorExtractionService {
    public List<MajorDto> extract(String s);
}

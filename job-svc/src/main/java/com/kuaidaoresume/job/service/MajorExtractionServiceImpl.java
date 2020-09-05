package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.MajorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MajorExtractionServiceImpl implements MajorExtractionService {
    @Override
    public List<MajorDto> extract(String s) {
        //dummy value
        //TODO ruichen will train a simple naive bayse model to extract
        return Arrays.asList(MajorDto.builder()
                .name("Computer Science")
                .build());
    }
}

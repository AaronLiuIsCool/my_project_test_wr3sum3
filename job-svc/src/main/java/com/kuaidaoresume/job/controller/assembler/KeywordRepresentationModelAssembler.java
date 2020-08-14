package com.kuaidaoresume.job.controller.assembler;

import com.kuaidaoresume.job.controller.v1.KeywordController;
import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;

import com.kuaidaoresume.job.dto.PersistedKeywordDto;
import org.springframework.stereotype.Component;

@Component
public class KeywordRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedKeywordDto> {
    public KeywordRepresentationModelAssembler() {
        super(KeywordController.class);
    }
}
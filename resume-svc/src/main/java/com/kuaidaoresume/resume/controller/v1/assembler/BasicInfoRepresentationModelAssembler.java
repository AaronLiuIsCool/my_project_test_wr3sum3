package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.api.BasicInfoController;
import com.kuaidaoresume.resume.dto.PersistedBasicInfoDto;
import org.springframework.stereotype.Component;

@Component
public class BasicInfoRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedBasicInfoDto> {

    public BasicInfoRepresentationModelAssembler() {
        super(BasicInfoController.class);
    }
}

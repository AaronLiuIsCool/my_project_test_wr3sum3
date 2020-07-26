package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.api.VolunteerExperienceController;
import com.kuaidaoresume.resume.dto.PersistedVolunteerExperienceDto;
import org.springframework.stereotype.Component;

@Component
public class VolunteerExperienceRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedVolunteerExperienceDto> {
    public VolunteerExperienceRepresentationModelAssembler() {
        super(VolunteerExperienceController.class);
    }
}

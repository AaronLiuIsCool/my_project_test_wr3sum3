package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.resume.controller.v1.api.CertificateController;
import com.kuaidaoresume.resume.dto.PersistedCertificateDto;
import org.springframework.stereotype.Component;

@Component
public class CertificateRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedCertificateDto> {
    public CertificateRepresentationModelAssembler() {
        super(CertificateController.class);
    }
}

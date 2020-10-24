package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.config.JpaTestConfig;
import com.kuaidaoresume.resume.model.Certificate;
import com.kuaidaoresume.resume.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("ut")
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class
})
@Import(JpaTestConfig.class)
public class CertificateRepositoryTest {

    private static final LocalDate NOW = LocalDate.now();
    
    private static final Long CERTIFICATE_ID = 1L;
    private static final String NAME = "CPA";

    private static final LocalDate ISSUE_DATE = LocalDate.of(2000, 1, 1);
    private static final LocalDate EXPIRATION_DATE = LocalDate.of(2001, 1, 1);
    
    private Resume resume;
    private Certificate certificate;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @BeforeEach
    public void setup() {
        resume = resumeRepository.save(Resume.builder().language("en").build());

        certificate = Certificate.builder()
            .name(NAME)
            .issueDate(ISSUE_DATE)
            .expirationDate(EXPIRATION_DATE)
            .resume(resume)
            .build();

        certificateRepository.save(certificate);
    }

    @Test
    public void whenSaved_thenFindByResumeId() {
        Collection<Certificate> certificates = certificateRepository.findAllByResumeId(resume.getId());
        assertThat(certificates.size(), is(1));
    }

    @Test
    public void whenSaved_thenUpdate() {
        Certificate toUpdate = certificate;
        toUpdate.setIssueDate(NOW);
        Certificate updated = certificateRepository.save(toUpdate);
        assertThat(certificateRepository.findById(updated.getId()).get().getIssueDate(), is(NOW));
    }

    @Test
    public void whenSaveAll_thenAllSaved() {
        Certificate certificate1 = Certificate.builder()
            .name(NAME)
            .issueDate(ISSUE_DATE)
            .expirationDate(EXPIRATION_DATE)
            .resume(resume)
            .build();

        Certificate certificate2 = Certificate.builder()
            .name(NAME)
            .issueDate(ISSUE_DATE)
            .expirationDate(EXPIRATION_DATE)
            .resume(resume)
            .build();

        certificateRepository.saveAll(Arrays.asList(certificate1, certificate2));
        assertThat(certificateRepository.count(), is(3L));
    }

    @Test
    public void whenSaveAll_thenUpdateExisting() {
        Certificate certificate1 = Certificate.builder()
            .name(NAME)
            .issueDate(ISSUE_DATE)
            .expirationDate(EXPIRATION_DATE)
            .resume(resume)
            .build();

        certificate.setExpirationDate(NOW);
        certificateRepository.saveAll(Arrays.asList(certificate1, certificate));
        assertThat(certificateRepository.findById(certificate.getId()).get().getExpirationDate(), is(NOW));
    }

    @Test
    public void whenSaved_thenDelete() {
        Long id = certificate.getId();
        certificateRepository.deleteById(id);
        assertFalse(certificateRepository.findById(id).isPresent());
    }

    @Test
    public void whenSaved_thenDeleteAllByResumeId() {
        Certificate certificate1 = Certificate.builder()
            .id(CERTIFICATE_ID)
            .name(NAME)
            .issueDate(ISSUE_DATE)
            .expirationDate(EXPIRATION_DATE)
            .resume(resume)
            .build();
        certificateRepository.save(certificate1);
        certificateRepository.deleteAll(certificateRepository.findAllByResumeId(resume.getId()));
        assertThat(certificateRepository.findAllByResumeId(resume.getId()).size(), is(0));
    }
}

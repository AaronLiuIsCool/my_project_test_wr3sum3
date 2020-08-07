package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.resume.model.Resume;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.DEFAULT_MAX_CERTIFICATES_SCORE;

@Component
public class CertificatesScoreCalculator implements ScoreCalculationStrategy<Resume> {

    @Override
    public int calculateScore(Resume resume) {
        int numCertificates = resume.getCertificates().size();
        int maxCertificatesScore = ScoreCalculationStrategy.getScoreConfig("max.certificates.score",
            DEFAULT_MAX_CERTIFICATES_SCORE);
        return numCertificates > maxCertificatesScore ? maxCertificatesScore : numCertificates;
    }
}

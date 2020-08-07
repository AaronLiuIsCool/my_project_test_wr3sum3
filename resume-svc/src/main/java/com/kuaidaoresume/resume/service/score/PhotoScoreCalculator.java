package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.resume.model.Resume;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.DEFAULT_PHOTO_SCORE;

@Component
public class PhotoScoreCalculator implements ScoreCalculationStrategy<Resume> {

    @Override
    public int calculateScore(Resume resume) {
        int photoScore = ScoreCalculationStrategy.getScoreConfig("photo.score", DEFAULT_PHOTO_SCORE);
        return resume.getLanguage().equals("zh") && StringUtils.isEmpty(resume.getPhotoReference()) ? 0 : photoScore;
    }
}

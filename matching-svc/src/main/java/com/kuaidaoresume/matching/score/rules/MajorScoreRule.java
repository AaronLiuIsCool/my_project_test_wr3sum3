package com.kuaidaoresume.matching.score.rules;

import com.kuaidaoresume.matching.dto.ResumeJobScoreDto;
import com.kuaidaoresume.matching.dto.ResumeJobScoreDto.Color;
import com.kuaidaoresume.matching.model.Job;
import com.kuaidaoresume.matching.model.Resume;

import java.util.Properties;
import java.util.Set;
import java.util.HashSet;

public class MajorScoreRule implements IScoreRule {

    @Override
    public ResumeJobScoreDto score(Properties scoreProperties, Job job, Resume resume) {
        Set<String> jobMarjors = new HashSet<>(job.getRelevantMajors());

        Set<String> resumeMajors = new HashSet<>(resume.getMajors());

        double score;
        String summaryZH;
        double fullMark = Double.valueOf(scoreProperties.getProperty("major.score.fullMark"));
        Color color;

        if(jobMarjors.isEmpty()) {
            score = fullMark;
            color = Color.GREEN;
            summaryZH = scoreProperties.getProperty("major.summary.zh.green.tier1");
        }

        else if(resumeMajors.containsAll(jobMarjors)) {
            score = fullMark;
            color = Color.GREEN;
            summaryZH = scoreProperties.getProperty("major.summary.zh.green.tier2");
        }
        else {
            score = 0.0;
            color = Color.RED;
            summaryZH = scoreProperties.getProperty("major.summary.zh.red");
        }

        ResumeJobScoreDto resumeJobScoreDto = ResumeJobScoreDto.builder()
                .category(this.getClass().getSimpleName())
                .score(score)
                .fullMark(fullMark)
                .color(color)
                .summaryZH(summaryZH)
                .build();
        return resumeJobScoreDto;
    }
}
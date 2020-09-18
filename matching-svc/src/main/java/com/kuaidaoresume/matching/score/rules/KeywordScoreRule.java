package com.kuaidaoresume.matching.score.rules;

import com.kuaidaoresume.matching.dto.ResumeJobScoreDto;
import com.kuaidaoresume.matching.dto.ResumeJobScoreDto.Color;
import com.kuaidaoresume.matching.model.Job;
import com.kuaidaoresume.matching.model.Resume;

import java.util.*;
import java.util.stream.Collectors;

public class KeywordScoreRule implements IScoreRule {

    @Override
    public ResumeJobScoreDto score(Properties scoreProperties, Job job, Resume resume) {
        Set<String> jobKeywords = job.getKeywords().stream()
                .map(keyword -> keyword.getValue()).collect(Collectors.toSet());
        Set<String> resumeKeywords = new HashSet<>(resume.getKeywords());

        Set<String> resumeIncludedKeywords = new HashSet<>(jobKeywords); // use the copy constructor
        resumeIncludedKeywords.retainAll(resumeKeywords);

        Set<String> resumeNotcludedKeywords = new HashSet<>(jobKeywords);
        resumeNotcludedKeywords.removeAll(resumeKeywords);

        double fullMark = Double.valueOf(scoreProperties.getProperty("keyword.score.fullMark"));
        Map<String, Set<String>> extraInfo = new HashMap<>();
        extraInfo.put(scoreProperties.getProperty("keyword.score.extraInfo.resumeIncludedKeywords"), resumeIncludedKeywords);
        extraInfo.put(scoreProperties.getProperty("keyword.score.extraInfo.resumeNotcludedKeywords"), resumeNotcludedKeywords);
        double score = fullMark * resumeIncludedKeywords.size() / Math.max(15, jobKeywords.size());

        ResumeJobScoreDto resumeJobScoreDto = ResumeJobScoreDto.builder()
                .category(this.getClass().getSimpleName())
                .score(score)
                .fullMark(fullMark)
                .color(score > Double.valueOf(scoreProperties.getProperty("keyword.score.greenThreshold")) * fullMark ? Color.GREEN : Color.RED)
                .summaryZH(scoreProperties.getProperty("keyword.summary.zh"))
                .extraInfo(extraInfo)
                .build();
        return resumeJobScoreDto;
    }
}

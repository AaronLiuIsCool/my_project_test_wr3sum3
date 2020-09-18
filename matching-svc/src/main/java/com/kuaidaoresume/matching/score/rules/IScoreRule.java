package com.kuaidaoresume.matching.score.rules;

import com.kuaidaoresume.matching.model.Job;
import com.kuaidaoresume.matching.model.Resume;
import com.kuaidaoresume.matching.dto.ResumeJobScoreDto;

import java.util.Properties;

public interface IScoreRule {
    public ResumeJobScoreDto score(Properties scoreProperties, Job job, Resume resume);
}

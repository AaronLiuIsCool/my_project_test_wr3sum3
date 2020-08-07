package com.kuaidaoresume.resume.service;

import com.kuaidaoresume.resume.dto.ResumeScoreDto;
import com.kuaidaoresume.resume.model.*;

import java.util.Collection;
import java.util.Optional;

public interface ResumeService {

    Optional<Resume> findResumeById(String resumeId);

    Resume saveResume(Resume resume);

    <T extends ResumeContainable> Optional<T> findById(Long id, Class<? extends ResumeContainable> type);

    <T extends ResumeContainable> Optional<T> findByResumeId(String resumeId, Class<? extends ResumeContainable> type);

    <T extends ResumeContainable> Collection<T> findAllByResumeId(String resumeId, Class<? extends ResumeContainable> type);

    <T extends ResumeContainable> void save(T toSave, Class<? extends ResumeContainable> type);

    void deleteById(Long id, Class<? extends ResumeContainable> type);

    void deleteAllByResumeId(String resumeId, Class<? extends ResumeContainable> type);

    BasicInfo saveBasicInfo(String resumeId, BasicInfo basicInfoToSave);

    Education newEducation(String resumeId, Education education);

    Collection<Education> saveEducations(String resumeId, Iterable<Education> educations);

    WorkExperience newWorkExperience(String resumeId, WorkExperience workExperience);

    Collection<WorkExperience> saveWorkExperiences(String resumeId, Iterable<WorkExperience> workExperiences);

    ProjectExperience newProjectExperience(String resumeId, ProjectExperience projectExperience);

    Collection<ProjectExperience> saveProjectExperiences(String resumeId, Iterable<ProjectExperience> projectExperiences);

    VolunteerExperience newVolunteerExperience(String resumeId, VolunteerExperience volunteerExperience);

    Collection<VolunteerExperience> saveVolunteerExperiences(String resumeId, Iterable<VolunteerExperience> volunteerExperiences);

    Certificate newCertificate(String resumeId, Certificate certificate);

    Collection<Certificate> saveCertificates(String resumeId, Iterable<Certificate> certificates);

    void saveKeywords(Iterable<Keyword> keywords);

    Optional<ResumeScoreDto> getResumeScore(String resumeId);
}

package com.kuaidaoresume.resume.service;

import com.kuaidaoresume.resume.dto.ResumeMatchingDto;
import com.kuaidaoresume.resume.dto.ResumeRatingDto;
import com.kuaidaoresume.resume.dto.ResumeScoreDto;
import com.kuaidaoresume.resume.model.*;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Optional;

public interface ResumeService {

    Optional<Resume> findResumeById(String resumeId);

    Resume saveResume(Resume resume);

    <T extends ResumeContainable> Optional<T> findById(Long id, Class<? extends ResumeContainable> type);

    <T extends ResumeContainable> Optional<T> findByResumeId(String resumeId, Class<? extends ResumeContainable> type);

    <T extends ResumeContainable> Collection<T> findAllByResumeId(String resumeId, Class<? extends ResumeContainable> type);

    <T extends ResumeContainable> void updateResumeContainable(T toUpdate, Class<? extends ResumeContainable> type) throws EntityNotFoundException;

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

    Optional<ResumeScoreDto> getResumeScore(String resumeId);

    Optional<ResumeMatchingDto> getResumeMatching(String resumeId);

    Optional<ResumeRatingDto> getResumeRating(String resumeId);
}

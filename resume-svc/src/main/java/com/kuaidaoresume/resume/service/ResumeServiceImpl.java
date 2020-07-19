package com.kuaidaoresume.resume.service;

import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.model.Education;
import com.kuaidaoresume.resume.repository.BasicInfoRepository;
import com.kuaidaoresume.resume.repository.EducationRepository;
import com.kuaidaoresume.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private final ResumeRepository resumeRepository;

    @Autowired
    private final BasicInfoRepository basicInfoRepository;

    @Autowired
    private final EducationRepository educationRepository;

    @Override
    public BasicInfo saveBasicInfo(String resumeId, BasicInfo basicInfo) {
        return resumeRepository.findById(resumeId).map(resume -> {
            resume.setBasicInfo(basicInfo);
            resumeRepository.save(resume);
            return basicInfo;
        }).orElseThrow(resumeNotFoundException(resumeId));
    }

    @Override
    public Optional<BasicInfo> findBasicInfoByResumeId(String resumeId) {
        return Optional.ofNullable(basicInfoRepository.findByResumeId(resumeId));
    }

    @Override
    public Optional<BasicInfo> findBasicInfoById(Long id) {
        return basicInfoRepository.findById(id);
    }

    @Override
    public Optional<Education> findEducationById(Long id) {
        return educationRepository.findById(id);
    }

    @Override
    public List<Education> findEducationsByResumeId(String resumeId) {
        return educationRepository.findAllByResumeId(resumeId);
    }

    @Override
    public Education newEducation(String resumeId, Education education) {
        return resumeRepository.findById(resumeId).map(resume -> {
            resume.getEducations().add(education);
            resumeRepository.save(resume);
            return education;
        }).orElseThrow(resumeNotFoundException(resumeId));
    }

    private Supplier<EntityNotFoundException> resumeNotFoundException(String resumeId) {
        return () -> new EntityNotFoundException(String.format("Resume Not Found with id %s", resumeId));
    }

    @Override
    public Education saveEducation(Education education) {
        return educationRepository.save(education);
    }

    @Override
    public Collection<Education> saveEducations(String resumeId, Iterable<Education> educations) {
        return resumeRepository.findById(resumeId).map(resume -> {
            Set<Education> existingEducations = new TreeSet<>((a, b) -> (int) (a.getId() - b.getId()));
            existingEducations.addAll(resume.getEducations());
            educations.forEach(education -> {
                education.setResume(resume);
                if(!existingEducations.add(education)) {
                    existingEducations.remove(education);
                    existingEducations.add(education);
                }
            });
            resume.setEducations(new ArrayList<>(existingEducations));
            resumeRepository.save(resume);
            return existingEducations;
        }).orElseThrow(resumeNotFoundException(resumeId));
    }

    @Override
    public void deleteEducationById(Long id) {
        educationRepository.deleteById(id);
    }

    @Override
    public void deleteAllEducationsByResumeId(String resumeId) {
        educationRepository.deleteAll(educationRepository.findAllByResumeId(resumeId));
    }

}

package com.kuaidaoresume.resume.service;

import com.kuaidaoresume.resume.dto.ResumeScoreDto;
import com.kuaidaoresume.resume.model.*;
import com.kuaidaoresume.resume.repository.KeywordRepository;
import com.kuaidaoresume.resume.repository.ResumeContainableRepository;
import com.kuaidaoresume.resume.repository.ResumeContainableRepositoryFactory;
import com.kuaidaoresume.resume.repository.ResumeRepository;
import com.kuaidaoresume.resume.service.score.ResumeScoreBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private final ResumeRepository resumeRepository;

    @Autowired
    private final ResumeContainableRepositoryFactory resumeContainableRepositoryFactory;

    @Autowired
    private final KeywordRepository keywordRepository;

    @Autowired
    private final ResumeScoreBuilder resumeScoreBuilder;

    @Override
    public BasicInfo saveBasicInfo(String resumeId, BasicInfo basicInfoToSave) {
        basicInfoToSave.getProfiles().stream().forEach(profile -> profile.setBasicInfo(basicInfoToSave));
        return saveResumeWithItemUpdate(
            (BiConsumer<BasicInfo, Resume>) (basicInfo, resume) -> resume.setBasicInfo(basicInfo)
        ).apply(resumeId, basicInfoToSave);
    }

    @Override
    public Education newEducation(String resumeId, Education educationToSave) {
        return saveResumeWithItemUpdate(
            (BiConsumer<Education, Resume>) (education, resume) ->
            resume.getEducations().add(education)).apply(resumeId, educationToSave);
    }

    @Override
    public Collection<Education> saveEducations(String resumeId, Iterable<Education> educations) {
        return saveResumeWithItemsUpdate(resume -> resume.getEducations(),
            (resume, updatedEducations) -> resume.setEducations(updatedEducations)
        ).apply(resumeId, educations);
    }

    @Override
    public WorkExperience newWorkExperience(String resumeId, WorkExperience workExperienceToSave) {
        return saveResumeWithItemUpdate(
            (BiConsumer<WorkExperience, Resume>) (workExperience, resume) -> resume.getWorkExperiences().add(workExperience)).apply(resumeId, workExperienceToSave);
    }

    @Override
    public Collection<WorkExperience> saveWorkExperiences(String resumeId, Iterable<WorkExperience> workExperiences) {
        return saveResumeWithItemsUpdate(resume -> resume.getWorkExperiences(),
            (resume, updatedWorkExperience) -> resume.setWorkExperiences(updatedWorkExperience)
        ).apply(resumeId, workExperiences);
    }

    @Override
    public ProjectExperience newProjectExperience(String resumeId, ProjectExperience projectExperienceToSave) {
        return saveResumeWithItemUpdate(
            (BiConsumer<ProjectExperience, Resume>) (projectExperience, resume) -> resume.getProjectExperiences().add(projectExperience)).apply(resumeId, projectExperienceToSave);
    }

    @Override
    public Collection<ProjectExperience> saveProjectExperiences(String resumeId, Iterable<ProjectExperience> projectExperiences) {
        return saveResumeWithItemsUpdate(resume -> resume.getProjectExperiences(),
            (resume, updatedExperience) -> resume.setProjectExperiences(updatedExperience)
        ).apply(resumeId, projectExperiences);
    }

    @Override
    public VolunteerExperience newVolunteerExperience(String resumeId, VolunteerExperience volunteerExperienceToSave) {
        return saveResumeWithItemUpdate(
            (BiConsumer<VolunteerExperience, Resume>) (volunteerExperience, resume) -> resume.getVolunteerExperiences().add(volunteerExperience)).apply(resumeId, volunteerExperienceToSave);
    }

    @Override
    public Collection<VolunteerExperience> saveVolunteerExperiences(String resumeId, Iterable<VolunteerExperience> volunteerExperiences) {
        return saveResumeWithItemsUpdate(resume -> resume.getVolunteerExperiences(),
            (resume, updatedExperience) -> resume.setVolunteerExperiences(updatedExperience)
        ).apply(resumeId, volunteerExperiences);
    }

    @Override
    public Certificate newCertificate(String resumeId, Certificate certificateToSave) {
        return saveResumeWithItemUpdate(
            (BiConsumer<Certificate, Resume>) (certificate, resume) -> resume.getCertificates().add(certificate)).apply(resumeId, certificateToSave);
    }

    @Override
    public Collection<Certificate> saveCertificates(String resumeId, Iterable<Certificate> certificates) {
        return saveResumeWithItemsUpdate(resume -> resume.getCertificates(),
            (resume, updatedCertificates) -> resume.setCertificates(updatedCertificates)
        ).apply(resumeId, certificates);
    }

    @Override
    public Optional<Resume> findResumeById(String resumeId) {
        return resumeRepository.findById(resumeId);
    }

    @Override
    public Resume saveResume(Resume resume) {
        return resumeRepository.save(resume);
    }

    @Override
    public <T extends ResumeContainable> Optional<T> findById(Long id, Class<? extends ResumeContainable> type) {
        return resumeContainableRepositoryFactory.getResumeContainableRepository(type).findById(id);
    }

    @Override
    public <T extends ResumeContainable> Optional<T> findByResumeId(String resumeId, Class<? extends ResumeContainable> type) {
        return resumeContainableRepositoryFactory.getResumeContainableRepository(type).findByResumeId(resumeId);
    }

    @Override
    public <T extends ResumeContainable> Collection<T> findAllByResumeId(String resumeId, Class<? extends ResumeContainable> type) {
        return resumeContainableRepositoryFactory.getResumeContainableRepository(type).findAllByResumeId(resumeId);
    }

    @Override
    public <T extends ResumeContainable> void save(T toSave, Class<? extends ResumeContainable> type) {
        resumeContainableRepositoryFactory.getResumeContainableRepository(type).save(toSave);
    }

    @Override
    public void deleteById(Long id, Class<? extends ResumeContainable> type) {
        resumeContainableRepositoryFactory.getResumeContainableRepository(type).deleteById(id);
    }

    @Override
    public void deleteAllByResumeId(String resumeId, Class<? extends ResumeContainable> type) {
        ResumeContainableRepository resumeContainableRepository = resumeContainableRepositoryFactory.getResumeContainableRepository(type);
        resumeContainableRepository.deleteAll(resumeContainableRepository.findAllByResumeId(resumeId));
    }

    @Override
    public void saveKeywords(Iterable<Keyword> keywords) {
        keywordRepository.saveAll(keywords);
    }

    @Override
    public Optional<ResumeScoreDto> getResumeScore(String resumeId) {
        return resumeRepository.findById(resumeId).map(resumeScoreBuilder::getResumeScoreDto);
    }

    private <T extends ResumeContainable> BiFunction<String, T, T> saveResumeWithItemUpdate(
        BiConsumer<T, Resume> resumeUpdater) {

        return (resumeId, item) -> resumeRepository.findById(resumeId).map(resume -> {
            item.setResume(resume);
            resumeUpdater.accept(item, resume);
            resumeRepository.save(resume);
            return item;
        }).orElseThrow(resumeNotFoundException(resumeId));
    }

    private <T extends ResumeContainable> BiFunction<String, Iterable<T>, Collection<T>> saveResumeWithItemsUpdate(
        Function<Resume, Collection<T>> itemsGetter,
        BiConsumer<Resume, Collection<T>> itemsSetter) {

        return (resumeId, itemsToUpdate) -> resumeRepository.findById(resumeId).map(
            ((Function<Iterable<T>, Function<Resume, Set<T>>>) items -> resume -> {
                Set<T> existingItems = new TreeSet<>((a, b) -> a.getId().compareTo(b.getId()));
                existingItems.addAll(itemsGetter.apply(resume));
                items.forEach(item -> {
                    item.setResume(resume);
                    if (!existingItems.add(item)) {
                        existingItems.remove(item);
                        existingItems.add(item);
                    }
                });
                Collection<T> updatedItems = new ArrayList<>(existingItems);
                itemsSetter.accept(resume, updatedItems);
                resumeRepository.save(resume);
                return existingItems;
            }).apply(itemsToUpdate)).orElseThrow(resumeNotFoundException(resumeId));
    }

    private Supplier<EntityNotFoundException> resumeNotFoundException(String resumeId) {
        return () -> new EntityNotFoundException(String.format("Resume Not Found with id %s", resumeId));
    }
}

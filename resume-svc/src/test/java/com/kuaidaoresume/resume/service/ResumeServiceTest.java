package com.kuaidaoresume.resume.service;

import com.kuaidaoresume.resume.model.Education;
import com.kuaidaoresume.resume.model.Resume;
import com.kuaidaoresume.resume.repository.ResumeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class ResumeServiceTest {

    private final String RESUME_ID = "ef5d82db-4675-43dc-b814-2dbee48d9df3";

    @InjectMocks
    private ResumeServiceImpl resumeService;

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private Resume resume;

    @Test
    public void whenSaveEducations_thenAllSaved() {
        Education existing = mock(Education.class);
        Education toAdd = mock(Education.class);
        Education toAdd1 = mock(Education.class);
        given(resume.getEducations()).willReturn(Arrays.asList(existing));
        given(resumeRepository.findById(RESUME_ID)).willReturn(Optional.of(resume));
        given(existing.getId()).willReturn(0L);
        given(toAdd.getId()).willReturn(1L);
        given(toAdd1.getId()).willReturn(2L);

        ArgumentCaptor<ArrayList> argument = ArgumentCaptor.forClass(ArrayList.class);

        resumeService.saveEducations(RESUME_ID, Arrays.asList(toAdd, toAdd1));

        verify(toAdd).setResume(resume);
        verify(toAdd1).setResume(resume);
        verify(resume).setEducations(argument.capture());
        verify(resumeRepository).save(resume);

        List<Education> savedEducations = argument.getValue();
        assertThat(savedEducations.size(), is(3));
        assertTrue(savedEducations.contains(existing));
        assertTrue(savedEducations.contains(toAdd));
        assertTrue(savedEducations.contains(toAdd1));
    }

    @Test
    public void whenSaveEducations_thenUpdateExisting() {
        Education existing = mock(Education.class);
        Education existing1 = mock(Education.class);
        Education toUpdate = mock(Education.class);
        Education toUpdate1 = mock(Education.class);

        given(existing.getId()).willReturn(0L);
        given(existing1.getId()).willReturn(1L);
        given(toUpdate.getId()).willReturn(0L);
        given(toUpdate1.getId()).willReturn(1L);

        given(resume.getEducations()).willReturn(Arrays.asList(existing, existing1));
        given(resumeRepository.findById(RESUME_ID)).willReturn(Optional.of(resume));

        ArgumentCaptor<ArrayList> argument = ArgumentCaptor.forClass(ArrayList.class);

        resumeService.saveEducations(RESUME_ID, Arrays.asList(toUpdate, toUpdate1));

        verify(toUpdate).setResume(resume);
        verify(toUpdate1).setResume(resume);
        verify(resume).setEducations(argument.capture());
        verify(resumeRepository).save(resume);

        List<Education> savedEducations = argument.getValue();
        assertThat(savedEducations.size(), is(2));
        assertTrue(savedEducations.contains(toUpdate));
        assertTrue(savedEducations.contains(toUpdate1));
    }

    @Test
    public void whenSaveEducations_thenUpdateExistingAndSaveNew() {
        Education existing = mock(Education.class);
        Education toUpdate = mock(Education.class);
        Education toAdd = mock(Education.class);

        given(existing.getId()).willReturn(0L);
        given(toUpdate.getId()).willReturn(0L);
        given(toAdd.getId()).willReturn(1L);

        given(resume.getEducations()).willReturn(Arrays.asList(existing));
        given(resumeRepository.findById(RESUME_ID)).willReturn(Optional.of(resume));

        ArgumentCaptor<ArrayList> argument = ArgumentCaptor.forClass(ArrayList.class);

        resumeService.saveEducations(RESUME_ID, Arrays.asList(toUpdate, toAdd));

        verify(toUpdate).setResume(resume);
        verify(toAdd).setResume(resume);
        verify(resume).setEducations(argument.capture());
        verify(resumeRepository).save(resume);

        List<Education> savedEducations = argument.getValue();
        assertThat(savedEducations.size(), is(2));
        assertTrue(savedEducations.contains(toUpdate));
        assertTrue(savedEducations.contains(toAdd));
    }
}
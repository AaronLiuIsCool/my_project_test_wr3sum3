package com.kuaidaoresume.resume.controller.v1.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.resume.config.ResumeApplicationTestConfig;
import com.kuaidaoresume.resume.controller.v1.assembler.BasicInfoRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.BasicInfoDto;
import com.kuaidaoresume.resume.dto.PersistedBasicInfoDto;
import com.kuaidaoresume.resume.dto.ProfileDto;
import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.model.Profile;
import com.kuaidaoresume.resume.model.Resume;
import com.kuaidaoresume.resume.service.ResumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BasicInfoController.class)
@Import({ BasicInfoRepresentationModelAssembler.class, ResumeApplicationTestConfig.class })
public class BasicInfoControllerTest {

    private static final String RESUME_ID = "aUUID";
    private static final String FULL_NAME = "fullName";
    private static final Long BASIC_INFO_ID = 1L;
    private static final String ALIAS = "alias";
    private static final String COUNTRY = "Canada";
    private static final String PROVINCE = "province";
    private static final String CITY = "city";
    private static final String EMAIL = "test@email.com";
    private static final String PHONE_NUMBER = "alias";
    private static final String PROFILE_URL= "profileUrl";
    private static final Profile.ProfileType PROFILE_TYPE = Profile.ProfileType.LINKEDIN;

    private Profile profile;

    private ProfileDto profileDto;

    private BasicInfo basicInfo;

    private BasicInfoDto basicInfoDto;

    private PersistedBasicInfoDto persistedBasicInfoDto;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private ResumeService resumeService;

    @BeforeEach
    public void setup() {
        profileDto = ProfileDto.builder()
            .type(PROFILE_TYPE)
            .url(PROFILE_URL)
            .build();

        profile = Profile.builder()
            .type(PROFILE_TYPE)
            .url(PROFILE_URL)
            .build();

        basicInfoDto = BasicInfoDto.builder()
            .fullName(FULL_NAME)
            .alias(ALIAS)
            .country(COUNTRY)
            .province(PROVINCE)
            .city(CITY)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .profiles(Arrays.asList(profileDto))
            .build();

        persistedBasicInfoDto = modelMapper.map(basicInfoDto, PersistedBasicInfoDto.class);
        persistedBasicInfoDto.setId(BASIC_INFO_ID);

        basicInfo = BasicInfo.builder()
            .id(BASIC_INFO_ID)
            .fullName(FULL_NAME)
            .alias(ALIAS)
            .country(COUNTRY)
            .province(PROVINCE)
            .city(CITY)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .profiles(Arrays.asList(profile))
            .resume(Resume.builder().id(RESUME_ID).build())
            .build();
    }

    @Test
    public void whenFindById_thenReturn200() throws Exception {
        given(resumeService.findById(BASIC_INFO_ID, BasicInfo.class)).willReturn(Optional.of(basicInfo));

        mvc.perform(get("/v1/basic-infos/{id}", BASIC_INFO_ID).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andExpect(jsonPath("$.fullName", is(FULL_NAME)))
            .andExpect(jsonPath("$.alias", is(ALIAS)))
            .andExpect(jsonPath("$.country", is(COUNTRY)))
            .andExpect(jsonPath("$.province", is(PROVINCE)))
            .andExpect(jsonPath("$.city", is(CITY)))
            .andExpect(jsonPath("$.email", is(EMAIL)))
            .andExpect(jsonPath("$.phoneNumber", is(PHONE_NUMBER)))
            .andExpect(jsonPath("$.profiles[0].type", is(PROFILE_TYPE.name())))
            .andExpect(jsonPath("$.profiles[0].url", is(PROFILE_URL)))
            .andExpect(jsonPath("$.links[0].rel", is("self")))
            .andExpect(jsonPath("$.links[0].href", is(String.format("http://localhost/v1/basic-infos/%s", BASIC_INFO_ID))))
            .andReturn();
    }

    @Test
    public void whenFindByResumeId_thenReturn200() throws Exception {
        given(resumeService.findByResumeId(RESUME_ID, BasicInfo.class)).willReturn(Optional.of(basicInfo));

        mvc.perform(get("/v1/resumes/{resumeId}/basic-info", RESUME_ID).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andExpect(jsonPath("$.fullName", is(FULL_NAME)))
            .andExpect(jsonPath("$.alias", is(ALIAS)))
            .andExpect(jsonPath("$.country", is(COUNTRY)))
            .andExpect(jsonPath("$.province", is(PROVINCE)))
            .andExpect(jsonPath("$.city", is(CITY)))
            .andExpect(jsonPath("$.email", is(EMAIL)))
            .andExpect(jsonPath("$.phoneNumber", is(PHONE_NUMBER)))
            .andExpect(jsonPath("$.profiles[0].type", is(PROFILE_TYPE.name())))
            .andExpect(jsonPath("$.profiles[0].url", is(PROFILE_URL)))
            .andExpect(jsonPath("$.links[1].rel", is("self")))
            .andExpect(jsonPath("$.links[1].href", is(String.format("http://localhost/v1/resumes/%s/basic-info", RESUME_ID))))
            .andReturn();
    }

    @Test
    public void whenCreate_thenReturn201() throws Exception {
        given(resumeService.saveBasicInfo(eq(RESUME_ID), any(BasicInfo.class))).willReturn(basicInfo);

        mvc.perform(post("/v1/resumes/{resumeId}/basic-info", RESUME_ID)
            .content(objectMapper.writeValueAsString(basicInfoDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andReturn();
    }

    @Test
    public void whenSave_thenReturn200() throws Exception {
        given(resumeService.findByResumeId(RESUME_ID, BasicInfo.class)).willReturn(Optional.of(basicInfo));
        given(resumeService.saveBasicInfo(eq(RESUME_ID), any(BasicInfo.class))).willReturn(basicInfo);

        mvc.perform(put("/v1/resumes/{resumeId}/basic-info", RESUME_ID)
            .content(objectMapper.writeValueAsString(persistedBasicInfoDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }
}
package com.kuaidaoresume.resume.controller.v1.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.resume.controller.v1.assembler.BasicInfoRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.BasicInfoDto;
import com.kuaidaoresume.resume.dto.ProfileDto;
import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.model.Profile;
import com.kuaidaoresume.resume.model.Resume;
import com.kuaidaoresume.resume.service.ResumeService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ResumeController.class)
@Import({ BasicInfoRepresentationModelAssembler.class })
class ResumeControllerTest {

    private static final String RESUME_ID = "aUUID";
    private static final String FULL_NAME = "fullName";
    private static final long BASIC_INFO_ID = 1L;
    private static final String ALIAS = "alias";
    private static final String COUNTRY = "Canada";
    private static final String PROVINCE = "province";
    private static final String CITY = "city";
    private static final String EMAIL = "test@email.com";
    private static final String PHONE_NUMBER = "alias";
    private static final String PROFILE_URL= "profileUrl";
    private static final Profile.ProfileType PROFILE_TYPE = Profile.ProfileType.LINKEDIN;

    private static final Profile PROFILE = Profile.builder()
        .type(PROFILE_TYPE)
        .url(PROFILE_URL)
        .build();

    private static final ProfileDto PROFILE_DTO = ProfileDto.builder()
        .type(PROFILE_TYPE)
        .url(PROFILE_URL)
        .build();

    private static final BasicInfo BASIC_INFO = BasicInfo.builder()
        .id(BASIC_INFO_ID)
        .fullName(FULL_NAME)
        .alias(ALIAS)
        .country(COUNTRY)
        .province(PROVINCE)
        .city(CITY)
        .email(EMAIL)
        .phoneNumber(PHONE_NUMBER)
        .profiles(Arrays.asList(PROFILE))
        .resume(Resume.builder().id(RESUME_ID).build())
        .build();

    public static final BasicInfoDto BASIC_INFO_DTO = BasicInfoDto.builder()
        .fullName(FULL_NAME)
        .alias(ALIAS)
        .country(COUNTRY)
        .province(PROVINCE)
        .city(CITY)
        .email(EMAIL)
        .phoneNumber(PHONE_NUMBER)
        .profiles(Arrays.asList(PROFILE_DTO))
        .resumeId(RESUME_ID)
        .build();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResumeService resumeService;

    @Test
    void whenFindBasicInfo_thenReturn200() throws Exception {
        given(resumeService.findBasicInfoByResumeId(RESUME_ID)).willReturn(Optional.ofNullable(
                BASIC_INFO
        ));

        mvc.perform(get(String.format("/v1/resumes/%s/basicInfo", RESUME_ID)).accept(MediaTypes.HAL_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk()) //
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("$.fullName", is(FULL_NAME)))
            .andExpect(jsonPath("$.alias", is(ALIAS)))
            .andExpect(jsonPath("$.country", is(COUNTRY)))
            .andExpect(jsonPath("$.province", is(PROVINCE)))
            .andExpect(jsonPath("$.city", is(CITY)))
            .andExpect(jsonPath("$.email", is(EMAIL)))
            .andExpect(jsonPath("$.phoneNumber", is(PHONE_NUMBER)))
            .andExpect(jsonPath("$.profiles[0].type", is(PROFILE_TYPE.name())))
            .andExpect(jsonPath("$.profiles[0].url", is(PROFILE_URL)))
            .andExpect(jsonPath("$.resumeId", is(RESUME_ID)))
            .andExpect(jsonPath("$._links.resumes.href", is(String.format("http://localhost/v1/resumes/%s/basicInfo", RESUME_ID))))
            .andReturn();
    }

    @Test
    void whenCreateBasicInfo_thenReturn201() throws Exception {
        given(resumeService.saveBasicInfo(RESUME_ID, BASIC_INFO_DTO)).willReturn(BASIC_INFO);

        mvc.perform(post(String.format("/v1/resumes/%s/basicInfo", RESUME_ID))
            .content(objectMapper.writeValueAsString(BASIC_INFO_DTO))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
            .andReturn();
    }

    @Test
    void whenUpdateBasicInfo_thenReturn202() throws Exception {
        given(resumeService.saveBasicInfo(RESUME_ID, BASIC_INFO_DTO)).willReturn(BASIC_INFO);

        mvc.perform(put(String.format("/v1/resumes/%s/basicInfo", RESUME_ID))
            .content(objectMapper.writeValueAsString(BASIC_INFO_DTO))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }
}
package com.kuaidaoresume.resume.dto;

import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public class BasicInfoDtoTest {
    public static final Profile.ProfileType PROFILE_TYPE = Profile.ProfileType.LINKEDIN;
    private final long id = 1L;
    private final String fullName = "aFullName";
    private final String alias = "aAlias";
    private final String country = "aCountry";
    private final String province = "aProvince";
    private final String city = "aCity";
    private final String email = "test@email.com";
    private final String phoneNumber = "123-456-7890";
    private final String profileUrl = "example.com";

    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        modelMapper = new ModelMapper();
    }

    @Test
    public void whenConvertBasicInfoToBasicInfoDto_thenCorrect() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(Profile.builder()
                .id(id)
                .type(Profile.ProfileType.LINKEDIN)
                .url(profileUrl)
                .build()
        );

        BasicInfo basicInfo = BasicInfo.builder()
                .fullName(fullName)
                .alias(alias)
                .country(country)
                .province(province)
                .city(city)
                .email(email)
                .phoneNumber(phoneNumber)
                .profiles(profiles)
                .build();

        BasicInfoDto basicInfoDto = modelMapper.map(basicInfo, BasicInfoDto.class);
        assertNotNull(basicInfoDto);
        assertThat(basicInfoDto.getFullName(), is(fullName));
        assertThat(basicInfoDto.getAlias(), is(alias));
        assertThat(basicInfoDto.getCountry(), is(country));
        assertThat(basicInfoDto.getProvince(), is(province));
        assertThat(basicInfoDto.getCity(), is(city));
        assertThat(basicInfoDto.getProfiles().get(0), is(ProfileDto.builder().type(Profile.ProfileType.LINKEDIN).url(profileUrl).build()));
    }

    @Test
    public void whenConvertBasicInfoDtoToBasicInfo_thenCorrect() {
        List<ProfileDto> profiles = new ArrayList<>();
        profiles.add(ProfileDto.builder()
                .type(PROFILE_TYPE)
                .url(profileUrl)
                .build()
        );

        BasicInfoDto basicInfoDto = BasicInfoDto.builder()
                .fullName(fullName)
                .alias(alias)
                .country(country)
                .province(province)
                .city(city)
                .email(email)
                .phoneNumber(phoneNumber)
                .profiles(profiles)
                .build();

        BasicInfo basicInfo = modelMapper.map(basicInfoDto, BasicInfo.class);
        assertNotNull(basicInfo);
        assertThat(basicInfo.getFullName(), is(fullName));
        assertThat(basicInfo.getAlias(), is(alias));
        assertThat(basicInfo.getCountry(), is(country));
        assertThat(basicInfo.getProvince(), is(province));
        assertThat(basicInfo.getCity(), is(city));
        assertThat(basicInfo.getProfiles().size(), is(1));
    }
}
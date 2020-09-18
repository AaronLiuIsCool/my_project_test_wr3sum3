package com.kuaidaoresume.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.kuaidaoresume.account.client.AccountClient;
import com.kuaidaoresume.account.dto.AccountDto;
import com.kuaidaoresume.account.dto.GenericAccountResponse;
import com.kuaidaoresume.account.dto.UpdatePasswordRequest;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Sessions;
import com.kuaidaoresume.common.crypto.Sign;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.web.props.AppProps;
import com.kuaidaoresume.web.service.HelperService;
import com.kuaidaoresume.web.view.Constant;
import com.kuaidaoresume.web.view.PageFactory;

import javax.servlet.http.Cookie;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ConfirmResetControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountClient accountClient;

    @Autowired
    EnvConfig envConfig;

    @Autowired
    AppProps appProps;

    @Autowired
    PageFactory pageFactory;

    @Test
    public void testGetConfirmReset() throws Exception {
        String userId = UUID.randomUUID().toString();
        String email = "test@kuaidaoresume.com";
        String signingToken = appProps.getSigningSecret();
        String token = Sign.generateEmailConfirmationToken(userId, email, signingToken);
        // get request
        MvcResult mvcResult = mockMvc.perform(get("/reset/" + token))
                .andExpect(status().isOk())
                .andExpect(view().name(Constant.VIEW_CONFIRM_RESET))
                .andExpect(content().string(containsString(pageFactory.buildConfirmResetPage().getDescription())))
                // TODO TF-125 YS after add token to .andExpect(content().string(containsString(token)))
                .andReturn();
    }

    @Test
    public void testGetConfirmResetWrongToken() throws Exception {
        String userId = UUID.randomUUID().toString();
        String email = "test@kuaidaoresume.com";
        String signingToken = appProps.getSigningSecret();
        String token = Sign.generateEmailConfirmationToken(userId, email, signingToken);
        token += "wrong_token";
        // get request
        MvcResult mvcResult = mockMvc.perform(get("/reset/" + token))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + ResetController.PASSWORD_RESET_PATH))
                .andReturn();
    }

}


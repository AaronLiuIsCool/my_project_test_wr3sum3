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
import com.kuaidaoresume.account.dto.*;
import com.kuaidaoresume.common.api.ResultCode;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.web.service.HelperService;
import com.kuaidaoresume.web.view.Constant;
import com.kuaidaoresume.web.view.PageFactory;

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
public class LoginControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountClient accountClient;

    @Autowired
    EnvConfig envConfig;

    @Autowired
    PageFactory pageFactory;

    @Autowired
    LoginController loginController;

    @Test
    public void testAleadyLoggedIn() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/login")
            .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.AUTHORIZATION_AUTHENTICATED_USER))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:" +
                    HelperService.buildUrl("http", "app." + envConfig.getExternalApex())))
            .andReturn();
    }

    @Test
    public void testGetLoginInGeneral() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name(Constant.VIEW_LOGIN))
            .andExpect(content().string(containsString("我还没有账号")))
            .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testLoginFail() throws Exception {
        GenericAccountResponse genericAccountResponse = new GenericAccountResponse();
        genericAccountResponse.setCode(ResultCode.UN_AUTHORIZED);
        genericAccountResponse.setMessage("Incorrect password");
        when(accountClient.verifyPassword(anyString(), any(VerifyPasswordRequest.class)))
            .thenReturn(genericAccountResponse);

        String email = "test@kuaidaoresume.com";
        MvcResult mvcResult = mockMvc.perform(post("/login")
            .param("email", email)
            .param("password", "pass"))
            .andExpect(status().isOk())
            .andExpect(view().name(Constant.VIEW_LOGIN))
            .andExpect(content().string(containsString(email)))
            .andExpect(content().string(containsString("对不起，账户或者密码不对.")))
            .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testIsValidSub() {
        assertThat(loginController.isValidSub("http://account." + envConfig.getExternalApex() + "/test")).isTrue();
        assertThat(loginController.isValidSub("httpxxx://account." + envConfig.getExternalApex() + "/test")).isFalse();
        assertThat(loginController.isValidSub("http://accountx." + envConfig.getExternalApex() + "/test")).isFalse();
    }
}

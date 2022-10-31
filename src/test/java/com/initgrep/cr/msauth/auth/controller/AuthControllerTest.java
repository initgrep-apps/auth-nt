package com.initgrep.cr.msauth.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.initgrep.cr.msauth.auth.dto.LoginRequest;
import com.initgrep.cr.msauth.auth.dto.RefreshTokenRequest;
import com.initgrep.cr.msauth.auth.dto.RegisterRequest;
import com.initgrep.cr.msauth.auth.providers.converter.JwtToUserConverter;
import com.initgrep.cr.msauth.auth.service.AuthService;
import com.initgrep.cr.msauth.security.config.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.initgrep.cr.msauth.ControllerTestUtil.*;
import static com.initgrep.cr.msauth.auth.constants.AuthConstants.BEARER;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@ContextConfiguration(classes = {AuthController.class, WebSecurityConfig.class})
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtToUserConverter jwtToUserConverter;

    @MockBean
    private JwtDecoder jwtDecoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void test_register() throws Exception {
        var tokenResponse = getTokenResponse();
        var registerModel = getRegisterRequest();
        when(authService.register(any(RegisterRequest.class))).thenReturn(tokenResponse);
        MockHttpServletRequestBuilder postRequest = post(REGISTER_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerModel));
        ResultActions registerResultActions = mockMvc.perform(postRequest.with(csrf()));
        testTokenExpectations(registerResultActions);
    }

    @Test
    void test_register_forInvalidRequest() throws Exception {
        var tokenResponse = getTokenResponse();
        var registerModel = getRegisterRequest();
        registerModel.setPhoneNumber("");
        when(authService.register(any(RegisterRequest.class))).thenReturn(tokenResponse);
        MockHttpServletRequestBuilder postRequest = post(REGISTER_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerModel));
        ResultActions registerResultActions = mockMvc.perform(postRequest.with(csrf()));
        registerResultActions.andExpect(status().isBadRequest());
    }

    @Test
    void test_login() throws Exception {
        var tokenResponse = getTokenResponse();
        var loginModel = getLoginRequest();
        when(authService.login(any(LoginRequest.class))).thenReturn(tokenResponse);
        MockHttpServletRequestBuilder postRequest = post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginModel));
        ResultActions loginResultActions = mockMvc.perform(postRequest.with(csrf()));
        testTokenExpectations(loginResultActions);
    }

    @Test
    void test_login_forInvalidRequest() throws Exception {
        var tokenResponse = getTokenResponse();
        var loginModel = getLoginRequest();
        loginModel.setPhoneNumber("");
        when(authService.login(any(LoginRequest.class))).thenReturn(tokenResponse);
        MockHttpServletRequestBuilder postRequest = post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginModel));
        ResultActions loginResultActions = mockMvc.perform(postRequest.with(csrf()));
        loginResultActions.andExpect(status().isBadRequest());
    }

    @Test
    void test_token() throws Exception {
        var tokenResponse = getTokenResponse();
        var refreshTokenRequest = getRefreshTokenRequest();
        when(authService.getNewAccessToken(any(RefreshTokenRequest.class))).thenReturn(tokenResponse);
        MockHttpServletRequestBuilder postRequest = post(TOKEN_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tokenResponse));
        ResultActions loginResultActions = mockMvc.perform(postRequest.with(csrf()));
        testTokenExpectations(loginResultActions);
    }


    private void testTokenExpectations(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.type").value(BEARER))
                .andExpect(jsonPath("$.expiresIn").value(300));
    }
}
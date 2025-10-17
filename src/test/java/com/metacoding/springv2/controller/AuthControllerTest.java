package com.metacoding.springv2.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.auth.AuthRequest;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthControllerTest extends MyRestDoc {

        @Autowired
        private ObjectMapper om; // java <-> JSON 변환

        // 회원가입 성공
        @Test
        public void join_success_test() throws Exception {
                // given
                AuthRequest.JoinDTO joinDTO = new AuthRequest.JoinDTO("test", "1234", "test@nate.com");
                String requestBody = om.writeValueAsString(joinDTO);
        
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.post("/join")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody));
                // then
                result.andExpect(status().isOk()) // HTTP 상태코드 200
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.id").value(3))
                                .andExpect(jsonPath("$.body.username").value("test"))
                                .andExpect(jsonPath("$.body.email").value("test@nate.com"))
                                .andExpect(jsonPath("$.body.roles").value("USER"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }
        
        // 회원가입 실패
        @Test
        public void join_fail_test() throws Exception {
                // given
                AuthRequest.JoinDTO joinDTO = new AuthRequest.JoinDTO("test", "1234", "test");
                String requestBody = om.writeValueAsString(joinDTO);
        
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.post("/join")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody));
                // then
                result.andExpect(status().isBadRequest()) // HTTP 400
                                .andExpect(jsonPath("$.msg").value("email:이메일 형식이 올바르지 않습니다"))
                                .andExpect(jsonPath("$.body").isEmpty())
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 유저네임 중복체크 성공
        @Test
        public void checkUsername_success_test() throws Exception {
                // given
                String username = "test";
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.get("/check-username")
                                                .param("username", username));
                // then
                result.andExpect(status().isOk()) // HTTP 200
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.available").value(true))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }
}
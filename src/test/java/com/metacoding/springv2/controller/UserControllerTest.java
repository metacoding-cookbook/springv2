package com.metacoding.springv2.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.core.util.JwtUtil;
import com.metacoding.springv2.user.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest extends MyRestDoc {
    
        @Autowired
        private ObjectMapper om;

        private String accessToken;

        @BeforeEach // 각 메서드 실행 전 공통으로 실행됨
        void setUp() {
                // 테스트용 사용자 생성 및 JWT 토큰 생성
                User testUser = User.builder()
                                .id(1)
                                .username("ssar")
                                .password("1234")
                                .email("ssar@metacoding.com")
                                .roles("USER")
                                .build();
                accessToken = JwtUtil.create(testUser);
        }

        // 회원정보 조회 성공
        @Test
        public void getUser_success_test() throws Exception {
                // given
                Integer userId = 1;
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.get("/api/users/" + userId)
                                                .header("Authorization", accessToken));
                // then
                result.andExpect(status().isOk())
                                    .andExpect(jsonPath("$.status").value(200))
                                    .andExpect(jsonPath("$.msg").value("성공"))
                                    .andExpect(jsonPath("$.body.id").value(1))
                                    .andExpect(jsonPath("$.body.username").value("ssar"))
                                    .andExpect(jsonPath("$.body.email").value("ssar@metacoding.com"))
                                    .andExpect(jsonPath("$.body.roles").value("USER"))
                                    .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 회원정보 업데이트 성공
        @Test
        public void updateUser_success_test() throws Exception {
                // given
                UserRequest.UpdateDTO updateDTO = new UserRequest.UpdateDTO("test@metacoding.com", "12345");
                String requestBody = om.writeValueAsString(updateDTO);
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.put("/api/users")
                                                .header("Authorization", accessToken)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.id").value(1))
                                .andExpect(jsonPath("$.body.username").value("ssar"))
                                .andExpect(jsonPath("$.body.email").value("test@metacoding.com"))
                                .andExpect(jsonPath("$.body.roles").value("USER"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }
}
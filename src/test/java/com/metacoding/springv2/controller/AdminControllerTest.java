package com.metacoding.springv2.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.user.*;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.core.util.JwtUtil;
import org.springframework.http.MediaType;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AdminControllerTest extends MyRestDoc {

        @Autowired
        private ObjectMapper om;

        private String adminToken;

        @BeforeEach
        void setUp() {
                // 테스트용 사용자 생성 및 JWT 토큰 생성
                User testUser = User.builder()
                                .id(2)
                                .username("cos")
                                .password("1234")
                                .email("cos@metacoding.com")
                                .roles("ADMIN")
                                .build();
                adminToken = JwtUtil.create(testUser);
        }   

        // 관리자 게시글삭제 성공
        @Test
        public void deleteById_success_test() throws Exception {
                // given
                Integer boardId = 2;
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.delete("/api/admin/boards/" + boardId)
                                                .header("Authorization", adminToken));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body").isEmpty())
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 관리자 역할수정 성공
        @Test
        public void rolesUpdate_success_test() throws Exception {
               // given
               Integer userId = 1;
               UserRequest.RolesDTO rolesDTO = new UserRequest.RolesDTO("USER,ADMIN");
               String requestBody = om.writeValueAsString(rolesDTO);
               // when
               ResultActions result = mvc.perform(
                               MockMvcRequestBuilders.put("/api/admin/users/" + userId)
                                               .header("Authorization", adminToken)
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .content(requestBody));
               // then
               result.andExpect(status().isOk())
                               .andExpect(jsonPath("$.status").value(200))
                               .andExpect(jsonPath("$.msg").value("성공"))
                               .andExpect(jsonPath("$.body.id").value(1))
                               .andExpect(jsonPath("$.body.username").value("ssar"))
                               .andExpect(jsonPath("$.body.email").value("ssar@metacoding.com"))
                               .andExpect(jsonPath("$.body.roles").value("USER,ADMIN"))
                               .andDo(MockMvcResultHandlers.print()).andDo(document);
        }
}
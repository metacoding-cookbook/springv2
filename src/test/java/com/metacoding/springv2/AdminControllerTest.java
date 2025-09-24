package com.metacoding.springv2;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2.core.util.JwtUtil;
import com.metacoding.springv2.user.User;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AdminControllerTest extends MyRestDoc {

        private String userToken;
        private String adminToken;

        @BeforeEach
        void setUp() {
                // 테스트용 사용자 생성 및 JWT 토큰 생성
                User user = User.builder()
                                .id(1)
                                .username("ssar")
                                .password("1234")
                                .email("ssar@metacoding.com")
                                .roles("USER")
                                .build();
                userToken = JwtUtil.create(user);

                User user2 = User.builder()
                                .id(2)
                                .username("cos")
                                .password("1234")
                                .email("cos@metacoding.com")
                                .roles("ADMIN")
                                .build();
                adminToken = JwtUtil.create(user2);
        }

        // 관리자 게시글 삭제 성공
        @Test
        public void deleteById_success_test() throws Exception {
                // given
                Integer boardId = 1;

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

        // 관리자 게시글 삭제 실패
        @Test
        public void deleteById_fail_test() throws Exception {
                // given
                Integer boardId = 1;

                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.delete("/api/admin/boards/" + boardId)
                                                .header("Authorization", userToken));
                // then
                result.andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.status").value(403))
                                .andExpect(jsonPath("$.msg").value("권한이 없습니다"))
                                .andExpect(jsonPath("$.body").isEmpty())
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 관리자 게시글 역할 수정 성공
        @Test
        public void rolesUpdate_success_test() throws Exception {
                // given
                Integer userId = 1;
                // when
                ResultActions result = mvc.perform(
                        MockMvcRequestBuilders.put("/api/admin/users/" + userId)
                                        .header("Authorization", adminToken));

                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.id").value(1))
                                .andExpect(jsonPath("$.body.username").value("ssar"))
                                .andExpect(jsonPath("$.body.email").value("ssar@metacoding.com"))
                                .andExpect(jsonPath("$.body.roles").value("ADMIN"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }
}
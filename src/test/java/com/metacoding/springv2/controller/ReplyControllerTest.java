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
import com.metacoding.springv2.reply.ReplyRequest;
import com.metacoding.springv2.user.User;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReplyControllerTest extends MyRestDoc {
    
        @Autowired
        private ObjectMapper om;

        private String accessToken;

        @BeforeEach
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

        // 댓글쓰기 성공
        @Test
        public void save_success_test() throws Exception {
                // given
                ReplyRequest.SaveDTO saveDTO = new ReplyRequest.SaveDTO("test-comment", 1);
                String requestBody = om.writeValueAsString(saveDTO);
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.post("/api/replies")
                                                .header("Authorization", accessToken)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.id").value(6))
                                .andExpect(jsonPath("$.body.comment").value("test-comment"))
                                .andExpect(jsonPath("$.body.userId").value(1))
                                .andExpect(jsonPath("$.body.boardId").value(1))
                                .andExpect(jsonPath("$.body.username").value("ssar"))
                                .andExpect(jsonPath("$.body.isOwner").value(true))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 댓글삭제 성공
        @Test
        public void deleteById_success_test() throws Exception {
                // given
                Integer replyId = 4;
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.delete("/api/replies/" + replyId)
                                                .header("Authorization", accessToken));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body").isEmpty())
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }
}
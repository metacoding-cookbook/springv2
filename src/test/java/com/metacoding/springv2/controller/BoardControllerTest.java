package com.metacoding.springv2.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
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
import com.metacoding.springv2.board.BoardRequest;
import com.metacoding.springv2.core.util.JwtUtil;
import com.metacoding.springv2.user.User;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest extends MyRestDoc {
    
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

        // 게시글 목록 조회 성공
        @Test
        public void findAll_success_test() throws Exception {
                // given
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.get("/api/boards")
                                                .header("Authorization", accessToken));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body", hasSize(5)))
                                .andExpect(jsonPath("$.body[0].id").value(1))
                                .andExpect(jsonPath("$.body[0].title").value("title1"))
                                .andExpect(jsonPath("$.body[0].content").value("content1"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }   
        
        // 게시글쓰기 성공
        @Test
        public void save_success_test() throws Exception {
                // given
                BoardRequest.SaveDTO saveDTO = new BoardRequest.SaveDTO("test-title", "test-content");
                String requestBody = om.writeValueAsString(saveDTO);
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.post("/api/boards")
                                                .header("Authorization", accessToken)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.id").value(6))
                                .andExpect(jsonPath("$.body.title").value("test-title"))
                                .andExpect(jsonPath("$.body.content").value("test-content"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 게시글상세 성공
        @Test
        public void findById_success_test() throws Exception {
                // given
                Integer boardId = 5;
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.get("/api/boards/" + boardId)
                                                .header("Authorization", accessToken));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.boardId").value(5))
                                .andExpect(jsonPath("$.body.title").value("title5"))
                                .andExpect(jsonPath("$.body.content").value("content5"))
                                .andExpect(jsonPath("$.body.userId").value(2))
                                .andExpect(jsonPath("$.body.username").value("cos"))
                                .andExpect(jsonPath("$.body.isOwner").value(false))
                                .andExpect(jsonPath("$.body.replies", hasSize(2)))
                                .andExpect(jsonPath("$.body.replies[0].id").value(4))
                                .andExpect(jsonPath("$.body.replies[0].username").value("ssar"))
                                .andExpect(jsonPath("$.body.replies[0].comment").value("comment4"))
                                .andExpect(jsonPath("$.body.replies[0].isOwner").value(true))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 게시글수정 정보 성공
        @Test
        public void updateInfo_success_test() throws Exception {
                // given
                Integer boardId = 1;
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.get("/api/boards/" + boardId + "/edit")
                                                .header("Authorization", accessToken));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.id").value(1))
                                .andExpect(jsonPath("$.body.title").value("title1"))
                                .andExpect(jsonPath("$.body.content").value("content1"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 게시글수정 성공
        @Test
        public void update_success_test() throws Exception {
                // given
                Integer boardId = 1;
                BoardRequest.UpdateDTO updateDTO = new BoardRequest.UpdateDTO("updated-title", "updated-content");
                String requestBody = om.writeValueAsString(updateDTO);
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.put("/api/boards/" + boardId)
                                                .header("Authorization", accessToken)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.id").value(1))
                                .andExpect(jsonPath("$.body.title").value("updated-title"))
                                .andExpect(jsonPath("$.body.content").value("updated-content"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 게시글삭제 성공
        @Test
        public void deleteById_success_test() throws Exception {
                // given
                Integer boardId = 2;
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.delete("/api/boards/" + boardId)
                                                .header("Authorization", accessToken));
                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body").isEmpty())
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }
}
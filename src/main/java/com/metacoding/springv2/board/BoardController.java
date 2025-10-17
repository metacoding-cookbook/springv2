package com.metacoding.springv2.board;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import com.metacoding.springv2.core.util.Resp;
import com.metacoding.springv2.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/boards")
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        var responseDTO = boardService.게시글목록();
        return Resp.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<?> save(@AuthenticationPrincipal User sessionUser,
            @Valid @RequestBody BoardRequest.SaveDTO requestDTO, Errors errors) {
        var responseDTO = boardService.게시글쓰기(requestDTO, sessionUser);
        return Resp.ok(responseDTO);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@AuthenticationPrincipal User sessionUser,
            @PathVariable("boardId") Integer boardId) {
        var responseDTO = boardService.게시글상세(boardId, sessionUser.getId());
        return Resp.ok(responseDTO);
    }

    @GetMapping("/{boardId}/edit")
    public ResponseEntity<?> updateInfo(@AuthenticationPrincipal User sessionUser,
            @PathVariable("boardId") Integer boardId) {
        var responseDTO = boardService.게시글정보(boardId,sessionUser.getId());
        return Resp.ok(responseDTO);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User sessionUser, @PathVariable("boardId") Integer boardId,
            @Valid @RequestBody BoardRequest.UpdateDTO requestDTO, Errors errors) {
        var responseDTO = boardService.게시글수정(requestDTO, boardId, sessionUser.getId());
        return Resp.ok(responseDTO);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteById(@AuthenticationPrincipal User sessionUser,
            @PathVariable("boardId") Integer boardId) {
        boardService.게시글삭제(boardId, sessionUser.getId());
        return Resp.ok(null);
    }
}
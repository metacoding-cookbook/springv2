package com.metacoding.springv2.admin;

import com.metacoding.springv2.core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.Errors;
import jakarta.validation.Valid;
import com.metacoding.springv2.user.*;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {
    private final AdminService adminService;

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardId") Integer boardId) {
        adminService.관리자_게시글삭제(boardId);
        return Resp.ok(null);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<?> rolesUpdate(@PathVariable("userId") Integer userId,
            @Valid @RequestBody UserRequest.RolesDTO requestDTO, Errors errors) {
        var responseDTO = adminService.관리자_역할수정(userId,requestDTO);
        return Resp.ok(responseDTO);
    }
}
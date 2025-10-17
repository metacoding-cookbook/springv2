package com.metacoding.springv2.user;

import org.springframework.validation.Errors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.metacoding.springv2.core.util.Resp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal User sessionUser,
            @PathVariable("userId") Integer userId) {
        var responseDTO = userService.회원조회(userId, sessionUser.getId());
        return Resp.ok(responseDTO);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User sessionUser,
            @Valid @RequestBody UserRequest.UpdateDTO requestDTO, Errors errors) {
        var responseDTO = userService.회원수정(requestDTO, sessionUser.getId());
        return Resp.ok(responseDTO);
    }

}
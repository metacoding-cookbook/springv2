package com.metacoding.springv2.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import com.metacoding.springv2.core.util.Resp;
import com.metacoding.springv2.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody AuthRequest.JoinDTO requestDTO, Errors errors) {
        var responseDTO = userService.회원가입(requestDTO);
        return Resp.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest.LoginDTO requestDTO, Errors errors) {
        var responseDTO = userService.로그인(requestDTO);
        return Resp.ok(responseDTO);
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> getUsername(@RequestParam String username) {
        var responseDTO = userService.유저네임중복체크(username);
        return Resp.ok(responseDTO);
    }
}


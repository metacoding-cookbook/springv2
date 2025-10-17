package com.metacoding.springv2.auth;

import com.metacoding.springv2.user.User;
import jakarta.validation.constraints.*;

public class AuthRequest {
    public record JoinDTO(
        @Size(min = 4, max = 20, message = "유저네임은 4자 이상 20자 이하로 입력해주세요") @NotEmpty(message = "유저네임을 입력해주세요") String username,
        @NotBlank(message = "비밀번호를 입력해주세요") @Size(min = 4, max = 60, message = "비밀번호는 4~60자여야 합니다") String password,
        @Email(message = "이메일 형식이 올바르지 않습니다") String email) {
        public User toEntity(String encPassword) {
            return User.builder()
                    .username(username)
                    .password(encPassword)
                    .email(email)
                    .roles("USER")
                    .build();
        }
    }
    public record LoginDTO(
        @NotEmpty(message = "유저네임을 입력해주세요") String username,
        @NotBlank(message = "비밀번호를 입력해주세요") String password) {
    }
}
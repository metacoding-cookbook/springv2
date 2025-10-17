package com.metacoding.springv2.user;

import jakarta.validation.constraints.*;

public class UserRequest {
    public record UpdateDTO(
        @Email(message = "이메일 형식이 올바르지 않습니다") String email,
        @NotBlank(message = "비밀번호를 입력해주세요") @Size(min = 4, max = 60, message = "비밀번호는 4~60자여야 합니다") String password) {
    }

    public record RolesDTO(
        @NotEmpty(message = "역할을 설정해주세요") String roles){
    }
}
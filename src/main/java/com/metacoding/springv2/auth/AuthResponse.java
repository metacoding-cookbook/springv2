package com.metacoding.springv2.auth;

import com.metacoding.springv2.user.User;

public class AuthResponse {
    public record DTO(Integer id, String username, String email, String roles) {
        public DTO(User user) {
            this(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
        }
    }
}
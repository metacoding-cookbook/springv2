package com.metacoding.springv2.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import java.util.*;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_tb")
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @Column(unique = true, length = 20, nullable = false)
    private String username;
    @Column(length = 60, nullable = false)
    private String password;
    @Column(length = 30, nullable = false)
    private String email;
    private String roles = "USER"; // 디폴트값은 USER

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public User(Integer id, String username, String password, String email, String roles, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    public void update(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void updateRoles(String roles) {
        this.roles = roles;
    }

    // 스프링 시큐리티 권한 처리
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // roles는 단일값 USER 혹은 복수값 USER,ADMIN 형태로 존재함
        String[] roleList = roles.split(",");

        for (String role : roleList) {
            authorities.add(() -> "ROLE_" + role);
        }
        return authorities;
    }
}
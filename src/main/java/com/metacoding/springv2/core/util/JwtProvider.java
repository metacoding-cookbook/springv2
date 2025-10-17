package com.metacoding.springv2.core.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.metacoding.springv2.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    // 요청 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtUtil.HEADER);
        if (bearerToken != null && bearerToken.startsWith(JwtUtil.TOKEN_PREFIX)) {
            return bearerToken.replace(JwtUtil.TOKEN_PREFIX, "");
        }
        return null;
    }

    // 토큰을 검증하고 Authentication 반환
    public Authentication getAuthentication(String token) {
        try {
            User user = JwtUtil.verify(token); // JwtUtil에서 User 객체 생성
            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities());
        } catch (Exception e) {
            return null;
        }
    }

    // 토큰이 유효한지 단순 체크
    public boolean validateToken(String token) {
        try {
            JwtUtil.verify(token); // verify 중 예외 안 나면 유효
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
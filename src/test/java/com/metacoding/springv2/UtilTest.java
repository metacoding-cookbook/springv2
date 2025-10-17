package com.metacoding.springv2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.mock.web.MockHttpServletRequest;
import com.metacoding.springv2.core.util.*;
import com.metacoding.springv2.user.User;
import jakarta.servlet.http.HttpServletRequest;


@Import({JwtUtil.class, JwtProvider.class})  // import 추가
@DataJpaTest
public class UtilTest {

    @Autowired
    private JwtProvider jwtProvider;

    // user 객체 생성
    private User user = User.builder()
    .id(1)
    .username("ssar")
    .roles("ROLE_USER")
    .build();

     // 토큰 요청을 위한 데이터
    private HttpServletRequest httpServletRequest(User user) {
        String bearerToken = JwtUtil.create(user);  // 토큰 생성
        MockHttpServletRequest request = new MockHttpServletRequest(); // 요청 객체 생성
        request.addHeader(JwtUtil.HEADER, bearerToken ); // 요청 헤더에 토큰 추가
        return request;
    }
        
    @Test
    public void jwtUtil_test() {
        // Given
        String bearerJwt = JwtUtil.create(user);
        // When
        String jwt = bearerJwt.substring(JwtUtil.TOKEN_PREFIX.length()); // Bearer 제거
        User decodedToken = JwtUtil.verify(jwt);
        // Eye
        System.out.println("bearerJwt : " + bearerJwt);
        System.out.println("jwt : " + jwt);
        System.out.println("userID : " + decodedToken.getId());
        System.out.println("username: " + decodedToken.getUsername());
        System.out.println("roles: " + decodedToken.getRoles());     
    }

    @Test
    public void JwtProvider_test() {
        // Given
        HttpServletRequest request = httpServletRequest(user); // HTTP 요청 객체 생성
        // When
        String jwt = jwtProvider.resolveToken(request); // 요청에서 토큰 추출
        boolean valid = jwtProvider.validateToken(jwt); // 토큰 유효성 체크
        Authentication authentication = jwtProvider.getAuthentication(jwt); // 토큰 검증 및 Authentication 반환
        // Eye
        System.out.println("jwt : " + jwt);
        System.out.println("유효성 체크: " + valid);
        System.out.println("username : " + ((User) authentication.getPrincipal()).getUsername());
    }
}
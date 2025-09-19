package com.metacoding.springv2;

import org.junit.jupiter.api.Test;
import com.metacoding.springv2.core.util.JwtUtil;
import com.metacoding.springv2.user.User;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.metacoding.springv2.core.util.JwtProvider;
import org.springframework.security.core.Authentication;

@Import({JwtUtil.class, JwtProvider.class})
@DataJpaTest
public class UtilTest {

    @Autowired
    private JwtProvider jwtProvider = new JwtProvider();

    // user 객체 생성
    private User user = User.builder()
    .id(1)
    .username("ssar")
    .roles("ROLE_USER")
    .build();

    // 토큰 요청을 위한 데이터
    private HttpServletRequest httpServletRequest(User user) {
        String bearer = JwtUtil.create(user);  // 토큰 생성
        MockHttpServletRequest request = new MockHttpServletRequest(); // 요청 객체 생성
        request.addHeader(JwtUtil.HEADER, bearer); // 요청 헤더에 토큰 추가
        return request;
    }


    @Test
    public void JwtUtil_test() {
        // Given
        // When
        String bearerToken = JwtUtil.create(user);
        System.out.println("JWT : " + bearerToken);

        String rawJwt = bearerToken.substring(JwtUtil.TOKEN_PREFIX.length());
        User decoded = JwtUtil.verify(rawJwt);

        // Eye
        System.out.println("UserID : " + decoded.getId());
        System.out.println("User Name : " + decoded.getUsername());
        System.out.println("UserRoles : " + decoded.getRoles());
        
    }

    @Test
    public void JwtProvider_test() {
        // Given

        HttpServletRequest request = httpServletRequest(user);

        // When
        String jwt = jwtProvider.resolveToken(request); // Bearer 제거된 순수 JWT
        boolean valid = jwtProvider.validateToken(jwt); // 토큰 유효성 체크
        Authentication authentication = jwtProvider.getAuthentication(jwt); // 토큰 검증 및 Authentication 반환

        // Eye
        System.out.println("jwt : " + jwt);
        System.out.println("valid: " + valid);
        System.out.println("username : " + ((User) authentication.getPrincipal()).getUsername());
        System.out.println("authentication : " + authentication);
    }

}

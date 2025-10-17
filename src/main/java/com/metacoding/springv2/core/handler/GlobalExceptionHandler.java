package com.metacoding.springv2.core.handler;

import com.metacoding.springv2.core.handler.ex.*;
import com.metacoding.springv2.core.util.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> exApi400(Exception400 e) {
        log.warn("[WARN] 사용자 입력 유효성 실패: " + e.getMessage());
        return Resp.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> exApi401(Exception401 e) {
        log.warn("[WARN] 사용자 인증 실패: " + e.getMessage());
        return Resp.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> exApi403(Exception403 e) {
        log.warn("[WARN] 사용자 권한 실패: " + e.getMessage());
        return Resp.fail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<?> exApi404(Exception404 e) {
        log.warn("[WARN] 사용자 자원 찾기 실패: " + e.getMessage());
        return Resp.fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<?> exApi500(Exception500 e) {
        log.warn("[ERROR] 예상 가능한 서버 오류: " + e.getMessage());
        return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    // 해당 오류가 발생하면 직접 처리 혹은 Exception500으로 관리하는 것이 좋다.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exUnKnown(Exception e) {
        log.error("[SYSTEM] 예상 불가능한 서버 오류: " + e.getMessage());
        e.printStackTrace();
        return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의하세요");
    }
}
package com.example.advance.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component // 빈 등록
public class UserOwnerCheckInterceptor implements HandlerInterceptor {

    // 1. 조건이 부합하면 true 반환
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        // 1. 현재 로그인한 사용자 이름을 꺼내야합니다.
        // -> JWTFilter에서 넣어준 현재 로그인 사용자 꺼내기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();


        // 2. 요청한 URL에서 -> /api/user/{username}/email 에서 어떤 username을 넣어줬는지 추출하기
        String path = request.getRequestURI(); // api/user/김동현/email
        String decodePath = URLDecoder.decode(path, StandardCharsets.UTF_8);

        String[] parts = decodePath.split("/");
        String username = parts[parts.length - 2]; // 나중에 어떻게 셋팅 되진 모르겠지만 일단 현재는 이렇게 수정

        // 3. 로그인한 사용자와 이메일 변경을 하려는 사용자가 일치하는 검사를 해준다.

        // 4. 만약에 일치하면 변경해주면 되고 -> controller 접근을 허용하면 되고
        // 만약에 '현재' 사용자와 소유자가 다른 경우에는 아래 조건문 실행
        if(!currentUsername.equals(username)) { // 만약에 아니라면!
            log.info("소유자가 아닙니다. 접근 거부");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "소유자만 수정할 수 있습니다.");
            return false;
        }

        log.info("currentUsername='{}' length={}", currentUsername, currentUsername.length());
        log.info("pathUsername='{}' length={}", username, username.length());
        log.info(" 2번째 : interceptor controller 들어가기 전 마지막 권한 검사 실행");
        return true;
        // 5. 만약에 일치하지 않으면 -> controller 접근을 막아주면 된다.
        // 일치하는 경우 접근 허용

    }
}

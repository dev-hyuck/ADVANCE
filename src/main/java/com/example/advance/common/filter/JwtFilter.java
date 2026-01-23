package com.example.advance.common.filter;

import com.example.advance.common.enums.UserRoleEnum;
import com.example.advance.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(3)
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰을 발급 받는 로그인의 경우에는 토큰 검사를 하지 않아도 통과
        String requestURL = request.getRequestURI();

        // ' 로그인은 토큰 검사 제외 '
        if(requestURL.equals("/api/login")) {
            filterChain.doFilter(request,response);
            return;
        }

        // 너 토큰 있어? 없어?
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || authorizationHeader.isBlank()) {
            log.info("JWT 토큰이 필요합니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요 합니다.");
            return;

        }

        // 토큰이 있어? 그럼 그 토큰 유효해?
        // 여기까지 통과 했다면 토큰이 유효하다!
        String jwt = authorizationHeader.substring(7);
        // 만약에 jwtUtil이 유효하지 않다면 error를 던져 줘라.
        if(!jwtUtil.validateToken(jwt)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\" : \"Unauthorized\"}");

        }

        // 만약에 유효해 그럼 어떤 정보를 가지고 있어 ?
        String username = jwtUtil.extractUsername(jwt);

        String auth = jwtUtil.extractRole(jwt);

        UserRoleEnum userRoleEnum = UserRoleEnum.valueOf(auth); // --> UserRoleEnum
        // username을 꺼내긴 했는데 어떻게 사용하는게 좋을까?
        // 여기서 나오는게 바로 HttpServletRequest가 등장한다!

        // equest.setAttribute("username", username); -> 방식을 이제 바꿔 보자
        // Spring Security 방식에 맞게 바꿀거임
        User user = new User(username, "", List.of(userRoleEnum :: getRole));

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        filterChain.doFilter(request,response);


    }
}

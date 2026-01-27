package com.example.advance.user.Controller;

import com.example.advance.common.utils.JwtUtil;
import com.example.advance.user.model.request.LoginRequest;
import com.example.advance.user.model.request.UpdateUserEmailRequest;
import com.example.advance.user.model.response.LoginResponse;
import com.example.advance.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')") // ADMIN 권한이 있는 친구만 접속 가능하도록 하는 어노테이션
    @GetMapping("/get")
    public String getUserInfo(@AuthenticationPrincipal User user) {

        log.info(user.getUsername());
        return user.getUsername();
    }

    // 로그인 Request에서 id와 비밀번호를 받아 오면 그 username 기반으로 username key 안에 받아서 사용 하겠다.
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
//
//        String token = jwtUtil.generateToken(request.getUsername());
//
//        return ResponseEntity.ok(new LoginResponse(token));
//
//    } 사용하지 않는 옛날 방식!

    @GetMapping("/validate")
    public ResponseEntity<Boolean> checkValidate (HttpServletRequest request){
        // 헤더 안에 있는 Authorization 값을 꺼낼 것이다.
        String authorizationHeader = request.getHeader("Authorization");
        // 앞 글자 7자를 지우겠다.
        String jwt = authorizationHeader.substring(7);
        Boolean validate = jwtUtil.validateToken(jwt);
        return ResponseEntity.ok(validate);
    }

    @GetMapping("/username")
    public ResponseEntity<String> getUsername(HttpServletRequest request){

        String authorizationHeader = request.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return ResponseEntity.ok(username);
    }

    @PutMapping("/{username}/email")
    public ResponseEntity<String> updateEmail(
            @PathVariable String username,
            @RequestBody UpdateUserEmailRequest request
    ){
        userService.updateUserEmail(username, request.getEmail());
        return ResponseEntity.ok("수정 완료");
    }


}

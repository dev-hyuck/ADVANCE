package com.example.advance.user.Controller;

import com.example.advance.common.utils.JwtUtil;
import com.example.advance.user.model.request.LoginRequest;
import com.example.advance.user.model.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final JwtUtil jwtUtil;

    @GetMapping("/get")
    public String getUserInfo(){
        log.info("호출");
        return "호출되었음";
    }

    // 로그인 Request에서 id와 비밀번호를 받아 오면 그 username 기반으로 username key 안에 받아서 사용 하겠다.
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){

        String token = jwtUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(new LoginResponse(token));

    }

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
}

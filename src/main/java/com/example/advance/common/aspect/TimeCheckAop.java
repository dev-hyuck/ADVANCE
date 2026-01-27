package com.example.advance.common.aspect;

import com.example.advance.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeCheckAop {

    // 어떤것을 : UserService 하위의 메서드
    // 언제 : 메서드 실행 전후 수행
    // 어떻게 : executionTime 메서드 안에 내용을 실행 하겠다.

    // 메서드 실행 전후로 시간을 비교 분석하는 기능을 만들것임.

    @Around("execution(* com.example.advance.user.service.UserService.*(..))") // 대상을 지정해야 됩니다. 이거 좀 신기한 기능이네.
    public Object executionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        // 메서드 실행 전
        long start = System.currentTimeMillis();

        log.info(" 4번째 : 서비스 레이어 메서드 실행 전 AOP 로직 수행");

        Object result = joinPoint.proceed(); // 실제 메서드 실행 -> Filter의 doFilter라고 생각하면 된다.

        // 메서드 실행 후
        long end = System.currentTimeMillis();

        log.info("6번째 : 서비스 레이어 메서드 실행 후 AOP 로직 수행 ");

        log.info("[AOP] {} 실행됨 in {} ms", joinPoint.getSignature().getName(), end - start);
        return result;
        }

        // 로그인 인증/인가를 Filter에서 처리 ( 첫번째 )
        // Interceptor을 받아서 통과를 했다면
        // UserEmailUpdate 메서드는 AOP 수행이 될것입니다.

        // 요청이 들어옴 -> JwtFilter 통과함 -> OwnerCheckInterceptor
        // 유효하면 UserController 접근 ->
        // UserEmailUpdate -> 타켓 대상인 서비스 레이어의 메서드가 -> TimeCheckAop 실행 전 로직 수행
        // UserEmailUpdate 실행이 끝남 -> TimeCheckAop 실행후 로직 수행
        // Controller에서 return 값 넘겨줌
        // JwtFilter 통과해서 Postman으로 결과 전달.
    }
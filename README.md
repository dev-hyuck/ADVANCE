# 2026.01.21 Spring Advance 과정

---

## 🎯 학습 목표

### ✅ Filter 생성 과정과 흐름 이해하기
- ✅ Filter 생성 구조 이해
- ✅ 요청(Request) / 응답(Response) 흐름 파악
- ✅ OncePerRequestFilter 동작 원리 이해

---

### ✅ JWT(Json Web Token) 구조와 Spring Boot JWT 인증 구현 이해하기
- ✅ JWT 구조(Header / Payload / Signature) 이해
- ✅ 토큰 생성 과정 이해
- ✅ 토큰 검증 흐름 이해
- ✅ Spring Boot 기반 JWT 인증 흐름 이해

---

## 📝 작업 내용

- Filter 적용
  - OncePerRequestFilter 기반 필터 구현
  - 요청 진입 전 / 응답 반환 후 흐름 확인

- JWT 인증 방식 구현
  - SecretKey 초기화
  - Access Token 발급
  - 토큰 유효성 검증
  - Claims 정보 추출

- 로그인 구현
  - 로그인 성공 시 JWT 토큰 발급
  - Authorization Header를 통한 인증 처리

- Token 결과 인증
  - Bearer Token 방식 적용
  - Postman을 통한 인증 테스트 수행

---

## 🔗 관련 이슈

### 📦 JWT 라이브러리 버전

```gradle
implementation "io.jsonwebtoken:jjwt-api:0.12.5"
runtimeOnly  "io.jsonwebtoken:jjwt-impl:0.12.5"
runtimeOnly  "io.jsonwebtoken:jjwt-jackson:0.12.5" // JSON 직렬화

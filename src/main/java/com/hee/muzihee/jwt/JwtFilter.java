package com.hee.muzihee.jwt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hee.muzihee.exception.dto.ApiExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Client 의 요청을 가로채 header 의 token 이 유효 한지 검증
// <지히> JwtFilter를 쓰는 이유
// : UsernamePasswordAuthenticationFilter 실행 전에 customFilter(==JwtFilter)를 걸어서 인증 권한이 없는 오류를 처리하기 위함
// => 현재 사용자의 권한을 확인하는 것이 JwtFilter가 하는 일이 되는 거임!
public class JwtFilter extends OncePerRequestFilter {//OncePerRequestFilter 인터페이스를 구현하기 때문에 요청 받을 때 단 한번만 실행된다

    private static final Logger log = LoggerFactory.getLogger(OncePerRequestFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    // 실제 필터링 로직은 doFilterInternal 에 작성
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("[JwtFilter] doFilterInternal START ===================================");
        try {
            // 1. Request Header 에서 토큰을 꺼내오기 위해서 resolveToken 메소드 호출 (아래 정의해둠)
            String jwt = resolveToken(request);
            log.info("[JwtFilter] jwt : {}", jwt);

            // 2. 꺼내온 토큰을 validateToken 으로 토큰 유효성 검사
            // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장하기
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiExceptionDto errorResponse = new ApiExceptionDto(HttpStatus.UNAUTHORIZED, e.getMessage());


            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(convertObjectToJson(errorResponse)); // <지히> convertObjectToJson 에서 받아온 값을 단순 텍스트로 응답
            /* <지히> catch문 동작 과정
            * RuntimeException가 발생하면
            * 1. response 인코딩 설정
            * 2. errorResponse를 생성함
            * 3. 상태코드를 UNAUTHORIZED의 value로 설정
            * 4. convertObjectToJson를 이용해서 errorResponse를 string타입으로 변환해서 response에 담아줌
            *
            * =>  convertObjectToJson는 쉽게 생각해서 형변환해주는 메소드라고 생각하면 될 듯~ */
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper(); // <지히> ObjectMapper : JSON 형식을 사용할 때, 응답들을 직렬화하고 요청들을 역직렬화 할 때 사용하는 기술
        return mapper.writeValueAsString(object); // object를 string 타입으로 읽어옴
        // ObjectMapper 참고 사이트 : https://escapefromcoding.tistory.com/341
    }

    // Request Header 에서 토큰 정보를 꺼내오기 (위에서 이 메소드를 호출함)
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        log.info("[JwtFilter] bearerToken : {}", bearerToken);

        ///Header에서 Bearer 부분 이하로 붙은 token을 파싱한다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {

            return bearerToken.substring(7);
            // <지히> hasText 가 문자열이면서 "Bearer "로 시작하면 7번 인덱스부터 마지막까지의 값(== 토큰정보) 을 return
        }
        return null;
    }
}
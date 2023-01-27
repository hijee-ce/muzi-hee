package com.hee.muzihee.configuration;

import com.hee.muzihee.jwt.JwtFilter;
import com.hee.muzihee.jwt.TokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityconfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
    private final TokenProvider tokenProvider;

    public JwtSecurityconfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // TokenProvider 를 주입받아서 JwtFilter 를 통해 Security 로직에 필터를 등록
    // JwtFilter 를 등록한다.
    // 클라이언트가 리소스 요청할때 접근 권한이 없는 경우 기본적으로 로그인 폼으로 보내게 되는데 이 역할을 하는 필터가
    // UsernamePasswordAuthenticationFilter 이고 이 필터 실행 전에 customFilter를 걸어서 인증 권한이 없는 오류 처리를 할 수 있도록 한다.

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        // UsernamePasswordAuthenticationFilter 필터 전에 커스텀 필터를 걸겠다
    }


}

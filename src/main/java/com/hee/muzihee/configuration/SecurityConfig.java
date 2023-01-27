package com.hee.muzihee.configuration;

import com.hee.muzihee.jwt.JwtAccessDeniedHandler;
import com.hee.muzihee.jwt.JwtAuthenticationEntryPoint;
import com.hee.muzihee.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    private final TokenProvider tokenProvider;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler){
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/musicalimgs/**");
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
    //@Override
   // protected void configure(HttpSecurity http) throws Exception {


         // CSRF 설정 Disable
         http.csrf().disable()
                .formLogin().disable()//Spring Security가 기본적으로 제공하는 formLogin 기능을 사용하지 않겠다는것
                .httpBasic().disable()// 매 요청마다 id, pwd를 보내는 방식으로 인증하는 httpBasic를 사용하지 않겠다는것

                // 시큐리티는 기본적으로 세션을 사용하지만 API 서버에선 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정

                 .exceptionHandling()
                 .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                 .accessDeniedHandler(jwtAccessDeniedHandler)
                 .and()
                 .sessionManagement()
                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                    
                .and()
                .authorizeRequests()// http servletRequest 를 사용하는 요청들에 대한 접근제한을 설정
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/v1/musicals-management/**").hasRole("ADMIN")
                .antMatchers("/api/v1/members-management/**").hasRole("ADMIN")
                .antMatchers("/auth/**").permitAll()// 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .antMatchers("/api/v1/musicals/**").permitAll()// 공연 조회 관련// 누구나 접근가능
                .antMatchers("/api/v1/main/**").permitAll()// 메인도 누구나 접근가능
                .antMatchers("/api/**").hasAnyRole("USER", "ADMIN") // 나머지 API 는 전부 인증 필요
                 // 기본적으로 그동안 배웠던 시큐리티에 대한 설정들
                .and()
                 .cors()
                 .and()
                 .apply(new JwtSecurityconfig(tokenProvider));

       return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        // 로컬 React에서 오는 요청은 CORS 허용해준다.
         configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000" ));// 해당 ip만 응답
         //configuration.setAllowedOrigins(Arrays.asList("http://43.200.33.166:3000" ));// 해당 ip만 응답

        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));// 해당메소드만응답하겠다
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Content-Type", "Access-Control-Allow-Headers", "Authorization", "X-Requested-With"));// 해당 헤더의 응답만허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /* 기본적으로 프로토콜, 호스트, 포트 를 통틀어서 Origin(출처) 라고 한다.

     즉 서로 같은 출처란 이 셋이 동일한 출처를 말하고, 여기서 하나라도 다르다면 Cross Origin, 즉 교차출처가 되는 것이다.

     http://localhost:8080 : Spring Boot
     http://localhost:3000 : React
     보안상의 이유로, 브라우저는 스크립트에서 시작한 Cross Origin HTTP Request를 제한한다. 즉, SOP(Same Origin Policy)를 따른다.

     React와 Spring Boot의 port 가 서로 다르기 때문에 cors 정책 위반 에러가 나왔던 것이다.*/

}
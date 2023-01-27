package com.hee.muzihee.jwt;

import com.hee.muzihee.exception.TokenException;
import com.hee.muzihee.member.dto.MemberDto;
import com.hee.muzihee.member.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

//TokenProvider : 토큰의 생성, JWT 토큰에 관련된 암호화, 복호화, 검증 로직
@Slf4j
@Component
public class TokenProvider {


    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30분

    private final UserDetailsService userDetailsService;

    private final Key key;

    //application.yml 에 정의해놓은 jwt.secret 값을 가져와서 JWT 를 만들 때 사용하는 암호화 키값을 생성
    public TokenProvider(@Value("${jwt.secret}") String secretKey, UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes); //secret값을 Base64 Decode해서 key변수에 할당
    }


    // Authentication 객체(유저)의 권한정보를 이용해서 토큰을 생성
    // <지히> AuthService에서 호출되고 있음 (정상적으로 로그인되면 토큰을 발급해줘야하니까!)
    public TokenDto generateTokenDto(MemberDto member) {
        log.info("[TokenProvider] generateTokenDto Start ===================================");
        log.info("[TokenProvider] {}", member.getMemberRole());

        // 권한들 가져오기
        List<String> roles =  Collections.singletonList(member.getMemberRole());

        //유저 권한정보 담기
        Claims claims = Jwts
                .claims()
                .setSubject(member.getMemberId()); // sub : Subject. 토큰 제목으로 토큰에서 사용자에 대한 식별값이 됨
                //.setSubject(String.valueOf(member.getMemberCode()));
        claims.put(AUTHORITIES_KEY, roles);// 위에서 가져온 권한을 담아줌

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // ACCESS_TOKEN_EXPIRE_TIME : 위에서 30분으로 정해둠
        String accessToken = Jwts.builder()
                .setClaims(claims)                        // payload "auth": "ROLE_USER" // aud : Audience. 토큰 대상자를 나타낸다.
                .setExpiration(accessTokenExpiresIn)       // payload "exp": 1516239022 (예시) // exp : Expiration Time. 토큰 만료 시각을 나타낸다.
                .signWith(key, SignatureAlgorithm.HS512)   // header "alg": "HS512"  // "alg": "서명 시 사용하는 알고리즘",
                .compact();

        return new TokenDto(BEARER_TYPE, member.getMemberName(), accessToken, accessTokenExpiresIn.getTime());
    }

    /* <지히> JWT Claims
    * claim이란 JWT를 이용해 전송되는 암호화된 정보를 의미
    * +) JSON 오브젝트에서 다루는 프로퍼티의 이름이기도 함
    * ex) { "sub": "12345" } 에서 sub 는 Subject claim을 의미
    * 전송되는 JSON 오브젝트 전체를 payload 라고 하며, claim은 그 속에 있는 property를 의미함
    *
    * JWT의 구조 : 헤더 / 내용 / 서명 => 3가지 영역으로 나뉨
    *
    * 1. 헤더 (일반적으로 2가지 정보를 가짐)
    * "alg" : 어떤 알고리즘을 사용했는지
    * "typ" : 토큰의 타입이 무엇인지
    *
    * 2. 내용 (payload)
    * 엔티티와 추가적인 데이터에 대한 명세서인 claim을 포함하는 영역
    * registered claim, public claim, private claim 이 존재
    * => 아래 링크 들어가보면 자세히 나와있음!
    * 참고사이트 : https://jake-seo-dev.tistory.com/77
    *           https://brunch.co.kr/@jinyoungchoi95/1
    *
    * 3. 서명 (signature)
    * 메세지가 변조되지 않았음을 확인하는 영역
    * +) 만약 private 키로 서명된 경우엔, JWT 의 송신자가 데이터를 보내기로 한 그 당사자인지 확인할 수도 있다.
    */

    public String getUserId(String accessToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }


    // Token에 담겨있는 정보를 이용해 Authentication 객체 리턴
    // <지히> JwtFilter에서 호출되고 있음 (현재 사용자의 권한을 확인하기 위함)
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken); //JWT 토큰을 복호화하여 토큰에 들어 있는 정보를 꺼낸다 (아래정의해준 메소드 호출)

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        log.info("[TokenProvider] authorities : {}", authorities);

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(accessToken));

        //UserDetails 객체를 생성해서 UsernamePasswordAuthenticationToken 형태로 리턴, SecurityContext 를 사용하기 위한 절차
        //SecurityContext가 Authentication  객체를 저장하고 있기 때문이다 
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        /* <지히>
        * loadUserByUsername의 리턴타입은 MemberDto
        *
        * UsernamePasswordAuthenticationToken(Object principal, Object credentials,
			                                    Collection<? extends GrantedAuthority> authorities)
        * Principal(접근 주체) : 보호받는 Resource에 접근하는 대상
        * Credential(비밀번호) : Resource에 접근하는 대상의 비밀번호
        *
        *
        * 스프링 시큐리티 동작 과정 참고 사이트 : https://dev-coco.tistory.com/174
        */
    }

    //토큰의 유효성 검증 ,Jwts 이 exception을 던짐
    // <지히> JwtFilter에서 호출되고 있음 (현재 토큰에 대한 유효성 검사 메소드)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("[TokenProvider] 잘못된 JWT 서명입니다.");
            throw new TokenException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("[TokenProvider] 만료된 JWT 토큰입니다.");
            throw new TokenException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("[TokenProvider] 지원되지 않는 JWT 토큰입니다.");
            throw new TokenException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("[TokenProvider] JWT 토큰이 잘못되었습니다.");
            throw new TokenException("JWT 토큰이 잘못되었습니다.");
        }

    }

    private Claims parseClaims(String accessToken) {// 토큰 정보를 꺼내기 위해서
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {// 만료되어도 정보를 꺼내서 던짐
            return e.getClaims();
        }
    }
}

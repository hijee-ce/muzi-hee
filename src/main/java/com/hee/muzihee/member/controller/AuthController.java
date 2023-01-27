package com.hee.muzihee.member.controller;


import com.hee.muzihee.common.ResponseDto;
import com.hee.muzihee.member.dto.MemberDto;
import com.hee.muzihee.member.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@RequestBody MemberDto memberDto) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.CREATED, "회원가입 성공", authService.signup(memberDto)));
        /* <지히> .ok().body(body 내용)
        * .ok() : A shortcut for creating a ResponseEntity with the given body and the status set to OK.
        *  => body와 status가 OK 상태여야 ResponseEntity를 생성
        *
        * .body : the body of the response entity (possibly empty)
        * => 응답엔티티의 본문 (비어있는 것도 가능)
        * */

    }

    /* 1. 포스트맨 로그인(POST) : http://localhost:8090/auth/login
    body -> raw (JSON) / 비밀번호 admin (받은 DB소스에선 1234라서 1234로 진행)
    {
        "memberId" : "admin",
        "memberPassword" : "1234"
    }
    */
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody MemberDto memberDto) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "로그인 성공", authService.login(memberDto)));
    }

}
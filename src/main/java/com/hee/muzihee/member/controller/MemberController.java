package com.hee.muzihee.member.controller;

import com.hee.muzihee.common.ResponseDto;
import com.hee.muzihee.exception.UserNotFoundException;
import com.hee.muzihee.member.dto.MemberDto;
import com.hee.muzihee.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members-management")
    public ResponseEntity<ResponseDto> selectMemberList(){

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", memberService.selectMemberList()));
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<ResponseDto> selectMyMemberInfo(@PathVariable String memberId, Authentication authentication) {

        // 관리자이거나 로그인한 유저가 본인의 정보를 조회할 때만 조회가 가능해야함

        String loginUserId = authentication.getName(); // getName으로 id를 가져옴
        Boolean loginUserAuth = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Boolean sameUser = memberId.equals(loginUserId);

        System.out.println("memberId " + memberId);
        System.out.println("loginUserId " + loginUserId);
        System.out.println("memberId.equals(loginUserId) " + memberId.equals(loginUserId));

        // ROLE_ADMIN 을 포함하고 있으면 true (관리자라는 의미)
        /*
         * System.out.println("isAuthenticated " + authentication.isAuthenticated()); // true
         * System.out.println("getAuthorities " + authentication.getAuthorities()); //[ROLE_USER]
         * System.out.println("getCredentials " + authentication.getCredentials()); // (비밀번호에 관한 메소드 -> 공백 출력)
         * System.out.println("getPrincipal " + authentication.getPrincipal()); // MemberDto에 정보가 담긴채로 출력됨
         * System.out.println("getDetails " + authentication.getDetails()); // null
         * */

        if (!(loginUserAuth || sameUser)){ // 관리자가 아니거나 본인(로그인한 유저)이 아닌 다른 회원의 정보조회 불가
            throw new UserNotFoundException("본인의 정보만 조회가능합니다.");
        }

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", memberService.selectMyInfo(memberId)));
    }

//    @PatchMapping(value = "/members")
//    public ResponseEntity<ResponseDto> updateMember( @ModelAttribute MemberDto memberDto) {
//
//        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "회원 정보 수정 성공",  memberService.updateMember(memberDto)));
//    }

    @DeleteMapping(value = "/members/{memberCode}")
    public ResponseEntity<ResponseDto> deleteMember( @PathVariable Long memberCode) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "회원 정보 삭제 성공",  memberService.deleteMember(memberCode)));
    }



}

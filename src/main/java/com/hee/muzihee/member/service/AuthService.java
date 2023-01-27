package com.hee.muzihee.member.service;

import com.hee.muzihee.exception.DuplicatedUsernameException;
import com.hee.muzihee.exception.LoginFailedException;
import com.hee.muzihee.jwt.TokenProvider;
import com.hee.muzihee.member.dao.MemberMapper;
import com.hee.muzihee.member.dto.MemberDto;
import com.hee.muzihee.member.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthService {

    private final MemberMapper memberMapper;


    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    public AuthService(MemberMapper memberMapper, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }


    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        log.info("[AuthService] Signup Start ===================================");
        log.info("[AuthService] MemberRequestDto {}", memberDto);

        if(memberMapper.selectByEmail(memberDto.getMemberEmail()) != null) {
            log.info("[AuthService] 이메일이 중복됩니다.");
            throw new DuplicatedUsernameException("이메일이 중복됩니다.");
        }

        log.info("[AuthService] Member Signup Start ==============================");
        memberDto.setMemberPassword(passwordEncoder.encode(memberDto.getMemberPassword()));
        log.info("[AuthService] Member {}", memberDto);
        int result = memberMapper.insertMember(memberDto);
        log.info("[AuthService] Member Insert Result {}", result > 0 ? "회원 가입 성공" : "회원 가입 실패");
        // <지히> result 값이 0보다 작으면 회원가입실패인데,, 실패처리를 안해주는 이유는 뭘까...? (지금은 그냥 단순 log 출력만)

        log.info("[AuthService] Signup End ==============================");

        return memberDto;
    }

    @Transactional
    public TokenDto login(MemberDto memberDto) {
        log.info("[AuthService] Login Start ===================================");
        log.info("[AuthService] {}", memberDto);

        // 1. 아이디 조회
        MemberDto member = memberMapper.findByMemberId(memberDto.getMemberId())
                .orElseThrow(() -> new LoginFailedException("잘못된 아이디 또는 비밀번호입니다"));

        // 2. 비밀번호 매칭
        if (!passwordEncoder.matches(memberDto.getMemberPassword(), member.getMemberPassword())) {
            // <지히> 사용자에게 입력받은 비밀번호(memberDto)와 위에서 아이디로 조회해온 비밀번호(member)를 비교
            log.info("[AuthService] Password Match Fail!!!!!!!!!!!!");
            throw new LoginFailedException("잘못된 아이디 또는 비밀번호입니다");
        }

        // 3. 토큰 발급
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        log.info("[AuthService] tokenDto {}", tokenDto);

        log.info("[AuthService] Login End ===================================");

        return tokenDto;

    }

}

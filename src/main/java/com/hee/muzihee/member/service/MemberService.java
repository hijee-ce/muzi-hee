package com.hee.muzihee.member.service;

import com.hee.muzihee.member.dao.MemberMapper;
import com.hee.muzihee.member.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Service
public class MemberService {
    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @GetMapping
    public MemberDto selectMyInfo(@PathVariable String memberId) {
        log.info("[MemberService] getMyInfo Start ==============================");

        MemberDto member = memberMapper.selectByMemberId(memberId);
        log.info("[MemberService] {}", member);
        log.info("[MemberService] getMyInfo End ==============================");

        return member;
    }

    public List<MemberDto> selectMemberList() {

        return memberMapper.selectMemberList();

    }

//    @Transactional
//    public Object updateMember(MemberDto memberDto) {
//        int result = memberMapper.updateMember(memberDto);
//
//        return (result > 0) ? "멤버 정보 수정 성공" : "멤버 정보 수정 실패";
//    }

    @Transactional
    public Object deleteMember(Long memberCode) {
        int result = memberMapper.deleteMember(memberCode);

        return (result > 0) ? "멤버 정보 삭제 성공" : "멤버 정보 삭제 실패";
    }
}
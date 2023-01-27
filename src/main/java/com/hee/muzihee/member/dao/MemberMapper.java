package com.hee.muzihee.member.dao;


import com.hee.muzihee.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;


@Mapper
public interface MemberMapper {

    MemberDto selectByEmail(String email);

    int insertMember(MemberDto member);

    Optional<MemberDto> findByMemberId(String memberId);

    MemberDto selectByMemberId(String memberId);

    List<MemberDto> selectMemberList();

//    int updateMember(MemberDto memberDto);

    int deleteMember(Long memberCode);
}

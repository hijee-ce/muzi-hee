package com.hee.muzihee.member.service;

import com.hee.muzihee.exception.UserNotFoundException;
import com.hee.muzihee.member.dao.MemberMapper;
import com.hee.muzihee.member.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    // <지히> UserDetailsService : Spring Security 에서 사용자의 정보를 담는 인터페이스

    private final MemberMapper mapper;

    public CustomUserDetailsService(MemberMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        log.info("[CustomUserDetailsService] ===================================");
        log.info("[CustomUserDetailsService] loadUserByUsername {}", memberId);

        return mapper.findByMemberId(memberId)
                .map(user -> addAuthorities(user)) // <지히> 아래에 만들어둔 메소드 호출
                .orElseThrow(() -> new UserNotFoundException(memberId + "> 찾을 수 없습니다."));
    }

    private MemberDto addAuthorities(MemberDto member) {
        member.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(member.getMemberRole())));

        /* <지히> GrantAuthority
        * 현재 사용자(principal)가 가지고 있는 권한을 의미
        * GrantedAuthority 객체는 UserDetailsService 에 의해 불러올 수 있고,
        * 특정 자원에 대한 권한이 있는지를 검사하여 접근 허용 여부를 결정
        * */

        return member;
    }

    /* <지히> 동작 과정
    * 1. memberId 값으로 user 정보를 찾아옴 (findByMemberId 이용)
    * 2. 해당 user 의 권한범위 결정을 위해 addAuthorities 호출 (이 때 파라미터로 user 넘겨줌)
    * 3. 파라미터로 넘겨받은 user 의 role 확인 후 그에 맞는 권한을 설정해줌 (setAuthorities)
    *
    * Member 테이블에 권한에 대한 컬럼은 존재하지 않고, role 컬럼만 존재 (default = ROLE_USER)
    * => role을 get 해서 Authorities를 set 하는 것!
    *
    * 그래서 이건 언제 쓰이는거지...? => TokenProvider 에서 권한정보를 가져올 때 사용됨 */
}
package com.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.board.dto.MemberDTO;
import com.board.entity.MemberEntity;
import com.board.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

	// private final MemberService service;
	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// username은 스프링 시큐리티가 필터로 작동하면서 로그인 요청에서 가로채온 userid임.
		// MemberDTO memberInfo = service.memberInfo(username); 
		MemberEntity memberInfo = memberRepository.findById(username).get();
		
		if(memberInfo == null) {
			throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
		}
		
		// SimpleGrantedAuthority : 여러개의 사용자 Roll 값을 받는 객체
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(memberInfo.getRole());
		grantedAuthorities.add(grantedAuthority);
		
		User user = new User(username, memberInfo.getPassword(), grantedAuthorities);
		
		// user를 가공시켜줘서 username, password, role 값을 받아서 전달해줘야 함.
		return user;
	}	
}

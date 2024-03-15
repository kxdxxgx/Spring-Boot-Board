package com.board;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.board.dto.MemberDTO;
import com.board.entity.MemberEntity;
import com.board.entity.repository.MemberRepository;
import com.board.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@RequiredArgsConstructor
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	// 순환 오류 
	// private final MemberService service;
	private final MemberRepository memberRepository;
	
	// 로그인 성공 시 해야 할 명령문
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		
		// authentication.getName() --> 로그인 시 입력한 userid 값을 가져옴.
		// 순환 오류 뜸 authSuccessHandler -> memberServiceImpl -> webSecurityConfig -> authSuccessHandler
		// MemberDTO member = service.memberInfo(authentication.getName()); 
		
		// 
		MemberEntity memberInfo = memberRepository.findById(authentication.getName()).get();
		
		// 마지막 로그인 날짜 등록
		// member.setLastlogindate(LocalDateTime.now());
		// service.lastlogindateUpdate(member);
		
		memberInfo.setLastlogindate(LocalDateTime.now());
		memberRepository.save(memberInfo);
		
		
		// 패스워드 확인 후 마지막 패스워드 변경일이 30일이 경과 되었을 경우 ...
		// 최종 패스워드 변경일 + 변경취소 횟수 ...
		
		// 세션 생성
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(3600*24*7); // 세션 유지 기간
//		session.setAttribute("email", service.memberInfo(member.getEmail()).getEmail());
//		session.setAttribute("username", service.memberInfo(member.getEmail()).getUsername());
//		session.setAttribute("role", service.memberInfo(member.getEmail()).getRole());
		session.setAttribute("email", memberInfo.getEmail());
		session.setAttribute("username", memberInfo.getUsername());
		session.setAttribute("role", memberInfo.getRole());
		session.setAttribute("FromSocial", memberInfo.getFromSocial());
		
		log.info("************************* FormLogin 성공 *************************");
		
		setDefaultTargetUrl("/board/list?page=1");
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}

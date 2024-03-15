package com.board.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.board.dto.MemberDTO;
import com.board.entity.AddressEntity;
import com.board.entity.MemberEntity;
import com.board.entity.repository.AddressRepository;
import com.board.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	// @Autowired
	// private MemberMapper mapper;
	
	private final MemberRepository memberRepository;
	private final AddressRepository addressRepository;
	private final BCryptPasswordEncoder pwdEncoder;
	
	// 회원 가입
	@Override
	public void memberInfoRegistry(MemberDTO member) {
		// mapper.memberInfoRegistry(member);
		member.setRegdate(LocalDateTime.now());
		member.setLastpwdate(LocalDateTime.now());
		member.setPwcheck(1);
		member.setRole("USER");
		member.setFromSocial("N");
		memberRepository.save(member.dtoToEntity(member));
	}
	
	// 회원 정보 가져오기
	@Override
	public MemberDTO memberInfo(String email) {
		// return mapper.memberInfo(userid);
		return memberRepository.findById(email).map(member -> new MemberDTO(member)).get(); 
	}
	
	// 아이디 중복 확인
	@Override
	public int idCheck(String email) {
		// return mapper.idCheck(userid);
		return memberRepository.findById(email).isEmpty()?0:1;
	}
	
	// 패스워드 수정
	@Override
	public void memberPasswordModify(MemberDTO member) {
		// mapper.memberPasswordModify(member);
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setPassword(member.getPassword());
		memberRepository.save(memberEntity);
	}
	
	// 마지막 로그인 날짜 등록 하기
	@Override
	public void lastlogindateUpdate(MemberDTO member) {
		// mapper.lastlogindateUpdate(member);
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setLastlogindate(member.getLastlogindate());
		memberRepository.save(memberEntity);
	}
	
	// 마지막 로그아웃 날짜 등록 하기
	@Override
	public void lastlogoutdateUpdate(MemberDTO member) {
		// mapper.lastlogoutdateUpdate(member);
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setLastlogindate(member.getLastlogoutdate());
		memberRepository.save(memberEntity);
	}
	
	// 아이디 찾기
	@Override
	public String searchID(MemberDTO member) {
		// return mapper.searchID(member);
		return memberRepository.findByUsernameAndTelno(member.getUsername(), member.getTelno())
						.map(m -> m.getEmail()).orElse("ID_NOT_FOUND");
	}
	
	/*
	// authkey 업데이트
	@Override
	public void authkeyUpdate(MemberDTO member) {
		mapper.authkeyUpdate(member);
	}
	*/
	
	/*
	// authkey 존재 여부 확인 
	public MemberDTO MemberInfoByAuthkey(MemberDTO member) {
		return mapper.MemberInfoByAuthkey(member);
	}
	*/
	
	// 주소 검색
	@Override
	public Page<AddressEntity> addrSearch(int pageNum, int postNum, String addrSearch) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, postNum, Sort.by(Direction.ASC,"zipcode"));
		
		return addressRepository.findByRoadContainingOrBuildingContaining(addrSearch, addrSearch, pageRequest);
		/*
		Map<String, Object> data = new HashMap<>();
		data.put("startPoint", startPoint);
		data.put("endPoint", endPoint);
		data.put("addrSearch", addrSearch);
		return mapper.addrSearch(data);
		*/
	}

	/*
	// 주소 행 최대 갯수 계산
	@Override
	public int addrTotalCount(String addrSearch) {
		return mapper.addrTotalCount(addrSearch);
	}
	*/

}

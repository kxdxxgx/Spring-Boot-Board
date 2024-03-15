package com.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.dto.AddressDTO;
import com.board.dto.MemberDTO;
import com.board.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper mapper;
	
	// 회원 가입
	@Override
	public void memberInfoRegistry(MemberDTO member) {
		mapper.memberInfoRegistry(member);
	}
	
	// 회원 정보 가져오기
	@Override
	public MemberDTO memberInfo(String userid) {
		return mapper.memberInfo(userid);
	}
	
	// 아이디 중복 확인
	@Override
	public int idCheck(String userid) {
		return mapper.idCheck(userid);
	}
	
	// 패스워드 수정
	@Override
	public void memberPasswordModify(MemberDTO member) {
		mapper.memberPasswordModify(member);
	}
	
	// 마지막 로그인 날짜 등록 하기
	@Override
	public void lastlogindateUpdate(MemberDTO member) {
		mapper.lastlogindateUpdate(member);
	}
	
	// 마지막 로그아웃 날짜 등록 하기
	@Override
	public void lastlogoutdateUpdate(MemberDTO member) {
		mapper.lastlogoutdateUpdate(member);
	}
	
	// 아이디 찾기
	@Override
	public String searchID(MemberDTO member) {
		return mapper.searchID(member);
	}
	
	// authkey 업데이트
	@Override
	public void authkeyUpdate(MemberDTO member) {
		mapper.authkeyUpdate(member);
	}
	
	// authkey 존재 여부 확인 
	public MemberDTO MemberInfoByAuthkey(MemberDTO member) {
		return mapper.MemberInfoByAuthkey(member);
	}
	
	// 주소 검색
	@Override
	public List<AddressDTO> addrSearch(int startPoint, int endPoint, String addrSearch) {
		Map<String, Object> data = new HashMap<>();
		data.put("startPoint", startPoint);
		data.put("endPoint", endPoint);
		data.put("addrSearch", addrSearch);
		return mapper.addrSearch(data);
	}

	// 주소 행 최대 갯수 계산
	@Override
	public int addrTotalCount(String addrSearch) {
		return mapper.addrTotalCount(addrSearch);
	}



}

package com.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.board.mapper.MemberMapper;

public interface MasterService {

	// 유저마다 게시한 게시물 갯수
	public List<HashMap<String, Object>> boardCountGroupbyUserid() throws Exception;
	
	// 유저마다 게시한 댓글 갯수
	public List<HashMap<String, Object>> replyCountGroupbyUserid() throws Exception;
	
	// 유저 수
	public String memberCount() throws Exception;
	
	// 파일 리스트
	public List<HashMap<String, Object>> fileList() throws Exception;
	
	// 파일 리스트
	public List<HashMap<String, Object>> fileDeleteList() throws Exception;
	
	// 파일 삭제
	public void fileDelete(int fileseqno) throws Exception;
	
	// 직업, 성별 유저 수
	public List<HashMap<String, Object>> countJobGenderMap() throws Exception;
}

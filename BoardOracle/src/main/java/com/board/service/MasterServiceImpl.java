package com.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.mapper.MasterMapper;

@Service
public class MasterServiceImpl implements MasterService {
	
	@Autowired
	private MasterMapper mapper;
	
	// 유저마다 게시한 게시물 갯수
	@Override
	public List<HashMap<String, Object>> boardCountGroupbyUserid() throws Exception {
		return mapper.boardCountGroupbyUserid();
	}
	
	// 유저마다 게시한 댓글 갯수
	@Override
	public List<HashMap<String, Object>> replyCountGroupbyUserid() throws Exception {
		return mapper.replyCountGroupbyUserid();
	}
	
	// 유저 수
	public String memberCount() throws Exception {
		return mapper.memberCount();
	}
	
	// 파일 리스트
	@Override
	public List<HashMap<String, Object>> fileList() throws Exception {
		return mapper.fileList();
	}
	
	// 파일 리스트
	@Override
	public List<HashMap<String, Object>> fileDeleteList() throws Exception {
		return mapper.fileDeleteList();
	}
	
	// 파일 삭제
	@Override
	public void fileDelete(int fileseqno) throws Exception {
		mapper.fileDelete(fileseqno);
	}
	
	// 직업, 성별 유저 수
	@Override
	public List<HashMap<String, Object>> countJobGenderMap() throws Exception {
		return mapper.countJobGenderMap();
	}
}

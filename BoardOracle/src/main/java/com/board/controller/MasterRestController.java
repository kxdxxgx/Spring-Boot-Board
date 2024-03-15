package com.board.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.service.MasterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
// 데이터 형식 보려고 만들었음
public class MasterRestController {

	private final MasterService service;
	
	@GetMapping("/restmaster/numberPost")
	public List<HashMap<String, Object>> postNumberPost(Model model) throws Exception {
		List<HashMap<String, Object>> boardCount = service.boardCountGroupbyUserid();
		System.out.println(boardCount);
		
		// 데이터 타입 확인 절차
		for (HashMap<String, Object> map : boardCount) {
		    String userid = (String) map.get("userid");
		    String username = (String) map.get("username");
		    Integer count = ((BigDecimal) map.get("board_count")).intValue();

		    System.out.println("userid : " + userid);
		    System.out.println("Username : " + username);
		    System.out.println("boardCount : " + count);
		}
		
		return boardCount;
	}
	
}
package com.board.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.board.dto.BoardDTO;
import com.board.dto.ReplyDTO;
import com.board.dto.ReplyInterface;
import com.board.entity.BoardEntity;
import com.board.entity.repository.BoardRepository;
import com.board.entity.repository.ReplyRepository;
import com.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RESTController {
	
	private final BoardService service;
	private final BoardRepository boardRepository;
	private final ReplyRepository replyRepository;
	
	@GetMapping("/rest/list")
	public Page<BoardEntity> getList(Model model, @RequestParam("page") int pageNum, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword) throws Exception {
		
		int postNum = 5; // 한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 5; // 화면 하단에 보여지는 페이지리스트의 페이지 갯수
		
		return service.list(pageNum, postNum, keyword);
	}
	
	@GetMapping("/rest/view/{seqno}")
	public BoardDTO getView(@PathVariable("seqno") Long seqno) throws Exception {
		return service.view(seqno);
	}
	
	@GetMapping("/rest/list2/")
	public List<BoardDTO> getList() throws Exception {
		List<BoardDTO> boardDTOs = new ArrayList<>();
		boardRepository.findAll().stream().forEach(list -> boardDTOs.add(new BoardDTO(list)));
		return boardDTOs;
	}
	
	@GetMapping("/rest/reply/{seqno}")
	public List<ReplyInterface> getReply(@PathVariable("seqno") Long seqno) {
		return replyRepository.replyView(seqno);
	}
	
	@GetMapping("/rest/reply2/{seqno}")
	public List<ReplyDTO> getReply2(@PathVariable("seqno") Long seqno) {
		return replyRepository.replyView2(seqno);
	}
}
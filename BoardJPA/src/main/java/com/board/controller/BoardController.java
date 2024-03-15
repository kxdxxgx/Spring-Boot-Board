package com.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.BoardDTO;
import com.board.dto.FileDTO;
import com.board.dto.LikeDTO;
import com.board.dto.ReplyDTO;
import com.board.dto.ReplyInterface;
import com.board.entity.BoardEntity;
import com.board.entity.LikeEntity;
import com.board.service.BoardService;
import com.board.util.PageUtil;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	/*
		1. @Autowired을 이용한 DI(Dependency Injection. 의존성 주입)
		@Autowired
		BoardService service;
	*/
	
	/*
		2. 생성자를 이용한 의존성 주입
		BoardService service;
		public BoardController(BoardService service) {
			this.service = service;
		}
	*/
	
	//  3. Lombok에 의한 생성자 생성으로 의존성 주입
	private final BoardService service;
	
	// 게시물 목록 보기
	@GetMapping("/board/list")
	public void getList(Model model, @RequestParam("page") int pageNum, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword) throws Exception {
		
		int postNum = 5; // 한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 5; // 화면 하단에 보여지는 페이지리스트의 페이지 갯수
		
		PageUtil page = new PageUtil();
		Page<BoardEntity> list = service.list(pageNum, postNum, keyword);
		int totalCount = (int)list.getTotalElements(); // 
		
		// int startPoint = (pageNum-1)*postNum + 1; 
		// int endPoint = pageNum*postNum;
		// int totalCount = service.getTotalCount(keyword); // 전체 게시물 갯수
		
		System.out.println("totalCount = " + totalCount);
		
		// PageUtil page = new PageUtil();
		// String pageList = page.getPageList(pageNum, postNum, pageListCount, totalCount, keyword);		
		
		model.addAttribute("list", service.list(pageNum, postNum, keyword));
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		// model.addAttribute("pageList")
		// int pageNum, int postNum, int pageListCount, int totalCount, String keyword
		model.addAttribute("pageList", page.getPageList(pageNum, postNum, pageListCount, totalCount, keyword));
	}
	
	// 게시물 등록 화면 보기
	@GetMapping("/board/write")
	public void getWrite() {	
	}
	
	// 게시물 등록 하기
	@ResponseBody
	@PostMapping("/board/write")
	public String postWrite(BoardDTO board) throws Exception {
		
		Long seqno = service.getSeqnoWithNextval();
		board.setSeqno(seqno);
		service.write(board);
		// return "redirect:/board/list?page=1";
		return "{\"message\":\"GOOD\"}";
		
	}
	
	//파일 업로드
	@ResponseBody
	@PostMapping("/board/fileUpload")
	public String postFileUpload(BoardDTO board,@RequestParam("kind") String kind,
			@RequestParam("sendToFileList") List<MultipartFile> multipartFile,
			@RequestParam(name="deleteFileList",required=false) Long[] deleteFileList) throws Exception {
		
		Long seqno = 0L;
		
		String path = "c:\\Repository\\file\\";
		if(kind.equals("I")) { // 게시물 등록
			seqno = service.getSeqnoWithNextval();
			board.setSeqno(seqno);
			service.write(board);
		}
		
		if(kind.equals("U")) { // 게시물 수정
			service.modify(board);
			seqno = board.getSeqno();
			
			if(deleteFileList != null) {
				
				for(int i=0; i<deleteFileList.length; i++) {
					service.deleteFileList(deleteFileList[i]);
				}
			}
		}
		
		if(!multipartFile.isEmpty()) {
			
			File targetFile = null;
			FileDTO fileInfo = null;
			
			for(MultipartFile mpr:multipartFile) {
				String org_filename = mpr.getOriginalFilename();
				String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));
				String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;
				long filesize = mpr.getSize();
				
				try {
					
					targetFile = new File(path + stored_filename);
					mpr.transferTo(targetFile);
					
					fileInfo = FileDTO.builder()
										.seqno(seqno)
										.org_filename(org_filename)
										.stored_filename(stored_filename)
										.filesize(filesize)
										.email(board.getEmail().getEmail())
										.checkfile("O")
										.build();

//					fileInfo.setSeqno(seqno);
//					fileInfo.setOrg_filename(org_filename);
//					fileInfo.setStored_filename(stored_filename);
//					fileInfo.setFilesize(filesize);
//					fileInfo.setEmail(email);
//					fileInfo.setCheckfile("O") // O : 파일 존재, X : 파일 삭제
//					fileInfo.("kind", kind);
					
					service.fileInfoRegistry(fileInfo);
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				
			}
		}
		
		return "{\"message\":\"GOOD\"}";
	}
	
	//파일 다운로드
	@GetMapping("/board/fileDownload")
	public void fileDownload(@RequestParam("fileseqno") Long fileseqno, HttpServletResponse rs) throws Exception {
		
		String path = "c:\\Repository\\file\\"; 
		
		FileDTO fileInfo = service.fileInfo(fileseqno);
		String org_filename = fileInfo.getOrg_filename();
		String stored_filename = fileInfo.getStored_filename();
		byte fileByte[] = FileUtils.readFileToByteArray(new File(path+stored_filename));
		
		// Content-Disposition 헤드를 구성하여 바이트 타입으로 변환된 HTTP Response 메세지를 전송한다는 것은
		// 이 파일을 다운받게끔 하는 것임 (배열로 변환된 파일이 담겨 있는 바디)
		// Content-Disposition 헤드 구성 예) Content-Disposition: "attachment; filename="hello.jpg"
		
		rs.setContentType("application/octet-stream");
		rs.setContentLength(fileByte.length);
		rs.setHeader("content-Disposition", "attatchment; filename=\"" + URLEncoder.encode(org_filename,"UTF-8")+"\";");
		rs.getOutputStream().write(fileByte);
		rs.getOutputStream().flush();
		rs.getOutputStream().close();
	}
	
	
	// 게시물 내용 보기
	@GetMapping("/board/view")
	public void getView(@RequestParam("seqno") Long seqno, @RequestParam("page") int pageNum,
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword,
			Model model, HttpSession session) throws Exception  {
		
		// 좋아요/싫어요 처리
		// LikeDTO likeDTO = new LikeDTO();
		String session_email = (String)session.getAttribute("email");
		// likeDTO.setSeqno(seqno);
		// likeDTO.setEmail(email);
		// findbyId를 활용해서 바꿔주는 방법은 있지만 그렇게 하지는 않음
		// LikeDTO likeCheckView = service.likeCheckView(likeDTO);
		LikeEntity likeCheckView = service.likeCheckView(seqno,session_email);
		
		// 초기에 좋아요/싫어요 등록이 안되어져 있을 경우
		if(likeCheckView == null) {
			model.addAttribute("myLikeCheck","N");
			model.addAttribute("myDislikeCheck", "N");
		} else if (likeCheckView != null) {
			model.addAttribute("myLikeCheck", likeCheckView.getMylikecheck());
			model.addAttribute("myDislikeCheck", likeCheckView.getMydislikecheck());
		}
		
		model.addAttribute("view", service.view(seqno));
		model.addAttribute("viewEmail", service.view(seqno).getEmail().getEmail());		
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", service.pre_seqno(seqno,keyword));
		model.addAttribute("next_seqno", service.next_seqno(seqno,keyword));
		model.addAttribute("fileInfoView", service.fileInfoView(seqno));
		
		// 세션 email 값 가져 오기
		String sessionEmail = (String)session.getAttribute("email");
		
		// 조회수 증가
		if(!sessionEmail.equals(service.view(seqno).getEmail().getEmail())) {
			service.hitno(seqno);
		}
	}
	
	// 좋아요/싫어요 관리
	@ResponseBody
	@PostMapping("/board/likeCheck")
	public String postLikeCheck(@RequestBody Map<String,Object> likeCheckData) throws Exception {
		
		System.out.println("hello1");
		
		int seq = (int)likeCheckData.get("seqno");
		Long seqLong = (long) seq;
		// Long seqno = (Long)likeCheckData.get("seqno");
		String email = (String)likeCheckData.get("email");
		String mylikecheck = (String)likeCheckData.get("mylikecheck");
		String mydislikecheck = (String)likeCheckData.get("mydislikecheck");
		int checkCnt= (int)likeCheckData.get("checkCnt");
		
		System.out.println("checkcnt : " + checkCnt);
		System.out.println("mylikecheck : " + mylikecheck);
		System.out.println("mydislikecheck : " + mydislikecheck);
		
		// 현재 날짜, 시간 구해서 좋아요/싫어요 한 날짜/시간 입력 및 수정
		String likeDate = "";
		String dislikeDate = "";
		LocalDateTime now = LocalDateTime.now();
		if(mylikecheck.equals("Y")) {
			likeDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}else if(mydislikecheck.equals("Y")) {
			dislikeDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		
		System.out.println("likedate :" + likeDate);
		System.out.println("mydislikecheck2 : " + mydislikecheck);
		
		likeCheckData.put("likedate", likeDate);
		likeCheckData.put("dislikedate", dislikeDate);
		
		LikeDTO likeData = new LikeDTO();
		/*
		likeData.setSeqno(seqno); 
		likeData.setEmail(email);
		likeData.setMylikecheck(mylikecheck);
		likeData.setMydislikecheck(mydislikecheck);
		likeData.setLikedate(likeDate);
		likeData.setDislikedate(dislikeDate);
		*/
		
		System.out.println("mylikecheck3 : " + mylikecheck);
		System.out.println("mydislikecheck3 : " + mydislikecheck);
		System.out.println("likeData.getMylikecheck : " + likeData.getMylikecheck());
		System.out.println("likeData.getMydislikecheck : " + likeData.getMydislikecheck());
		
		// tbl_like 테이블 입력/수정
		LikeEntity likeCheckView = service.likeCheckView(seqLong,email);
		System.out.println("likeCheckView :" + likeCheckView);
		if (likeCheckView == null) service.likeCheckRegistry(seqLong, email, mylikecheck, mydislikecheck, likeDate, dislikeDate);
			else service.likeCheckUpdate(seqLong, email, mylikecheck, mydislikecheck, likeDate, dislikeDate);
		
		int likeCnt = service.view(seqLong).getLikecnt();
		int dislikeCnt = service.view(seqLong).getDislikecnt();
		
		System.out.println("likeCnt : " + likeCnt);
		System.out.println("dislikeCnt : " + dislikeCnt);
		
		switch(checkCnt) {
				
				case 1 : likeCnt--; break;
				case 2 : likeCnt++; dislikeCnt--; break;
				case 3 : likeCnt++; break;	
				case 4 : dislikeCnt--; break;
				case 5 : dislikeCnt++; likeCnt--; break;
				case 6 : dislikeCnt++; break;	
			}
			
			System.out.println("checkCnt : " + checkCnt);
			System.out.println("likeCnt : " + likeCnt);
			System.out.println("dislikeCnt : " + dislikeCnt);
			
			BoardDTO board = new BoardDTO();
			board.setSeqno(seqLong);
			board.setLikecnt(likeCnt);
			board.setDislikecnt(dislikeCnt);
			service.boardlikeUpdate(board);
			
			System.out.println("board.getLikecnt : " + board.getLikecnt());
			System.out.println("board.getDislikecnt : " + board.getDislikecnt());
			
			return "{\"likeCnt\":\"" + likeCnt + "\",\"dislikeCnt\":\"" + dislikeCnt + "\"}";
		}
	
	// 게시물 수정 화면 보기
	@GetMapping("/board/modify")
	public void getModify(@RequestParam("seqno") Long seqno, @RequestParam("page") int pageNum, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword, Model model) throws Exception {
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("view", service.view(seqno));
		model.addAttribute("fileInfoView", service.fileInfoView(seqno));
	}
	
	// 게시물 수정 하기
	@Transactional
	@ResponseBody
	@PostMapping("/board/modify")
	public String postModify(BoardDTO board, @RequestParam("page") int pageNum, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword,
			@RequestParam(name="deleteFileList",required=false) Long[] deleteFileList
			) throws Exception {
		
		//?
		service.modify(board);
		
		if(deleteFileList != null) {
			
			for(int i=0; i<deleteFileList.length; i++) {
				service.deleteFileList(deleteFileList[i]);
			}
		}
		
		return "{\"message\":\"GOOD\"}";
	}
	
	// 게시물 삭제 하기
	@Transactional
	@GetMapping("/board/delete")
	public String getDelete(@RequestParam("seqno") Long seqno) throws Exception {
		// transaction 시작
		service.fileInfoUpdate(seqno); // tbl_file 테이블의 checkfile column 값을 'X'로 변환
		service.delete(seqno); // 게시물 행 삭제
		// transaction 끝
		return "redirect:/board/list?page=1";
	}
	
	// 댓글 처리
	@ResponseBody
	@PostMapping("/board/reply")
	public List<ReplyInterface> postReply(ReplyInterface reply, @RequestParam("option") String option) throws Exception {
		
		LocalDateTime now = LocalDateTime.now();
		// String regdate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		// reply.setReplyregdate(regdate);
		System.out.println("replycontent : "+reply.getReplycontent());
		System.out.println("replywriter : "+reply.getReplywriter());
		
		switch(option) {
		
			case "I" : service.replyRegistry(reply); // 댓글 등록
						break;
			case "U" : service.replyUpdate(reply); // 댓글 수정
						break;
			case "D" : service.replyDelete(reply); // 댓글 삭제
						break;
		}
		
		return service.replyView(reply);
	}
}

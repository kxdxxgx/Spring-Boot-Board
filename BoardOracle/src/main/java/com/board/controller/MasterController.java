package com.board.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.web.server.ServerJwtDsl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.dto.MemberDTO;
import com.board.service.BoardService;
import com.board.service.MasterService;
import com.board.service.MemberService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
public class MasterController {
	
	@Autowired
	MasterService service;
	
	@Autowired
	MemberService memberservice; // 멤버 변수
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
    private DataSource dataSource; //데이터베이스 정보
	
	// 관리자 페이지
	@GetMapping("/master/sysManage")
	public void getSysManage() {
		
	}
	
	// 보드 수 확인
	@GetMapping("/master/numberPost")
	public void getNumberPost(Model model) throws Exception {
		List<HashMap<String, Object>> boardCount = service.boardCountGroupbyUserid();
		System.out.println(boardCount);
		
		// 데이터 타입 확인 절차
		for (HashMap<String, Object> map : boardCount) {
		    String userid = (String) map.get("userid");
		    String username = (String) map.get("username");
		    Integer boardcount = ((BigDecimal) map.get("boardcount")).intValue();

		    System.out.println("userid : " + userid);
		    System.out.println("Username : " + username);
		    System.out.println("boardCount : " + boardcount);
		}
		
		model.addAttribute("boardCount", boardCount);
	}
	
	// 댓글 수 확인
	@GetMapping("/master/numberReply")
	public void getNumberReply(Model model) throws Exception {
		
		List<HashMap<String, Object>> replyCount = service.replyCountGroupbyUserid();
		System.out.println(replyCount);
		
		model.addAttribute("replyCount", replyCount);
	}
	
	// 회원 수
	@GetMapping("/master/memberCount")
	public void getMemberCount(Model model) throws Exception {
		
		List<HashMap<String, Object>> memberCount = service.countJobGenderMap();
		model.addAttribute("memberCount", memberCount);
	}
	
	// 파일 수정 리스트 보여주기
	@GetMapping("/master/fileModify")
	public void getFileModify(Model model) throws Exception {
		
		model.addAttribute("fileList", service.fileList());
	}
	
	// 파일 삭제 목록 만들기
	@GetMapping("/master/fileModifyX")
	public String getFileModifyX(@RequestParam("fileseqno") int fileseqno) throws Exception {
		
		boardService.deleteFileList(fileseqno);
		
		return "redirect:/master/fileModify";
	}
	
	// 파일 삭제 리스트 보여주기
	@GetMapping("/master/fileDelete")
	public void getFileDelete(Model model) throws Exception {
		
		model.addAttribute("fileList", service.fileDeleteList());
	}
	
	// 파일 삭제 하기
	@GetMapping("/master/fileDeleteX")
	public String getFileDeleteX(@RequestParam("fileseqno") int fileseqno) throws Exception {
		
		service.fileDelete(fileseqno);
		return "redirect:/master/fileDelete";
	}
	
	// 시스템 정보
	@GetMapping("/master/systemInfo")
	public void getSystemInfo(MemberDTO member, HttpSession session, Model model) throws Exception {
		System.out.println(servletContext.getServerInfo());
		System.out.println(servletContext.getJspConfigDescriptor());
		System.out.println(servletContext.getEffectiveMajorVersion());
		System.out.println(servletContext.getEffectiveMinorVersion());
		System.out.println(servletContext.getMajorVersion());
		System.out.println(servletContext.getMinorVersion());
		System.out.println("getServletContextName : " + servletContext.getServletContextName());
		System.out.println("getVirtualServerName : " + servletContext.getVirtualServerName());
		System.out.println("getClassLoader : " + servletContext.getClassLoader());
		System.out.println("getDefaultSessionTrackingModes : " + servletContext.getDefaultSessionTrackingModes());
		System.out.println("getEffectiveSessionTrackingModes : " + servletContext.getEffectiveSessionTrackingModes());
		model.addAttribute("tomcat_apache", servletContext.getServerInfo());
		model.addAttribute("current_application_name", servletContext.getServletContextName());
		model.addAttribute("virtual_server_name", servletContext.getVirtualServerName());
		
		Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        String databaseProductName = metaData.getDatabaseProductName();
        String databaseProductVersion = metaData.getDatabaseProductVersion();

        model.addAttribute("database_Info",databaseProductName); //데이터베이스 정보
        model.addAttribute("database_VersionInfo",databaseProductVersion); //데이터베이스 버전 정보
	}
	
	@GetMapping("/master/boardManage")
	public void getBoardManage() throws Exception {
		
	}
	
	@PostMapping("/master/boardManage")
	public void postBoardManage(@RequestBody Map<String, String> map, HttpSession session) throws Exception {
		System.out.println("postBoardManage");
		if(map.get("size") != null) {
            session.setMaxInactiveInterval(3600*24*7); //세션유지기간 = 한시간24시간7일
            session.setAttribute("size", map.get("size"));
        } 
	}
}
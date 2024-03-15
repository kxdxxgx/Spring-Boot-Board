package com.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.AddressDTO;
import com.board.dto.MemberDTO;
import com.board.entity.AddressEntity;
import com.board.service.MemberService;
import com.board.util.PageUtil;
import com.board.util.Password;

@Controller
public class MemberController {
	
	@Autowired
	MemberService service; // 멤버 변수
	
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;
	
	// 로그인 화면 보기
	@GetMapping("/member/login")
	public void getLogin() {
		
	}
	
	// 로그인
	@PostMapping("/member/login")
	public void postLogin() {
		
	}
	
	// 로그인 
	@ResponseBody
	@PostMapping("/member/loginCheck")
	public String postlogin(MemberDTO member, HttpSession session
			// , @RequestParam("autologin") String autologin
			) {
		
		/*
		String authkey = "";

		// 로그인 시 authkey 생성
		if(autologin.equals("NEW")) {
			authkey = UUID.randomUUID().toString().replaceAll("-", "");
			member.setAuthkey(authkey);
			service.authkeyUpdate(member);
		}
		
		// authkey가 클라이언트에 쿠키로 존재할 경우 '로그인 과정 없이' 세션 생성 후 게시판 목록 페이지로 이동
		if(autologin.equals("PASS")) {
			MemberDTO memberInfo = service.MemberInfoByAuthkey(member);
			if(memberInfo != null ) {
				// 세션 생성
				session.setMaxInactiveInterval(3600*24*7); // 세션 유지 기간
				session.setAttribute("email", memberInfo.getEmail());
				session.setAttribute("username", memberInfo.getUsername());
				session.setAttribute("role", memberInfo.getRole());
				
				// return "redirect:/";
				return "{\"message\":\"GOOD\"}";
			}
		}
		
		System.out.println("email = " + member.getEmail());
		System.out.println("password = " + member.getPassword());
		System.out.println("DB password = " + service.memberInfo("jingom368"));
		*/
		
		// 아이디 존재 여부 확인
		if(service.idCheck(member.getEmail()) == 0) {
			// return "redirect:/";
			return "{\"message\":\"ID_NOT_FOUND\"}";
		}
		System.out.println("front email : " + member.getEmail());
		System.out.println("front passwrod : " + member.getPassword());
		System.out.println("server password : " + service.memberInfo(member.getEmail()).getPassword());
		
		// 패스워드가 올바르게 들어 왔는지 확인
		if(!pwdEncoder.matches(member.getPassword(), service.memberInfo(member.getEmail()).getPassword())) {
			
			// 잘못된 패스워드일 때
			// return "redirect:/board/list?page=1";
			return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
		
		} 
		
		return "{\"message\":\"GOOD\"}";
		/*
			else {
			// 제대로 된 아이디와 패스워드가 입력되었을 때 
			
			// 마지막 로그인 날짜 등록
			member.setLastlogindate(LocalDate.now());
			service.lastlogindateUpdate(member);
			
			// 패스워드 확인 후 마지막 패스워드 변경일이 30일이 경과 되었을 경우 ...
			// 최종 패스워드 변경일 + 변경취소 횟수 ...
			
			// 세션 생성
			session.setMaxInactiveInterval(3600*24*7); // 세션 유지 기간
			session.setAttribute("email", service.memberInfo(member.getEmail()).getEmail());
			session.setAttribute("username", service.memberInfo(member.getEmail()).getUsername());
			session.setAttribute("role", service.memberInfo(member.getEmail()).getRole());
			
			// return "redirect:/";
			return "{\"message\":\"GOOD\",\"authkey\":\"" + member.getAuthkey() + "\"}";
		}
		*/
	}
	
	// 로그인 화면 보기
	@GetMapping("/member/signup")
	public void getSignup() {
		
	}
	
	
	// 회원 등록 하기
	@ResponseBody
	@PostMapping("/member/signup")
	public Map<String,String> postSignup(MemberDTO member, @RequestParam("fileUpload") MultipartFile multipartFile) throws Exception {

		System.out.println("signup => good");
		String path = "c:\\Repository\\profile\\";
		File targetFile;
		
		// 중요한 것!
		if(!multipartFile.isEmpty()) {
			
			String org_filename = multipartFile.getOriginalFilename();
			String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));
			String stored_filename = UUID.randomUUID().toString().replaceAll("-","") + org_fileExtension;
			
			try {
				targetFile = new File(path + stored_filename);
				multipartFile.transferTo(targetFile);
				
				member.setOrg_filename(org_filename);
				member.setStored_filename(stored_filename);
				member.setFilesize(multipartFile.getSize());
				
			}catch(Exception e) {
				e.printStackTrace();
			}
				String inputPassword = member.getPassword();
				String pwd = pwdEncoder.encode(inputPassword); // 단방향 암호화
				member.setPassword(pwd);
				member.setLastpwdate(LocalDateTime.now());		
		}
			service.memberInfoRegistry(member);
			// return "redirect:/board/list?page=1";
			// return "{\"message\":\"GOOD\"}";
			
			Map<String,String> data = new HashMap<>(); data.put("message", "GOOD");
			data.put("username", URLEncoder.encode(member.getUsername(), "UTF-8"));
			 
			return data;
			
			// return "{\"message\":\"GOOD\",\"username\":\"" + URLEncoder.encode(member.getUsername(), "UTF-8") + "\"}";
	}
	
	// 회원 가입 시 아이디 중복 확인
	@ResponseBody
	@PostMapping("/member/idCheck")
	public int postIdCheck(@RequestBody String email) throws Exception {
		
		int result = service.idCheck(email);
		System.out.println("result = " + result);
		return result;
	}
	
	// 로그아웃
	@GetMapping("/member/logout")
	public String getLogout(HttpSession session, Model model) {
		String email = (String)session.getAttribute("email");
		String username = (String)session.getAttribute("username");
		
		System.out.println("email : " + email);
		
		MemberDTO member = new MemberDTO();
		member.setEmail(email);
		member.setLastlogoutdate(LocalDateTime.now());
		
		System.out.println("Lastlogoutdate : " + LocalDate.now());

		service.lastlogoutdateUpdate(member);
		
		model.addAttribute("email", email);
		model.addAttribute("username", username);
		// session.invalidate(); // 모든 세션 종료 --> 로그아웃...
		
		return "redirect:/";
	}
	
	@PostMapping("/member/invalidate")
	@ResponseBody
	public void postInvalidate(HttpSession session) {
		session.invalidate();
	}
	
	// 패스워드 변경 후 세션 종료
	@GetMapping("member/memberSessionOut")
	public String getMemberSessionOut(HttpSession session) {
		
		String email = (String)session.getAttribute("email");
		MemberDTO member = new MemberDTO();
		member.setEmail(email);
		member.setLastlogoutdate(LocalDateTime.now());
		service.lastlogoutdateUpdate(member);
		session.invalidate();
		return "redirect:/";
	}
	
	// 아이디 찾기
	@GetMapping("/member/searchID")
	public void getSearchID() {
		
	}
	
	// 아이디 찾기
	@ResponseBody
	@PostMapping("/member/searchID")
	public String postSearchID(MemberDTO member) {
		
		String email = (service.searchID(member) == null?"ID_NOT_FOUND":service.searchID(member));
		System.out.println("email : " + email);
		return "{\"email\":\"" + email + "\"}";
	}
	
	//임시 패드워드 설정
	@GetMapping("/member/searchPassword")
	public void getSearchPassword() throws Exception {
	}
	
	//임시 패드워드 설정
	@ResponseBody
	@PostMapping("/member/searchPassword")
	public String getSearchPassword(MemberDTO member) {
		System.out.println("email : " + member.getEmail());
		System.out.println("telno : " + member.getTelno());
		System.out.println("service telno : " + service.memberInfo(member.getEmail()).getTelno());
		if(service.idCheck(member.getEmail()) == 0)
			return "{\"message\":\"ID_NOT_FOUND\"}";
		if(!service.memberInfo(member.getEmail()).getTelno().equals(member.getTelno())) {
			return "{\"message\":\"TELNO_NOT_FOUND\"}";
		}
		
		// 임시 패스워드 생성
		Password password = new Password();
		String pwRandom = password.tempPassowrdMaker();
		
		System.out.println("pwRandom: " + pwRandom);
		// 임시 패스워드 인코딩 후 수정
		member.setPassword(pwdEncoder.encode(pwRandom));
		member.setLastpwdate(LocalDateTime.now());
		System.out.println("pwRandomencode: " + pwdEncoder.encode(pwRandom));
		service.memberPasswordModify(member);
		return "{\"message\":\"GOOD\",\"password\":\"" + pwRandom + "\"}";
	}
	
	// 로그인 시 패스워드 변경 여부 확인
    @GetMapping("/member/pwCheckNotice")
    public void  getPWCheckNotice() throws Exception {

    }
	
	// 주소 검색
	@GetMapping("/member/addrSearch")
	public void getAddrsearch(@RequestParam("addrSearch") String addrSearch, 
			@RequestParam("page") int pageNum, Model model) {
		int postNum = 10; // 한 화면에 보여지는 게시물 행의 갯수
		// int startPoint = (pageNum-1)*postNum + 1; 
		// int endPoint = pageNum*postNum;
		int pageListCount = 5; // 화면 하단에 보여지는 페이지리스트의 페이지 갯수
		// int totalCount = service.addrTotalCount(addrSearch); // 전체 게시물 갯수
		Page<AddressEntity> list = service.addrSearch(pageNum, postNum, addrSearch);
		List<AddressEntity> addressList = list.getContent();
		int totalCount = (int)list.getTotalElements(); // 
		
		PageUtil page = new PageUtil();
		for (AddressEntity address : addressList) {
			AddressDTO addressDTO = new AddressDTO(address);
		    System.out.println("Road: " + addressDTO.getRoad());
		    System.out.println("Building: " + addressDTO.getBuilding());
		    // 다른 필요한 주소 정보들을 출력하거나 처리할 수 있습니다.
		}
		
		// model.addAttribute("list", service.addrSearch(pageNum, postNum, addrSearch));;
		model.addAttribute("list",list);
		model.addAttribute("pageList", page.getPageAddress(pageNum, postNum, pageListCount, totalCount, addrSearch));
	}
	
	// 회원 정보 보기
	@GetMapping("/board/memberInfo")
	public void getmemberInfo(HttpSession session, Model model) throws Exception {
		String email = (String)session.getAttribute("email");
		model.addAttribute("memberInfo", service.memberInfo(email));
	}
	
	// 회원 기본 정보 변경
	@GetMapping("/board/memberInfoModify")
	public void getMemberInfoModify() {
		
	}
	
	// 회원 패스워드 변경
	@GetMapping("/member/memberPasswordModify")
	public void getMemberPasswordModify(HttpSession session, Model model) throws Exception {
		
	}
	
	// 회원 패스워드 변경
	@ResponseBody
	@PostMapping("/member/memberPasswordModify")
	public String postMemberPasswordModify(@RequestParam("old_password") String old_password,
			@RequestParam("new_password") String new_password, HttpSession session) throws Exception {
		
		String email = (String)session.getAttribute("email");
		
		// 패스워드가 올바르게 들어 왔는지 확인
		if(!pwdEncoder.matches(old_password, service.memberInfo(email).getPassword())) {
			// 잘못된 패스워드일 때
			// return "redirect:/board/list?page=1";
			return "{\"message\":\"PASSWORD_NOT_FOUND\"}";	
		}
		
		// 신규 패스워드로 업데이트
		MemberDTO member = new MemberDTO();
		member.setEmail(email);
		member.setPassword(pwdEncoder.encode(new_password));
		member.setLastpwdate(LocalDateTime.now());
		service.memberPasswordModify(member);
		
		return "{\"message\":\"GOOD\"}";
	}
}

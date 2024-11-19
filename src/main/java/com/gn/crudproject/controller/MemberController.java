package com.gn.crudproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.crudproject.config.JwtUtil;
import com.gn.crudproject.dto.MemberDto;
import com.gn.crudproject.entity.Member;
import com.gn.crudproject.repository.MemberRepository;
import com.gn.crudproject.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MemberController {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private Logger logger 
	= LoggerFactory.getLogger(MemberController.class);
	
	@GetMapping("/login")
	public String loginView() {
		return "member/login";
	}
	
	// 로그인 기능
	@PostMapping("/login")
	@ResponseBody 
	public ResponseEntity<Map<String,String>> loginApi(@RequestBody MemberDto dto){	
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "로그인중 오류가 발생했습니다.");
		
		String email = dto.getEmail();
		String password = dto.getPassword();
		
		Member member = memberRepository.findByEmail(email);
		if(member == null) {
			throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
		}
		
		// 암호화된 password를 디코딩한 값과 입력한 패스워드의 값이 다르면 null을 반환합니다.
		if(!encoder.matches(password, member.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		String token = jwtUtil.generateToken(member);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer "+token);
		
		resultMap.put("res_code", "200");
		resultMap.put("res_msg", "로그인되었습니다. 환영합니다.");

		return ResponseEntity.ok().headers(httpHeaders).body(resultMap);
		
	}
	
	@GetMapping("/signup")
	public String createMemberView() {
		return "member/create";
	}
	
	@PostMapping("/member")
	@ResponseBody
	public Map<String,String> createMemberApi(@RequestBody MemberDto dto){
		logger.info(dto.toString());
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "회원가입중 오류가 발생했습니다.");
		
		Member saved = memberService.save(dto);
		
		if(saved.getId() != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원가입이 완료되었습니다.");			
		}
		
		return resultMap;
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		new SecurityContextLogoutHandler().logout(request, response, 
				SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/login";
	}
	
	

}

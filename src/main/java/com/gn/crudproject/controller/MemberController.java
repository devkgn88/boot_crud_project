package com.gn.crudproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	private Logger logger 
	= LoggerFactory.getLogger(MemberController.class);
	
	@GetMapping("/login")
	public String loginView() {
		return "member/login";
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

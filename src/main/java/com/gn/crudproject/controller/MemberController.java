package com.gn.crudproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.crudproject.dto.MemberDto;
import com.gn.crudproject.entity.Member;
import com.gn.crudproject.repository.MemberRepository;

@Controller
public class MemberController {
	
	@Autowired
	private MemberRepository memberRepository;
	
	private Logger logger 
	= LoggerFactory.getLogger(MemberController.class);
	
	@GetMapping("/member/create")
	public String createMemberView() {
		return "member/create";
	}
	
	@PostMapping("/member/create")
	@ResponseBody
	public Map<String,String> createMemberApi(MemberDto dto){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "회원가입중 오류가 발생했습니다.");
		
		// 1. DTO를 엔티티로 변환
		Member member = dto.toEntity();
		logger.info(member.toString());
		// 2. 레퍼지토리로 엔티티를 DB에 저장
		Member saved = memberRepository.save(member);
		logger.info(member.toString());
		
		if(saved.getId() != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원가입이 완료되었습니다.");			
		}
		
		return resultMap;
		
	}
	

}

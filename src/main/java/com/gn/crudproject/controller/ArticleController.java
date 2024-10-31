package com.gn.crudproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.crudproject.dto.ArticleDto;
import com.gn.crudproject.entity.Article;

@Controller
public class ArticleController {
	
	@GetMapping("/article/create")
	public String createArticleView() {
		return "article/create";
	}
	
	@PostMapping("/article/create")
	@ResponseBody
	public Map<String,String> createArticleApi(
			ArticleDto dto
			){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "게시글 등록중 오류가 발생했습니다.");
		
		// System.out.println(dto);
		// DTO 형태로 전달받은 데이터를 Entity로 변환
		Article article = dto.toEntity();
		
		
		return resultMap;
	}
}

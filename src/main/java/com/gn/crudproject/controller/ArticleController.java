package com.gn.crudproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ArticleController {
	
	@GetMapping("/article/create")
	public String createArticleView() {
		return "article/create";
	}
	
	@PostMapping("/article/create")
	@ResponseBody
	public Map<String,String> createArticleApi(
			@RequestParam("title") String title
			,@RequestParam("content") String content
			){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "게시글 등록중 오류가 발생했습니다.");
		
		System.out.println(title);
		System.out.println(content);
		
		return resultMap;
	}
}

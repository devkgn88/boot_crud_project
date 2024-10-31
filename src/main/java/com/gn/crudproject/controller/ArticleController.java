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

import com.gn.crudproject.dto.ArticleDto;
import com.gn.crudproject.entity.Article;
import com.gn.crudproject.repository.ArticleRepository;

@Controller
public class ArticleController {
	
	@Autowired
	private ArticleRepository articleRepository;

	private Logger logger 
	= LoggerFactory.getLogger(ArticleController.class);
	
	
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
		
		// 1. DTO를 엔티티로 변환
		Article article = dto.toEntity();
		// DTO가 엔티티로 잘 변환되었는지 확인
		logger.info(article.toString());
		
		// 2. 레퍼지토리로 엔티티를 DB에 저장
		Article saved = articleRepository.save(article);
		// article에 DB에 잘 저장되는지 확인
		logger.info(article.toString());
		
		if(saved.getId() != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 등록되었습니다.");			
		}
		
		return resultMap;
	}
}

package com.gn.crudproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.crudproject.dto.ArticleDto;
import com.gn.crudproject.dto.PageBarDto;
import com.gn.crudproject.entity.Article;
import com.gn.crudproject.service.ArticleService;

@Controller
public class ArticleController {
	
//	@Autowired
//	private ArticleRepository articleRepository;

	@Autowired
	private ArticleService articleService;
	
	private Logger logger 
	= LoggerFactory.getLogger(ArticleController.class);
	
	// 게시글 생성 화면
	@GetMapping("/article/create")
	public String createArticleView() {
		return "article/create";
	}
	
	
	// 게시글 생성 기능
	@PostMapping("/article/create")
	@ResponseBody
	public Map<String,String> createArticleApi(
			ArticleDto dto
			){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "게시글 등록중 오류가 발생했습니다.");
		
		Article created = articleService.create(dto);
		
		if(created != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 등록되었습니다.");			
		}
		
		return resultMap;
	}
	
	// 게시글 목록 조회
	@GetMapping("/article")
	public String selectArticleAll(Model model
	,@RequestParam(name="nowPage", defaultValue="0") int nowPage
			) {
		
		// 1. 모든 데이터 가져오기
		Pageable pageable = PageRequest.of(nowPage, 5, Sort.by("createdTime").descending());
		
		Page<Article> pageArticle = articleService.list(pageable);
		
		PageBarDto pageBarDto 
			= new PageBarDto(pageArticle.getTotalPages()
					,pageArticle.getNumber(),
					pageArticle.getSize());
		
		// 2. 모델에 데이터 등록하기
		model.addAttribute("articleList",pageArticle);
		model.addAttribute("paging",pageBarDto);
		
		// 3. 뷰 페이지 설정하기
		return "article/list";
	}
	
	// 게시글 단일 조회
	@GetMapping("/article/{id}")
	public String selectArticleOne(@PathVariable("id") Long id,
			Model model) {
		Article articleEntity = articleService.detail(id);
		model.addAttribute("article",articleEntity);
		return "article/detail";
	}
	
	// 게시글 수정 화면
	@GetMapping("/article/{id}/update")
	public String updateArticleView(@PathVariable("id") Long id,
			Model model) {
		Article articleEntity = articleService.detail(id);
		model.addAttribute("article",articleEntity);
		return "article/update";
	}
	
	// 게시글 수정 기능
	@PostMapping("/article/{id}/update")
	@ResponseBody
	public Map<String,String> updateArticleApi(
			ArticleDto dto
			){
		// 기본 응답 셋팅하기
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "게시글 수정중 오류가 발생했습니다.");

		// 타깃 조회하기
		Article updated = articleService.update(dto);
		
		// 업데이트 및 정상 응답하기
		if(updated != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 정상적으로 수정되었습니다.");
		}
		return resultMap;
	}
	
	@DeleteMapping("/article/{id}")
	@ResponseBody
	public Map<String,String> deleteArticleApi(
			@PathVariable("id") Long id){
		// logger.info("삭제 요청이 들어왔습니다.");
		
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "게시글 삭제중 오류가 발생했습니다.");
		
		Article deleted = articleService.delete(id);
		// 2. 대상 엔티티 삭제하기
		if(deleted != null) {
			// 3. 삭제 완료 메시지 남기기
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 정상적으로 삭제되었습니다.");
		}
		
		return resultMap;
	}
	
	
}

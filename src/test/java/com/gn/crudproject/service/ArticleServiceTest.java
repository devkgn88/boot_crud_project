package com.gn.crudproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gn.crudproject.entity.Article;

@SpringBootTest
class ArticleServiceTest {
	
	@Autowired
	ArticleService articleService;

	@Test
	void detail_성공() {
		// 1. 예상 데이터
		Long id = 1L;
		Article expected = new Article(id,"11111","aaaaa",LocalDateTime.of(2024, 11,4,10,53,6),null);
		// 2. 실제 데이터
		Article article = articleService.detail(id);
		// 3. 비교 및 검증
		assertEquals(expected.toString(),article.toString());
	}
	
	@Test
	void detail_실패() {
		// 1. 예상 데이터
		Long id = 100L;
		Article expected = null;
		// 2. 실제 데이터
		Article article = articleService.detail(id);
		// 3. 비교 및 검증
		assertEquals(expected,article);
	}
	

}

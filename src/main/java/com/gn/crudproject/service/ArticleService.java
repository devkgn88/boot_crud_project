package com.gn.crudproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gn.crudproject.dto.ArticleDto;
import com.gn.crudproject.entity.Article;
import com.gn.crudproject.repository.ArticleRepository;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepository;
	
	// 게시글 등록
	public Article create(ArticleDto dto) {
		Article article = dto.toEntity();
		if(article.getId() != null) {
			return null;
		}
		return articleRepository.save(article);
	}
	
	// 게시글 목록 조회
	public Page<Article> list(Pageable pageable){
		return articleRepository.findAll(pageable);
	}
	
	// 게시글 단일 조회
	public Article detail(Long id) {
		return articleRepository.findById(id).orElse(null);
	}
	
	// 게시글 수정
	@Transactional
	public Article update(ArticleDto dto) {
		Article articleEntity = dto.toEntity();
		Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
		if(target == null || dto.getId() != target.getId()) {
			return null;
		} else {
			return articleRepository.save(articleEntity);
		}
	}
	
	// 게시글 삭제
	@Transactional
	public Article delete(Long id) {
		Article target = articleRepository.findById(id).orElse(null);
		if(target == null) {
			return null;
		}
		articleRepository.delete(target);
		return target;
	}
	
}

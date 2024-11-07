package com.gn.crudproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gn.crudproject.dto.ArticleDto;
import com.gn.crudproject.dto.UploadFileDto;
import com.gn.crudproject.entity.Article;
import com.gn.crudproject.entity.UploadFile;
import com.gn.crudproject.repository.ArticleRepository;
import com.gn.crudproject.repository.UploadFileRepository;

@Service
public class ArticleService {
	
	@Autowired
	private UploadFileService fileService;

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UploadFileRepository uploadFileRepository;
	
	// 게시글 등록
	public Article create(ArticleDto articleDto) {
		// 게시글 정보를 등록하고 파일에 관련 게시글 관련 컬럼을 만들기!!! 어때!!
		
		// 1. 파일 정보 등록
		int fileLeng = articleDto.getFiles().size();
		
		List<UploadFileDto> fileDtoList = new ArrayList<UploadFileDto>();
		
		for(MultipartFile file : articleDto.getFiles()) {
			UploadFileDto fileDto = fileService.uploadFile(file);
			if(fileDto != null) fileDtoList.add(fileDto);
		}
		
		// 2. 컴퓨터에 파일이 정상적으로 저장됐다면 
		// 메타 데이터 데이터베이스에 저장
		if(fileDtoList.size() == fileLeng) {
			
			List<UploadFile> fileList = new ArrayList<UploadFile>();
			
			for(UploadFileDto fileDto : fileDtoList) {
				UploadFile uploadFile = fileDto.toEntity();
				UploadFile created = new UploadFile();
				if(uploadFile.getId() != null) {
					created = null;
				}else {
					created = uploadFileRepository.save(uploadFile);
					fileList.add(created);
				}
			}
			
			// 3. 데이터베이스에 파일이 잘 등록되었다면, 게시글 등록
			if(fileList.size() == fileDtoList.size()) {
				Article article = articleDto.toEntity();
				Article created = new Article();
				if(article.getId() != null) {
					created = null;
				} else {
					created = articleRepository.save(article);
				}
				
				// 4. 게시글 정보도 잘 등록되었다면 매핑 테이블에 정보 등록
				if(created != null) {
					
					
				}
			}
			
		}
		
		// 2. 게시글 등록
		// 3. 파일과 게시글 정보 연결
		return new Article();
//		Article article = dto.toEntity();
//		if(article.getId() != null) {
//			return null;
//		}
//		return articleRepository.save(article);
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

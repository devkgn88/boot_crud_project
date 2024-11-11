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
	private UploadFileRepository fileRepository;
	
	@Transactional
	public Article create(ArticleDto articleDto) {
		
		Article createdArticle = new Article();
		
		try {
			Article article = articleDto.toEntity();
			if(article.getId() != null) {
				throw new Exception("이미 존재하는 게시글입니다.");
			} if(article.getId() == null) {
				createdArticle = articleRepository.save(article);
				// 1. 게시글 정보 등록 완료
				if(createdArticle != null) {
					
					// 2. 파일 메모리에 저장
					int fileLeng = articleDto.getFiles().size();
					
					List<UploadFileDto> fileDtoList = new ArrayList<UploadFileDto>();
					
					for(MultipartFile file : articleDto.getFiles()) {
						UploadFileDto fileDto = fileService.uploadFile(file);
						if(fileDto != null) fileDtoList.add(fileDto);
					}
					
					// 3. 파일 메타 데이터 저장
					if(fileDtoList.size() == fileLeng) {
						
						// 비어 있는 entity 리스트 생성
						List<UploadFile> fileList = new ArrayList<UploadFile>();
						
						// 파일 정보 저장 정보를 추가한 dto 리스트 반복문 돌리기
						for(UploadFileDto fileDto : fileDtoList) {
							// UploadFile uploadFile = fileDto.toEntity();
							UploadFile uploadFile = new UploadFile(fileDto.getId(),fileDto.getOri_name()
									,fileDto.getNew_name(), fileDto.getFile_dir(), createdArticle);
							
							UploadFile createdFile = new UploadFile();
							if(uploadFile.getId() != null) {
								createdFile= null;
								
							}else {
								createdFile = fileRepository.save(uploadFile);
								fileList.add(createdFile);
							}
						}
						
						if(fileList.size() != fileDtoList.size()) {
							throw new Exception("파일 메타 데이터 저장 중 오류가 발생하였습니다.");
						}
						
					} else {
						throw new Exception("파일 저장 중 오류가 발생하였습니다.");
					}
				} else {
					throw new Exception("게시글 등록중 오류가 발생하였습니다.");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	
		return createdArticle;
	}
	
	// 게시글 목록 조회
	public Page<Article> list(Pageable pageable, String searchText){
		
		if(searchText != null && searchText.trim().isEmpty() == false) {
			return articleRepository.findByTitleContaining(searchText,pageable);
		} else {
			return articleRepository.findAll(pageable);
		}
		
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
			if(dto.getDelete_files() != null && !dto.getDelete_files().isEmpty()) {
				
				for(Long file_id : dto.getDelete_files()) {
					// 1. 메모리에서 파일 자체 삭제
					fileService.delete(file_id);
					// 2. 데이터페이스에서 파일 데이터 삭제
					UploadFile deleteFile = fileRepository.findById(file_id).orElse(null);
					
					if(deleteFile != null) {
						fileRepository.delete(deleteFile);
					}
					
				}
			}
			
			if(dto.getFiles() != null && !dto.getFiles().isEmpty()) {
				// 파일 컴퓨터 메모리에 저장
				List<UploadFileDto> fileDtoList = new ArrayList<UploadFileDto>();
				
				for(MultipartFile file : dto.getFiles()) {
					UploadFileDto fileDto = fileService.uploadFile(file);
					if(fileDto != null) fileDtoList.add(fileDto);
				}
				
				// 비어 있는 entity 리스트 생성
				List<UploadFile> fileList = new ArrayList<UploadFile>();
				
				// 파일 정보 저장 정보를 추가한 dto 리스트 반복문 돌리기
				for(UploadFileDto fileDto : fileDtoList) {
					// UploadFile uploadFile = fileDto.toEntity();
					UploadFile uploadFile = new UploadFile(fileDto.getId(),fileDto.getOri_name()
							,fileDto.getNew_name(), fileDto.getFile_dir(), target);
					
					UploadFile createdFile = new UploadFile();
					if(uploadFile.getId() != null) {
						createdFile= null;
						
					}else {
						createdFile = fileRepository.save(uploadFile);
						fileList.add(createdFile);
					}
				}
			}
			
			
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
		List<UploadFile> fileList = fileRepository.findAllByArticleId(id);
		if(fileList != null && fileList.isEmpty() == false) {
			for(UploadFile uf : fileList) {
				// 1. 메모리에서 파일 자체 삭제
				fileService.delete(uf.getId());
				// 2. 데이터페이스에서 파일 데이터 삭제
				UploadFile deleteFile = fileRepository.findById(uf.getId()).orElse(null);
				
				if(deleteFile != null) {
					fileRepository.delete(deleteFile);
				}
				
			}
		}
		articleRepository.delete(target);
		return target;
	}
	
}

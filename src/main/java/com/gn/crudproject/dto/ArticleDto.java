package com.gn.crudproject.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gn.crudproject.entity.Article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ArticleDto {
	private Long id;
	private String title;	// 제목
	private String content;	// 내용
	private List<MultipartFile> files;
	private List<Long> delete_files;
	
	public Article toEntity() {
		return new Article(id,title,content,null,null);
	} 
}
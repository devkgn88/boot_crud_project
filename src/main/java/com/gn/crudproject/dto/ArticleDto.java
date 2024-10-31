package com.gn.crudproject.dto;

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
	private String title;	// 제목
	private String content;	// 내용
	
	public Article toEntity() {
		return new Article(null,title,content);
	}
}
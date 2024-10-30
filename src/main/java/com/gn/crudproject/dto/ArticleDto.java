package com.gn.crudproject.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ArticleDto {
	private String title;	// 제목
	private String content;	// 내용
}
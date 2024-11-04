package com.gn.crudproject.dto;

import lombok.Data;

@Data
public class PageBarDto {
	
	private int pageSize;
	private int nowPage;

	// 페이징바
	private int pageBarSize = 2;
	private int pageBarStart;
	private int pageBarEnd;
		
	//이전, 다음 여부
	private boolean prev = true;
	private boolean next = true;
	
	public PageBarDto(int totalPage, int nowPage, int pageSize) {
		this.pageSize = pageSize;
		this.nowPage = nowPage;
		this.pageBarStart = (nowPage/pageBarSize) * pageBarSize;
		this.pageBarEnd = pageBarStart + pageBarSize -1;
		if(pageBarEnd > totalPage-1) pageBarEnd = totalPage-1;
		if(pageBarStart == 0) prev = false;
		if(pageBarEnd >= totalPage-1) next = false;
	}
	
	
	

}

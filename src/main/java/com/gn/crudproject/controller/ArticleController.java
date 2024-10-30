package com.gn.crudproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
	
	@GetMapping("/article/create")
	public String createArticlePage() {
		return "article/create";
	}
}

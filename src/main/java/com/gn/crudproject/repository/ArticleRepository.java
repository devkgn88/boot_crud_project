package com.gn.crudproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.crudproject.entity.Article;

public interface ArticleRepository extends JpaRepository<Article,Long>{

}

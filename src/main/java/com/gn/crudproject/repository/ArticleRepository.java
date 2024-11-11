package com.gn.crudproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gn.crudproject.entity.Article;

public interface ArticleRepository extends JpaRepository<Article,Long>{

	Page<Article> findAll(Pageable pageable);
	
	Page<Article> findByTitleContaining(String title, Pageable pageable);
	
	@Query(value="SELECT a FROM Article a "+
	"WHERE a.title LIKE CONCAT('%',?1,'%') "+
	"OR a.content LIKE CONCAT('%',?1,'%') "+
	"ORDER BY a.createdTime DESC "
	,countQuery="SELECT COUNT(a) FROM Article a "+
	"WHERE a.title LIKE CONCAT('%',?1,'%') "+
	"OR a.content LIKE CONCAT('%',?1,'%') ")
	Page<Article> findByTitleOrContentContaining(String keyword, Pageable pageable);
	
}

package com.gn.crudproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.crudproject.entity.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile,Long>{

	List<UploadFile> findAllByArticleId(Long articleId);
}

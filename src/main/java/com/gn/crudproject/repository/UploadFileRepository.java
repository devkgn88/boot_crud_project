package com.gn.crudproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.crudproject.entity.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile,Long>{

}

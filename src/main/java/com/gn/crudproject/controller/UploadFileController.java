package com.gn.crudproject.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gn.crudproject.entity.UploadFile;
import com.gn.crudproject.repository.UploadFileRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UploadFileController {
	
	
	@Autowired
	private UploadFileRepository uploadFileRepository;
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Object> articleFileDownload(
			@PathVariable("id") Long id, HttpServletRequest req) throws Exception{
		
		try {
			UploadFile fileData = uploadFileRepository.findById(id).orElse(null);
			
			Path filePath = Paths.get(fileData.getFileDir());
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentDisposition(ContentDisposition.builder("attachment")
										.filename(fileData.getOriName()).build());
			
			return new ResponseEntity<Object>(resource,headers,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}

	}
	

}

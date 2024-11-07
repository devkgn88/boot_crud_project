package com.gn.crudproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CrudprojectApplication implements WebMvcConfigurer{

	@Value("${ffupload.location}")
	private String fileDir;
	
	public static void main(String[] args) {
		SpringApplication.run(CrudprojectApplication.class, args);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
			.addResourceLocations("file:///"+fileDir);
	}

}

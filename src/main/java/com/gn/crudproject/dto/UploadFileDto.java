package com.gn.crudproject.dto;

import com.gn.crudproject.entity.Article;
import com.gn.crudproject.entity.UploadFile;

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
public class UploadFileDto {

	private Long id;
	private String ori_name;
	private String new_name;
	private String file_dir;

	
	public UploadFile toEntity() {
		return new UploadFile(id,ori_name,new_name,file_dir,null);

	}
	
}

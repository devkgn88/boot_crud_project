package com.gn.crudproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="upload_file")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class UploadFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String oriName;
	
	@Column
	private String newName;
	
	@Column
	private String fileDir;
	
	@ManyToOne
	@JoinColumn(name="article_id")
	private Article article;
	
}

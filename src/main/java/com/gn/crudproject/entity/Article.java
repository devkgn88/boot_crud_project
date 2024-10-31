package com.gn.crudproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="article")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Article {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String title;
	
	@Column
	private String content;
}

package com.gn.crudproject.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="member")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdTime;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updatedTime;
}

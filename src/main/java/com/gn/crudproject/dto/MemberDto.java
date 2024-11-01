package com.gn.crudproject.dto;

import com.gn.crudproject.entity.Member;

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
public class MemberDto {
	
	private String email;
	private String password;
	
	public Member toEntity() {
		return new Member(null,email,password,null,null);
	}

}

package com.gn.crudproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gn.crudproject.repository.UserRepository;

@Service
// 스프링 시큐리티에서 사용자 정보를 가져올 때 
// 사용하는 인터페이스 UserDetailsService를 implements 받습니다.
public class UserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	// 사용자 이름(email)으로 사용자의 정보를 가져오는 메소드
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
				.orElseThrow(()-> new IllegalArgumentException(email));
	}
	
}

package com.gn.crudproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gn.crudproject.service.MemberDetailService;

// 스프링에서 읽는 환경설정 파일임을 의미해요.
@Configuration
// 스프링 시큐리티를 활성화하겠다는 어노테이션입니다.
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
	private MemberDetailService memberDetailService;
	
	@Autowired
	private JwtUtil jwtUtil;

	
	// 정적 리소스에 스프링 시큐리티를 비활성화합니다. 
	// 이미지, html등의 파일에는 인증, 인가를 적용하지 않아요.
	@Bean
	WebSecurityCustomizer configure() {
		return (web -> web.ignoring()
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
	}
	
	// 특정 HTTP 요청이 들어왔을 때 보안 관련 사항을 설정합니다.
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//		HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//		requestCache.setMatchingRequestParameterName(null);
		
		http.authorizeHttpRequests(requests -> requests
							.requestMatchers("/login","/signup","/member").permitAll()
							.anyRequest().authenticated())
//			.requestCache(cache -> cache.requestCache(requestCache))
			.formLogin(login -> login
					.loginPage("/login")
					.defaultSuccessUrl("/article")
					.successHandler(new MyLoginSuccessHandler()))
//			.formLogin(AbstractHttpConfigurer::disable)
//			.httpBasic(AbstractHttpConfigurer::disable)
			.logout(logout -> logout
					.logoutSuccessUrl("/login")
					.invalidateHttpSession(true))
			.addFilterBefore(new JwtAuthFilter(memberDetailService,jwtUtil), 
				UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	// 비밀번호 암호화에 사용될 빈을 등록합니다.
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	// AuthenticationManager(인증 관리자) 관련 사항을 설정합니다. 
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
	throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}

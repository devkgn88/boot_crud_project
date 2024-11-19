package com.gn.crudproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
	
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	private CustomAuthenticationEntryPoint authenticationEntryPoint;

	
	// 정적 리소스에 스프링 시큐리티를 비활성화합니다. 
	// 이미지, html등의 파일에는 인증, 인가를 적용하지 않아요.
	@Bean
	WebSecurityCustomizer configure() {
		return (web -> web.ignoring()
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
					);
	}
	
	// 특정 HTTP 요청이 들어왔을 때 보안 관련 사항을 설정합니다.
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//		HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//		requestCache.setMatchingRequestParameterName(null);
		
//		http.authorizeHttpRequests(requests -> requests
//							.requestMatchers("/login","/signup","/member").permitAll()
//							.anyRequest().authenticated())
//			.requestCache(cache -> cache.requestCache(requestCache))
//			.formLogin(login -> login
//					.loginPage("/login")
//					.defaultSuccessUrl("/article")
//					.successHandler(new MyLoginSuccessHandler()))
//			.formLogin(AbstractHttpConfigurer::disable)
//			.httpBasic(AbstractHttpConfigurer::disable)
//			.logout(logout -> logout
//					.logoutSuccessUrl("/login")
//					.invalidateHttpSession(true))
//			.addFilterBefore(new JwtAuthFilter(memberDetailService,jwtUtil), 
//				UsernamePasswordAuthenticationFilter.class);
		
		// csrf, cors 끄기
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors((Customizer.withDefaults()));
		
		// 더이상 세션 인증 방식을 사용하지 않기 때문에 세션 관리를 꺼줍니다.
		http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
				SessionCreationPolicy.STATELESS));
		
		// SpringSecurity가 자동으로 생성해주는 로그인 폼을 비활성화합니다. 
		http.formLogin(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);
		
		// JwtAuthFilter를 UsernamePasswordAuthenticationFilter가 동작하기 전에 동작하도록 설정합니다.
		http.addFilterBefore(new JwtAuthFilter(memberDetailService, jwtUtil), 
				UsernamePasswordAuthenticationFilter.class);
		
		// 인증, 인가 단계에서 오류가 발생했을 때의 동작을 지정해줍니다. 
		http.exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(
				authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler));
		
		// 권한 규칙을 작성해줍니다. 
		http.authorizeHttpRequests(requests -> requests
		.requestMatchers("/login","/signup","/member").permitAll()
		.anyRequest().authenticated());
		
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

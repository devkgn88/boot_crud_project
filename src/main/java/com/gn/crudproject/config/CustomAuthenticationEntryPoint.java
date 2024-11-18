package com.gn.crudproject.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 인증(사용자인지 아닌지 검증)에서 예외가 발생했을때 동작하는 핸들러입니다.
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
	
	@Override
	public void commence(HttpServletRequest request, 
			HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// 가입하지 않은 사용자가 접근하는 경우 동작을 규정합니다.
		response.sendRedirect("/signup");
	}
	
	
}

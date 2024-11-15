package com.gn.crudproject.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler{
	
	private final ObjectMapper objectMapper;

	@Override
	public void handle(final HttpServletRequest request, final HttpServletResponse response,
		final AccessDeniedException accessDeniedException) throws IOException, ServletException {
		System.out.println("여기가 도는건가요?");

		String responseBody = "에러나따요";
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseBody);
	}

}

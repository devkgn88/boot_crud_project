package com.gn.crudproject.config;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

// 인가(권한이 있는지 아닌지 검증)에서 예외가 발생했을 때 동작하는 핸들러입니다.
@Component
@AllArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler{@Override

	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// 사용자는 맞지만, 권한이 없을때 취할 동작을 지정합니다.
		response.sendRedirect("/");
	}

}

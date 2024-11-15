package com.gn.crudproject.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gn.crudproject.service.MemberDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilter extends OncePerRequestFilter{
	
	private final MemberDetailService memberDetailService;
	private final JwtUtil jwtUtil;
	
	public JwtAuthFilter(MemberDetailService memberDetailService,
			JwtUtil jwtUtil) {
		this.memberDetailService = memberDetailService;
		this.jwtUtil = jwtUtil;
	}

	// JWT 검증 필터를 수행할 메소드입니다.
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 발급받은 JWT를 이용해서 인증하려면 HTTP 요청 헤더의 Authorization 키안에 
		// Bearer이라는 문자열과 JWT 토큰값을 넣어줘야 합니다.
		// 그러므로 반대로 JWT 헤더가 있는지 확인하려면 Authorization키를 기준으로 HTTP 헤더의 값을 가져와서
		String authorizationHeader = request.getHeader("Authorization");
		
		// Bearer로 시작하는 데이터를 찾습니다.
		// 만일, key값이 Authorization인 값이 있으고, 그 값이 Bearer로 시작한다면 
		// 정상적인 JWT 헤더가 있다는 뜻이겠지요.
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			// Bearer과 한칸 띄어쓰기 까지 이후를 잘라줍니다. -> JWT 토큰값
			String token = authorizationHeader.substring(7);
			// HTTP헤더에서 꺼낸 JWT 토큰값의 유효성을 검증합니다.
			if (jwtUtil.isValidToken(token)) {
				String userEmail = jwtUtil.getUserEmail(token);

				// 유저와 토큰이 일치할 경우 userDetails를 생성합니다.
				// 우리가 email로 설정한 사용자의 식별자는 JWT 토큰에서는 userId이며,
				// Spring Security에서는 userName이라고 부릅니다.
				UserDetails userDetails = memberDetailService.loadUserByUsername(
					userEmail);
				
				// 만약에 email을 기준으로 조회한 사용자 정보가 있다면
				if (userDetails != null) {
					// UserDetails, Password, Role으로 접근 권한 인증 Token을 생성합니다.
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

					//현재 들어온 요청(Request)의 Security Context에 접근 권한을 설정해줍니다.
					SecurityContextHolder.getContext()
						.setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}
		// 헤더가 없다면 바로, 헤더가 있다면 위 작업을 수행한 후에 다음 필터로 작업을 넘깁니다.
		filterChain.doFilter(request, response);
		
	}
	
}

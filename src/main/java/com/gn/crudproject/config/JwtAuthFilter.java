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
import lombok.RequiredArgsConstructor;

// Spring Security가 사용할 필터입니다. 
// 요청이 들어올 때마다 Jwt의 유효성을 검사하고,
// 유효한 경우 사용자의 정보를 SecurityContext에 설정합니다. 
// SecurityContext는 검증된 사용자의 정보가 저장된 보관소라고 생각하시면 됩니다.
// @RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
	
	private final MemberDetailService memberDetailService;
	private final JwtUtil jwtUtil;
	
	public JwtAuthFilter(MemberDetailService memberDetailService,
			JwtUtil jwtUtil) {
		this.memberDetailService = memberDetailService;
		this.jwtUtil = jwtUtil;
	}

	// JWT 검증 필터를 수행할 메소드입니다.
	// HTTP 헤더에 들어있는 인증 정보를 꺼내와서 유효성을 검토합니다.
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// JWT를 통해 인증하는 경우 HTTP 요청 헤더의 Authorization 키안에 
		// "Bearer" + JWT 토큰값을 넣습니다. 
		// 그러므로 반대로 JWT 헤더가 있는지 확인하려면 
		// Authorization키를 기준으로 HTTP 헤더의 값을 가져옵니다.
		String authorizationHeader = request.getHeader("Authorization");
		
		// 그 다음 Bearer로 시작하는 데이터를 찾습니다.
		// 만일, key값이 Authorization인 값이 있으고, 그 값이 Bearer로 시작한다면 
		// 정상적인 JWT 헤더가 있다는 뜻이겠지요.
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			// Bearer은 JWT 토큰값 앞에 붙여주는 무의미한 값이므로 
			// Bearer과 한칸 띄어쓰기 이후를 잘라서 가져옵니다. -> JWT 토큰값
			String token = authorizationHeader.substring(7);
			// HTTP헤더에서 꺼낸 JWT 토큰값의 유효성을 검증합니다.
			if (jwtUtil.isValidToken(token)) {
				// JwtUtil에서 만들어주었던 메소드입니다. 
				// 내부 메소드 getClaims를 통해서 토큰에서 클레임을 꺼내고
				// 클레임에서 이메일을 꺼내는 메소드였습니다.
				String userEmail = jwtUtil.getUserEmail(token);

				// 유저와 토큰이 일치할 경우 userDetails를 생성합니다.
				// 우리가 토큰에서 email로 설정한 사용자의 식별자는 
				// Spring Security에서는 userName이라고 불렀어요.
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

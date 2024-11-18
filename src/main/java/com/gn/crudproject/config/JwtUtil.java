package com.gn.crudproject.config;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gn.crudproject.entity.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// JWT를 생성하고 검증하는 기능 메소드를 가진 클래스입니다.
@Component	// 재사용이 가능한 Bean(빈)으로 등록합니다.
public class JwtUtil {

	// JWT 서명에 사용할 키입니다. 
	// 클래스 내부에서 많이 사용되기 때문에 메소드 밖으로 뺐습니다.
	private final Key key;
	// 액세스 토큰의 만료 시간값입니다.
	private final long expTime;
	

	// JwtUtil 클래스의 매개변수 생성자를 활용하여 
	// 2개의 필드 key와 expTime을 초기화합니다. 
	public JwtUtil(
			@Value("${jwt.secret}") final String secretKey,
			@Value("${jwt.expiration_time}") final long expTime)
		{
			// application.properties에서 가지고온 key값을 Key 객체로 변환해서 사용합니다.
			byte[] keyBytes = Decoders.BASE64.decode(secretKey);
			this.key = Keys.hmacShaKeyFor(keyBytes);
			this.expTime = expTime;
		}

	// 아래 AccessToken을 생성하는 함수 createAccessToken을 활용하여
	// 문자열 형태로 JWT를 리턴하는 메소드입니다.
	public String generateToken(Member member) {
		return createAccessToken(member,expTime);
	}
	
	// Access Token을 생성하는 내부 메소드입니다.
	// 매개변수로 회원 엔티티와 만료 기간을 사용합니다.
	public String createAccessToken(Member member, long expTime) {
		// 1. Claims 객체를 생성하여 사용자 정보를 클레임에 추가합니다.
		Claims claims = Jwts.claims();
		claims.put("id", member.getId());
		claims.put("email", member.getEmail());
		claims.put("role", "USER");
		
		// 2. 현재 시간을 기준으로 토큰 발생시간과 만료 시간을 설정합니다.
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime tokenValidity = now.plusSeconds(expTime);
		
		// 3. JWT 빌더를 사용하여 클레임, 발행시간, 만료시간을 설정합니다 .
		// 암호화 알고리즘과 key를 지정하여 토큰을 생성합니다.
		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuedAt(Date.from(now.toInstant()))
				.setExpiration(Date.from(tokenValidity.toInstant()))
				.setClaims(claims)
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	
	// 토큰의 유효성을 검증하는 메소드입니다.
	public boolean isValidToken(String token) {
		try{
			// 아래 코드는 주어진 key값으로 JWT를 복호화하는 과정입니다.
			// 만일 복호화 과정중에 오류가 발생했다면 유효하지 않은 토큰이므로 false를 리턴합니다.
			Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	// 주어진 JWT 토큰에서 사용자 ID를 추출하는 메소드입니다.
	// 아래 메소드 getClaims를 사용하여 토큰 내부에서 클래임을 꺼냅니다. 
	// 클래임에서 id값을 추출하여 반환합니다.
	public Long getUserId(String token) {
		Claims claims = getClaims(token);
		return claims.get("id",Long.class);
	}
	
	// 주어진 JWT 토큰에서 사용자 Email을 가져오는 메소드입니다. 
	// 아래 메소드 getClaims를 사용하여 토큰 내부에서 클래임을 꺼냅니다. 
	// 클래임에서 email값을 추출하여 반환합니다.
	public String getUserEmail(String token) {
		Claims claims = getClaims(token);
		return (String)claims.get("email");
	}
	
	
	// 복호화 key값으로 토큰을 복호화한 다음 Claims를 추출하는 내부 메소드입니다.
	// 클래임에서 id값을 꺼내는 getUserId 메소드와 
	// email값을 꺼내는 getUserEmail 메소드에서 사용합니다.
	// Claim는 JWT의 Payload내부의 사용자 정보를 인것 알고 있으시죠?
	// 토큰이 만료되었을 경우 ExpiredJwtException 예외에서 클레임을 추출하여 반환합니다.
	private Claims getClaims(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		}catch(ExpiredJwtException e) {
			return e.getClaims();
		}
	}
	
}

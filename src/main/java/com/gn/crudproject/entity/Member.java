package com.gn.crudproject.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="member")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class Member implements UserDetails{
	// Member 엔티티를 인증용 객체로 사용하려면
	// UserDetails를 implements 받아야 합니다.
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdTime;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updatedTime;
	
	// Builder는 lombok이 가지고 있는 기능중 하나 입니다. 
	// Builder 어노테이션을 붙이면 java의 buil() 메소드를 통해
	// 일부 필드만으로도 매개변수 생성자를 통해 객체를 만들 수 있습니다.
	@Builder
	public Member(String email, String password, String auth) {
		this.email = email;
		this.password = password;
	}
	// 아래에 코드 중에서 @Override 어노테이션이 붙어있는 메소드는
	// UserDetails를 implements 받으면서 구현한 추상 메소드입니다.
	
	// 사용자가 가지고 있는 권한의 목록을 반환합니다. 
	// 아직까지는 특별한 권한 설정을 해주지 않았기 때문에 "user"라는 사용자 권한만 담았습니다.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("user"));
	}

	// 사용자를 식별할 수 있는 고유한 이름을 반환합니다.
	// 이때 사용되는 username은 반드시 고유한 값이어야 합니다.
	// 우리는 고유한 값으로 이메일을 사용했으므로 이메일을 사용합니다.
	@Override
	public String getUsername() {
		return email;
	}
	
	// 사용자의 비밀번호를 반환합니다. 
	// 이때 저장되어 있는 비밀번호는 암호화되어 있어야 합니다.
	@Override
	public String getPassword() {
		return password;
	}
	
	// 아래 is~로 시작하여 boolean 타입을 리턴하는 메소드가
	// true를 리턴할 경우 사용 가능한 계정이라는 뜻입니다.
	
	// 계정 만료 여부를 반환하는 메소드입니다. 
	// 임시 계정이거나 비활성화된 계정인지 관리하는데 사용됩니다. 
	@Override
	public boolean isAccountNonExpired() {
		// 계정 만료 여부를 확인하는 로직이 필요하다면 적어줍니다. 
		return true;
	}
	
	// 계정의 잠금 여부를 반환하는 메소드입니다. 
	// 사용자가 부적절한 접근을 다수 시도하면 계정을 잠글 수있습니다. 
	// 아래 메소드는 잠긴 계정인지 관리하는데 사용됩니다.
	@Override
	public boolean isAccountNonLocked() {
		// 계정 잠금 여부를 확인하는 로직이 필요하다면 적어줍니다. 
		return true;
	}
	
	// 패스워드 만료 여부를 반환하는 메소드입니다.
	// 비밀번호의 지속 기한을 지정하고 만료여부를 확인할 수 있습니다.
	@Override
	public boolean isCredentialsNonExpired() {
		// 패스워드가 만료되었는지 확인하는 로직이 필요하다면 추가합니다. 
		return true;
	}
	
	// 계정 사용 가능 여부를 반환하는 메소드입니다.
	@Override
	public boolean isEnabled() {
		// 계정이 사용 가능한지 확인하는 로직이 필요하다면 추가합니다.
		return true;
	}
}

package com.example.danbi.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service

// 스프링 시큐리티가 로그인 시 사용할 UserSecurityService는 스프링 시큐리티가 제공하는 UserDetailsService 인터페이스를 구현(implements)해야 한다.
public class SpringSecurityService implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Override
	// 스프링 시큐리티의 UserDetailsService는 loadUserByUsername 메서드를 구현하도록 강제하는 인터페이스이다.
	// loadUserByUsername 메서드는 사용자명(username)으로 스프링 시큐리티의 사용자(User) 객체를 조회하여 리턴하는 메서드이다.
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// 1. 엔티티 User를 조회
		Optional<SiteUser> _siteUser = this.userRepository.findByUsername(username);
		if(_siteUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
		}
		SiteUser siteUser = _siteUser.get();
		
		// 2. Authority는 List<GrantedAuthority>
		List<GrantedAuthority> authorities = new ArrayList<>();
		if("admin".equals(username)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		
		// 2. 리턴하는 USER는 스프링시큐리티의 유저임
		// 비밀번호까지 포함해야 함 - 로그인이므로 비밀번호 동일여부 체크해야 함
		// UserDetails.User(username, password, authorities)
		return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
		
		// 여기서는 User객체까지 생성하고 종료
	}
}

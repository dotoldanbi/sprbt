package com.example.danbi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 이 파일이 스프링의 환경설정파일임을 의미
@EnableWebSecurity // 모든 요청 URL이 스프링시큐리티의 제어를 받도록 함
@EnableMethodSecurity(prePostEnabled=true) // @PreAuthorize("isAuthenticated()") 사용가능
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
				.csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
				.headers((headers) -> headers.addHeaderWriter(
						new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

				// 밑으로 form login : 스프링 시큐리티의 로그인 설정을 담당
				.formLogin((formLogin) -> formLogin.loginPage("/user/login") // form login에서 submit 후 매핑할 POST
						.defaultSuccessUrl("/"))

				.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
						.logoutSuccessUrl("/").invalidateHttpSession(true));

		return http.build();
	}

	@Bean
	// AuthenticationManager는 스프링 시큐리티의 인증을 처리한다.
	// AuthenticationManager는 사용자 인증 시 앞에서 작성한 UserSecurityService와 PasswordEncoder를
	// 내부적으로 사용하여 인증과 권한 부여 프로세스를 처리한다.
	// Bean으로 설정하면 로딩 시에 자동 생성
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

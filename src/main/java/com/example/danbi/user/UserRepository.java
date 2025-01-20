package com.example.danbi.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long>{
	
	// 로그인 시 사용자 ID를 조회하는 트랝잭션이 필요
	Optional<SiteUser> findByUsername(String username);

}

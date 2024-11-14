package com.gn.crudproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.crudproject.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	// Optional<Member> findByEmail(String email);
	Member findByEmail(String email);
}

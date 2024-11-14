package com.kosa.mini.api.repository;

import com.kosa.mini.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByEmail(String email);
    @Query("SELECT m FROM Member m JOIN FETCH m.role WHERE m.email = :email")
    Optional<Member> findByEmailWithRole(@Param("email") String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Member findByEmailAndPassword(String email, String password);
}

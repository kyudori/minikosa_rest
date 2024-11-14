package com.kosa.mini.api.repository;

import com.kosa.mini.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEditProfileRepository extends JpaRepository<Member,Integer> {

}

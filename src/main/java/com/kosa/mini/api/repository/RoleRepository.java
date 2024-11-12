package com.kosa.mini.api.repository;

import com.kosa.mini.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

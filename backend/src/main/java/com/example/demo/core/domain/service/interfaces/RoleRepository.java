package com.example.demo.core.domain.service.interfaces;

import java.util.Optional;

import com.example.demo.core.domain.model.ERole;
import com.example.demo.core.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
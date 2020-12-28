package com.tinFInale.security.repository;

import com.tinFInale.security.model.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    UserRole findByRole(String role);
}

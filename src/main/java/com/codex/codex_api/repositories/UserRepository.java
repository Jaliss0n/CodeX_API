package com.codex.codex_api.repositories;

import com.codex.codex_api.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;


public interface UserRepository extends JpaRepository<Users, UUID> {
    UserDetails findByLogin(String login);
}

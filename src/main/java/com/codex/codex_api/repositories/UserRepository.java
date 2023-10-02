package com.codex.codex_api.repositories;

import com.codex.codex_api.models.AccessUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;


public interface UserRepository extends JpaRepository<AccessUserModel, UUID> { //Se der erro trocar UUID por string
    UserDetails findByLogin(String login);
}

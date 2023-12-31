package com.codex.codex_api.repositories;

import com.codex.codex_api.models.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AvatarRepository extends JpaRepository<Avatar, UUID> {
    Optional<Avatar> findByNameAvatar(String fileName);
}

package com.codex.codex_api.repositories;

import com.codex.codex_api.models.AvatarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AvatarRepository extends JpaRepository<AvatarModel, UUID> {
}

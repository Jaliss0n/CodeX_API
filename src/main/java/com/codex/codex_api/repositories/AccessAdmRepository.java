package com.codex.codex_api.repositories;

import com.codex.codex_api.models.AccessUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccessAdmRepository extends JpaRepository<AccessUserModel, UUID> {

}

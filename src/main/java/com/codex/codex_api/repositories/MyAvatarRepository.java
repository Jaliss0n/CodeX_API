package com.codex.codex_api.repositories;


import com.codex.codex_api.models.Users;
import com.codex.codex_api.models.MyAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MyAvatarRepository extends JpaRepository<MyAvatar, UUID> {}

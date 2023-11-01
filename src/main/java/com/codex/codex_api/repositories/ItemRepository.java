package com.codex.codex_api.repositories;

import com.codex.codex_api.models.Item;
import com.codex.codex_api.models.MyAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
    Optional<Item> findByNameItem(String fileName);
}
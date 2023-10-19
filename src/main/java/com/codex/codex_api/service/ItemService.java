package com.codex.codex_api.service;

import com.codex.codex_api.controllers.ItemController;
import com.codex.codex_api.controllers.UsersController;
import com.codex.codex_api.dtos.AccessUserDto;
import com.codex.codex_api.dtos.ItemDto;
import com.codex.codex_api.exceptions.NotCreated;
import com.codex.codex_api.exceptions.NotFound;
import com.codex.codex_api.models.Item;
import com.codex.codex_api.models.Users;
import com.codex.codex_api.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public ResponseEntity<Item> saveItem(ItemDto itemDto) {
        try {
            var item = new Item();
            BeanUtils.copyProperties(itemDto, item);
            Item savedItem = itemRepository.save(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        } catch (Exception e) {
            throw new NotCreated();
        }

    }

    public ResponseEntity<List<Item>> getAllItems() {
        try {
            List<Item> itemList = itemRepository.findAll();
            if(!itemList.isEmpty()) {
                for (Item item : itemList) {
                    UUID id = item.getIdItem();
                    item.add(linkTo(methodOn(ItemController.class).getOneItem(id)).withSelfRel());
                }
            }
            return ResponseEntity.ok().body(itemList);
        } catch (Exception e) {
            throw new NotFound();
        }
    }

    public ResponseEntity<Object> getOneItem(UUID id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) {
            throw new NotFound();
        }
        itemOptional.get().add(linkTo(methodOn(ItemController.class).getAllItems()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(itemOptional.get());
    }

    public ResponseEntity<Object> updateItem(UUID id, ItemDto itemDto) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()){
            throw new NotFound();
        } else {
            var item = itemOptional.get();
            BeanUtils.copyProperties(itemDto, item);
            return ResponseEntity.status(HttpStatus.OK).body(itemRepository.save(item));
        }

    }

    public ResponseEntity<Object> deleteItem(UUID id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isEmpty()) {
            throw new NotFound();
        }
        Item item = itemOptional.get();
        itemRepository.delete(item);
        return ResponseEntity.status(HttpStatus.OK).body("Item successfully deleted.");
    }
}



package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.ItemDto;
import com.codex.codex_api.models.ItemModel;
import com.codex.codex_api.repositories.ItemRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @PostMapping("/item")
    public ResponseEntity<ItemModel> saveItem(@RequestBody @Valid ItemDto itemDto) {
        var itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDto, itemModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemRepository.save(itemModel));
    }

    @GetMapping("/item")
    public ResponseEntity<List<ItemModel>> getAllItems() {
        List<ItemModel> itemModelList = itemRepository.findAll();
        if(!itemModelList.isEmpty()) {
            for(ItemModel itemList : itemModelList) {
                UUID id = itemList.getIdItem();
                itemList.add(linkTo(methodOn(ItemController.class).getOneItem(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(itemModelList);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<Object> getOneItem(@PathVariable(value = "id") UUID id){
        Optional<ItemModel> itemO = itemRepository.findById(id);
        if(itemO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        itemO.get().add(linkTo(methodOn(ItemController.class).getAllItems()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(itemO.get());
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable(value = "id") UUID id,
                                               @RequestBody @Valid ItemDto itemDto) {
        Optional<ItemModel> itemO = itemRepository.findById(id);
        if(itemO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        var itemModel = itemO.get();
        BeanUtils.copyProperties(itemDto, itemModel);
        return ResponseEntity.status(HttpStatus.OK).body(itemRepository.save(itemModel));
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable(value = "id") UUID id) {
        Optional<ItemModel> itemO = itemRepository.findById(id);
        if(itemO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        itemRepository.delete(itemO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Item deleted successfully.");
    }
}

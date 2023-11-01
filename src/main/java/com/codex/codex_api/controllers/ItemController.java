package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.ItemDto;
import com.codex.codex_api.models.Item;
import com.codex.codex_api.repositories.ItemRepository;
import com.codex.codex_api.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RequiredArgsConstructor
@RestController
@RequestMapping("item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/new")
    public ResponseEntity<Item> saveItem(@RequestBody @Valid ItemDto itemDto) {
        return itemService.saveItem(itemDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Item>> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Object> getOneItem(@PathVariable(value = "id") UUID id){
        return itemService.getOneItem(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable(value = "id") UUID id,
                                               @RequestBody @Valid ItemDto itemDto) {
       return itemService.updateItem(id, itemDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable(value = "id") UUID id) {
       return itemService.deleteItem(id);
    }

    @PutMapping("/img/upload/{id}")
    public ResponseEntity<?> uploadImageToFileSystem(@PathVariable(value = "id") UUID id, @RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = itemService.uploadImageItem(file, id);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }

    @DeleteMapping("/img/delete/{id}")
    public ResponseEntity<?> deleteImageItem(@PathVariable(value = "id") UUID id) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.deleteImageItem(id));
    }

    @GetMapping("/img/view/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte [] imageData = itemService.downloadImageItem(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
    }

}

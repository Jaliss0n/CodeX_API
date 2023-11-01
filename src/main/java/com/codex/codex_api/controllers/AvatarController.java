package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.AvatarDto;
import com.codex.codex_api.models.Avatar;
import com.codex.codex_api.service.AvatarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping("/new")
    public ResponseEntity<Avatar> saveAvatar(@RequestBody @Valid AvatarDto avatarDto) {
        return avatarService.saveAvatar(avatarDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Avatar>> getAllAvatar() {
        return avatarService.getAllAvatars();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Object> getOneAvatar(@PathVariable(value = "id") UUID id){
        return avatarService.getOneAvatar(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateAvatar(@PathVariable(value = "id") UUID id,
                                                  @RequestBody @Valid AvatarDto avatarDto) {
        return avatarService.updateAvatar(id, avatarDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteAvatar(@PathVariable(value = "id") UUID id) throws IOException {
        return avatarService.deleteAvatar(id);
    }

    @PutMapping("/img/upload/{id}")
    public ResponseEntity<?> uploadImageToFileSystem(@PathVariable(value = "id") UUID id, @RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = avatarService.uploadImageAvatar(file, id);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }

    @DeleteMapping("/img/delete/{id}")
    public ResponseEntity<?> deleteImageAvatar(@PathVariable(value = "id") UUID id) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(avatarService.deleteImageAvatar(id));
    }

    @GetMapping("/img/view/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte [] imageData = avatarService.downloadImageAvatar(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
    }
}

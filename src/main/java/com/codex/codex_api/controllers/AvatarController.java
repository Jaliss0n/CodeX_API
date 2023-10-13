package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.AvatarDto;
import com.codex.codex_api.models.Avatar;
import com.codex.codex_api.repositories.AvatarRepository;
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
public class AvatarController {

    @Autowired
    AvatarRepository avatarRepository;

    @PostMapping("/avatar")
    public ResponseEntity<Avatar> saveAvatar(@RequestBody @Valid AvatarDto avatarDto) {
        var avatarModel = new Avatar();
        BeanUtils.copyProperties(avatarDto, avatarModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(avatarRepository.save(avatarModel));
    }

    @GetMapping("/avatar")
    public ResponseEntity<List<Avatar>> getAllAvatar() {
        List<Avatar> avatarModelList = avatarRepository.findAll();
        if(!avatarModelList.isEmpty()) {
            for(Avatar avatarList : avatarModelList) {
                UUID id = avatarList.getIdAvatar();
                avatarList.add(linkTo(methodOn(AvatarController.class).getOneAvatar(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(avatarModelList);
    }

    @GetMapping("/avatar/{id}")
    public ResponseEntity<Object> getOneAvatar(@PathVariable(value = "id") UUID id){
        Optional<Avatar> avatarO = avatarRepository.findById(id);
        if(avatarO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avatar not found.");
        }
        avatarO.get().add(linkTo(methodOn(AvatarController.class).getAllAvatar()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(avatarO.get());
    }

    @PutMapping("/avatar/{id}")
    public ResponseEntity<Object> updateAvatar(@PathVariable(value = "id") UUID id,
                                                  @RequestBody @Valid AvatarDto avatarDto) {
        Optional<Avatar> avatarO = avatarRepository.findById(id);
        if(avatarO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avatar not found.");
        }
        var avatarModel = avatarO.get();
        BeanUtils.copyProperties(avatarDto, avatarModel);
        return ResponseEntity.status(HttpStatus.OK).body(avatarRepository.save(avatarModel));
    }

    @DeleteMapping("/avatar/{id}")
    public ResponseEntity<Object> deleteAvatar(@PathVariable(value = "id") UUID id) {
        Optional<Avatar> avatarO = avatarRepository.findById(id);
        if(avatarO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avatar not found.");
        }
        avatarRepository.delete(avatarO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Avatar deleted successfully.");
    }
}

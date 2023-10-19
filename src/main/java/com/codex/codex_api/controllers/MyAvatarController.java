package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.MyAvatarDto;
import com.codex.codex_api.dtos.MyAvatarItemDTO;
import com.codex.codex_api.dtos.MyAvatarUpdateAvatarDto;
import com.codex.codex_api.models.MyAvatar;
import com.codex.codex_api.service.MyAvatarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RequiredArgsConstructor
@RestController
@RequestMapping("avatar")
public class MyAvatarController {


    private final MyAvatarService myAvatarService;

    @PostMapping("/my/new")
    public ResponseEntity<MyAvatar> saveMyAvatar(@RequestBody @Valid MyAvatarDto myAvatarDto) {
        return myAvatarService.saveMyAvatar(myAvatarDto);
    }

    @GetMapping("/my/list")
    public ResponseEntity<List<MyAvatar>> getAllMyAvatar() {
       return myAvatarService.getAllMyAvatar();
    }

    @PutMapping("/my/update/{id}")
    public ResponseEntity<Object> updateAvatar(@PathVariable(value = "id") UUID id,
                                               @RequestBody @Valid MyAvatarUpdateAvatarDto myAvatarUpdateAvatarDto ) {
        return myAvatarService.updateMyAvatar(id, myAvatarUpdateAvatarDto);
    }

    @PutMapping("/my/add-item/{id}")
    public ResponseEntity<Object> addItem(@PathVariable(value = "id") UUID id,
                                                  @RequestBody @Valid MyAvatarItemDTO myAvatarItemDTO) {
        return myAvatarService.addItem(id, myAvatarItemDTO);
    }

    @DeleteMapping("/my/remove-item/{id}")
    public ResponseEntity<Object> removeItem(@PathVariable(value = "id") UUID id,
                                          @RequestBody @Valid MyAvatarItemDTO myAvatarItemDTO) {
        return myAvatarService.removeItem(id, myAvatarItemDTO);
    }

    @GetMapping("/my/list/{id}")
    public ResponseEntity<Object> getOneMyAvatar(@PathVariable(value = "id") UUID id){
       return myAvatarService.getOneMyAvatar(id);
    }

    @DeleteMapping("/my/delete/{id}")
    public ResponseEntity<Object> deleteMyAvatar(@PathVariable(value = "id") UUID id) {
       return  myAvatarService.deleteMyAvatar(id);
    }
}

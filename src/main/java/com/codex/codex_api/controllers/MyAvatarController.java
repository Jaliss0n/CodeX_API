package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.MyAvatarDto;
import com.codex.codex_api.models.MyAvatar;
import com.codex.codex_api.repositories.MyAvatarRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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


@RequiredArgsConstructor
@RestController
@RequestMapping("avatar")
public class MyAvatarController {


    @Autowired
    MyAvatarRepository myAvatarRepository;

    @PostMapping("/my/new")
    public ResponseEntity<MyAvatar> saveMyAvatar(@RequestBody @Valid MyAvatarDto myAvatarDto) {

        var myAvatar = new MyAvatar();
        BeanUtils.copyProperties(myAvatarDto, myAvatar);

        MyAvatar savedMyAvatar = myAvatarRepository.save(myAvatar);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMyAvatar);
    }


    @GetMapping("/my/list")
    public ResponseEntity<List<MyAvatar>> getAllMyAvatar() {
        List<MyAvatar> myAvatarModelList = myAvatarRepository.findAll();
        if(!myAvatarModelList.isEmpty()) {
            for(MyAvatar myAvatarList : myAvatarModelList) {
                UUID id = myAvatarList.getIdMyAvatar();
                myAvatarList.add(linkTo(methodOn(MyAvatarController.class).getOneMyAvatar(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(myAvatarModelList);
    }

    @GetMapping("/my/list/{id}")
    public ResponseEntity<Object> getOneMyAvatar(@PathVariable(value = "id") UUID id){
        Optional<MyAvatar> myAvatarO = myAvatarRepository.findById(id);
        if(myAvatarO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("My Avatar not found.");
        }
        myAvatarO.get().add(linkTo(methodOn(MyAvatarController.class).getAllMyAvatar()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(myAvatarO.get());
    }

    @DeleteMapping("/my/delete/{id}")
    public ResponseEntity<Object> deleteMyAvatar(@PathVariable(value = "id") UUID id) {
        Optional<MyAvatar> myAvatar0 = myAvatarRepository.findById(id);
        if(myAvatar0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        myAvatarRepository.delete(myAvatar0.get());
        return ResponseEntity.status(HttpStatus.OK).body("MyAvatar deleted successfully.");
    }
}

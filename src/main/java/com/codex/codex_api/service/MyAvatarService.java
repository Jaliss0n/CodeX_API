package com.codex.codex_api.service;

import com.codex.codex_api.controllers.MyAvatarController;
import com.codex.codex_api.dtos.MyAvatarDto;
import com.codex.codex_api.dtos.MyAvatarItemDTO;
import com.codex.codex_api.models.Item;
import com.codex.codex_api.models.MyAvatar;
import com.codex.codex_api.repositories.ItemRepository;
import com.codex.codex_api.repositories.MyAvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class MyAvatarService {

    private final MyAvatarRepository myAvatarRepository;
    private final ItemRepository itemRepository;

    public ResponseEntity<MyAvatar> saveMyAvatar(MyAvatarDto myAvatarDto) {
        var myAvatar = new MyAvatar();
        BeanUtils.copyProperties(myAvatarDto, myAvatar);
        MyAvatar savedMyAvatar = myAvatarRepository.save(myAvatar);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMyAvatar);
    }

    public ResponseEntity<Object> addItem(UUID id, MyAvatarItemDTO myAvatarItemDTO) {
        Optional<MyAvatar> myAvatarOptional = myAvatarRepository.findById(id);
        if (myAvatarOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MyAvatar not found in this database.");
        }
        MyAvatar myAvatar = myAvatarOptional.get();

        Item item = itemRepository.findById(myAvatarItemDTO.idItem()).orElse(null);

        if(myAvatar != null && item != null) {
            myAvatar.getItems().add(item);
            item.getMyAvatars().add(myAvatar);

            myAvatarRepository.save(myAvatar);
            itemRepository.save(item);

            return ResponseEntity.status(HttpStatus.OK).body("Itens associados com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in this database.");
        }

    }

    public ResponseEntity<Object> removeItem(UUID id, MyAvatarItemDTO myAvatarItemDTO) {
        Optional<MyAvatar> myAvatarOptional = myAvatarRepository.findById(id);
        if (myAvatarOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MyAvatar not found in this database.");
        }
        MyAvatar myAvatar = myAvatarOptional.get();

        Item item = itemRepository.findById(myAvatarItemDTO.idItem()).orElse(null);

        if(myAvatar != null && item != null) {
            myAvatar.getItems().remove(item);
            item.getMyAvatars().remove(myAvatar);

            myAvatarRepository.save(myAvatar);
            itemRepository.save(item);

            return ResponseEntity.status(HttpStatus.OK).body("Iten removido com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in this database.");
        }

    }

    public List<MyAvatar> getAllMyAvatar() {
        List<MyAvatar> myAvatarList = myAvatarRepository.findAll();
        if(!myAvatarList.isEmpty()) {
            for (MyAvatar myAvatar : myAvatarList) {
                UUID id = myAvatar.getIdMyAvatar();
                myAvatar.add(linkTo(methodOn(MyAvatarController.class).getOneMyAvatar(id)).withSelfRel());
            }
        }
        return myAvatarList;
    }

    public ResponseEntity<Object> getOneMyAvatar(UUID id) {
        Optional<MyAvatar> myAvatar0 = myAvatarRepository.findById(id);
        if (myAvatar0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avatar not found.");
        }
        myAvatar0.get().add(linkTo(methodOn(MyAvatarController.class).getAllMyAvatar()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(myAvatar0.get());
    }

    public ResponseEntity<Object> updateMyAvatar(UUID id, MyAvatarDto myAvatarDto) {
        Optional<MyAvatar> myAvatar0 = myAvatarRepository.findById(id);
        if (myAvatar0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MyAvatar not found, in this database.");
        }
        var myAvatar = myAvatar0.get();
        BeanUtils.copyProperties(myAvatarDto, myAvatar);
        return ResponseEntity.status(HttpStatus.OK).body(myAvatarRepository.save(myAvatar));
    }

    public ResponseEntity<Object> deleteMyAvatar(UUID id) {
        Optional<MyAvatar> myAvatarOptional = myAvatarRepository.findById(id);

        if (myAvatarOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MyAvatar not found in this database");
        }
        MyAvatar myAvatar = myAvatarOptional.get();

        myAvatarRepository.delete(myAvatar);

        return ResponseEntity.status(HttpStatus.OK).body("MyAvatar deleted successfully.");
    }
}

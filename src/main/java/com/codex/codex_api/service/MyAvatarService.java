package com.codex.codex_api.service;

import com.codex.codex_api.controllers.MyAvatarController;
import com.codex.codex_api.dtos.MyAvatarDto;
import com.codex.codex_api.dtos.MyAvatarItemDTO;
import com.codex.codex_api.dtos.MyAvatarUpdateAvatarDto;
import com.codex.codex_api.exceptions.ItemAlreadyExists;
import com.codex.codex_api.exceptions.NotCreated;
import com.codex.codex_api.exceptions.NotFound;
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
       try {
           var myAvatar = new MyAvatar();
            BeanUtils.copyProperties(myAvatarDto, myAvatar);
            MyAvatar savedMyAvatar = myAvatarRepository.save(myAvatar);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMyAvatar);

       } catch (Exception e) {
            throw new NotCreated();
       }
    }

    public ResponseEntity<Object> addItem(UUID id, MyAvatarItemDTO myAvatarItemDTO) {

        Optional<MyAvatar> myAvatarOptional = myAvatarRepository.findById(id);

        if (myAvatarOptional.isEmpty()) {
            throw new NotFound();
        } else {
            MyAvatar myAvatar = myAvatarOptional.get();
            Item item = itemRepository.findById(myAvatarItemDTO.idItem()).orElse(null);

            if (myAvatar != null && item != null) {
                if (!myAvatar.getItems().contains(item)) {
                    myAvatar.getItems().add(item);
                    item.getMyAvatars().add(myAvatar);

                    myAvatarRepository.save(myAvatar);
                    itemRepository.save(item);

                    return ResponseEntity.ok().body("Item added successfully.");
                } else {
                    throw new ItemAlreadyExists();
                }
            } else {
                throw new NotCreated();
            }
        }

    }

    public ResponseEntity<Object> removeItem(UUID id, MyAvatarItemDTO myAvatarItemDTO) {
        Optional<MyAvatar> myAvatarOptional = myAvatarRepository.findById(id);

        if (myAvatarOptional.isEmpty()) {
            throw new NotFound();
        } else {
            MyAvatar myAvatar = myAvatarOptional.get();

            if (myAvatarItemDTO != null) {
                Item item = itemRepository.findById(myAvatarItemDTO.idItem()).orElse(null);

                if (item != null) {
                    myAvatar.getItems().remove(item);
                    item.getMyAvatars().remove(myAvatar);
                    myAvatarRepository.save(myAvatar);
                    itemRepository.save(item);
                    return ResponseEntity.ok().body("Item removido com sucesso.");
                } else {
                    throw new NotCreated();
                }
            } else {
                for (Item item : myAvatar.getItems()) {
                    item.getMyAvatars().remove(myAvatar);
                }
                myAvatar.getItems().clear();
                myAvatarRepository.save(myAvatar);
                return ResponseEntity.ok().body("Todos os itens foram removidos com sucesso.");
            }
        }
    }

    public ResponseEntity<List<MyAvatar>> getAllMyAvatar() {
        try {
            List<MyAvatar> myAvatarList = myAvatarRepository.findAll();
            if(!myAvatarList.isEmpty()) {
                for (MyAvatar myAvatar : myAvatarList) {
                    UUID id = myAvatar.getIdMyAvatar();
                    myAvatar.add(linkTo(methodOn(MyAvatarController.class).getOneMyAvatar(id)).withSelfRel());
                }
            }

            return ResponseEntity.ok().body(myAvatarList);
        } catch (Exception e) {
            throw new NotFound();
        }

    }

    public ResponseEntity<Object> getOneMyAvatar(UUID id) {
        Optional<MyAvatar> myAvatar0 = myAvatarRepository.findById(id);
        if (myAvatar0.isEmpty()) {
            throw new NotFound();
        }
        myAvatar0.get().add(linkTo(methodOn(MyAvatarController.class).getAllMyAvatar()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(myAvatar0.get());
    }

    public ResponseEntity<Object> updateMyAvatar(UUID id, MyAvatarUpdateAvatarDto myAvatarUpdateAvatarDto) {
        Optional<MyAvatar> myAvatar0 = myAvatarRepository.findById(id);
        if (myAvatar0.isEmpty()){
            throw new NotFound();
        } else {
            var myAvatar = myAvatar0.get();
            BeanUtils.copyProperties(myAvatarUpdateAvatarDto, myAvatar);
            return ResponseEntity.status(HttpStatus.OK).body(myAvatarRepository.save(myAvatar));
        }

    }

    public ResponseEntity<Object> deleteMyAvatar(UUID id) {
        Optional<MyAvatar> myAvatarOptional = myAvatarRepository.findById(id);

        if (myAvatarOptional.isEmpty()) {
            throw new NotFound();
        }
        MyAvatar myAvatar = myAvatarOptional.get();

        myAvatarRepository.delete(myAvatar);

        return ResponseEntity.ok().body("MyAvatar deleted with successfully.");

    }
}

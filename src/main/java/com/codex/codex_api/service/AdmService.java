package com.codex.codex_api.service;

import com.codex.codex_api.controllers.UsersController;
import com.codex.codex_api.dtos.AccessUserDto;
import com.codex.codex_api.models.Users;
import com.codex.codex_api.repositories.MyAvatarRepository;
import com.codex.codex_api.repositories.UserRepository;
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

public class AdmService {

    private final UserRepository userRepository;
    private final MyAvatarRepository myAvatarRepository;

    public Users saveAdm(AccessUserDto accessUserDto) {
        var accessAdmModel = new Users();
        BeanUtils.copyProperties(accessUserDto, accessAdmModel);
        return userRepository.save(accessAdmModel);
    }

    public List<Users> getAllAdm() {
        List<Users> userList = userRepository.findAll();
        if(!userList.isEmpty()) {
            for (Users user : userList) {
                UUID id = user.getIdAccess();
                user.add(linkTo(methodOn(UsersController.class).getOneAccessAdm(id)).withSelfRel());
            }
        }
        return userList;

    }

    public ResponseEntity<Object> getOneAccessAdm(UUID id) {
        Optional<Users> accessAdmO = userRepository.findById(id);
        if (accessAdmO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adm not found.");
        }
        accessAdmO.get().add(linkTo(methodOn(UsersController.class).getAllAccessAdm()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(accessAdmO.get());
    }

    public ResponseEntity<Object> updateUser(UUID id, AccessUserDto accessUserDto) {
        Optional<Users> user0 = userRepository.findById(id);
        if (user0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found, in this database.");
        }
        var userModel = user0.get();
        BeanUtils.copyProperties(accessUserDto, userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }

    public ResponseEntity<Object> deleteUser(UUID id) {
        Optional<Users> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in this database");
        }
        Users user = userOptional.get();

        if (user.getMyAvatar() != null) {
            myAvatarRepository.delete(user.getMyAvatar());
        }
        userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body("User and related MyAvatar deleted successfully.");
    }
}

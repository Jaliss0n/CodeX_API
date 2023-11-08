package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.AccessUserDto;
import com.codex.codex_api.dtos.MyAvatarItemDTO;
import com.codex.codex_api.external.api.CodebankApi;
import com.codex.codex_api.models.Users;
import com.codex.codex_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UsersController {

    private final UserService userService;

    @PostMapping("/new")
    public ResponseEntity<Users> saveAdm(@RequestBody @Valid AccessUserDto accessUserDto) {
        return userService.saveAdm(accessUserDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Users>> getAllAccessAdm(){

        return userService.getAllAdm();

    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Object> getOneAccessAdm(@PathVariable(value = "id") UUID id) {
        return userService.getOneAccessAdm(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateAccessAdm(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid AccessUserDto accessUserDto) {
        return userService.updateUser(id, accessUserDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteAccessAdm(@PathVariable(value = "id") UUID id) {
        return userService.deleteUser(id);
    }



}

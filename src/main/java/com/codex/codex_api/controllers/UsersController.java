package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.AccessUserDto;
import com.codex.codex_api.models.Users;
import com.codex.codex_api.service.AdmService;
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

    private final  AdmService admService;

    @PostMapping("/new")
    public ResponseEntity<Users> saveAdm(@RequestBody @Valid AccessUserDto accessUserDto) {
        return admService.saveAdm(accessUserDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Users>> getAllAccessAdm(){
       List<Users> users = admService.getAllAdm();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Object> getOneAccessAdm(@PathVariable(value = "id") UUID id) {
        return admService.getOneAccessAdm(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateAccessAdm(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid AccessUserDto accessUserDto) {
        return admService.updateUser(id, accessUserDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteAccessAdm(@PathVariable(value = "id") UUID id) {
        return admService.deleteUser(id);
    }

}

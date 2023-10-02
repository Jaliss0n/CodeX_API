package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.AccessUserDto;
import com.codex.codex_api.models.AccessUserModel;
import com.codex.codex_api.repositories.AccessAdmRepository;
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
public class AccessAdmController {

    @Autowired
    AccessAdmRepository accessAdmRepository;

    @PostMapping("/access/adm")
    public ResponseEntity<AccessUserModel> saveAccessAdm(@RequestBody @Valid AccessUserDto accessUserDto) {
        var accessAdmModel = new AccessUserModel();
        BeanUtils.copyProperties(accessUserDto, accessAdmModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(accessAdmRepository.save(accessAdmModel));
    }

    @GetMapping("/access/adm")
    public ResponseEntity<List<AccessUserModel>> getAllAccessAdm(){
        List<AccessUserModel> accessAdmList = accessAdmRepository.findAll();
        if(!accessAdmList.isEmpty()) {
            for(AccessUserModel accessAdm : accessAdmList) {
                UUID id = accessAdm.getIdAccess();
                accessAdm.add(linkTo(methodOn(AccessAdmController.class).getOneAccessAdm(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(accessAdmList);
    }

    @GetMapping("/access/adm/{id}")
    public ResponseEntity<Object> getOneAccessAdm(@PathVariable(value = "id") UUID id){
        Optional<AccessUserModel> accessAdmO = accessAdmRepository.findById(id);
        if(accessAdmO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adm not found.");
        }
        accessAdmO.get().add(linkTo(methodOn(AccessAdmController.class).getAllAccessAdm()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(accessAdmO.get());
    }

    @PutMapping("/access/adm/{id}")
    public ResponseEntity<Object> updateAccessAdm(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid AccessUserDto accessUserDto) {
        Optional<AccessUserModel> accessAdmO = accessAdmRepository.findById(id);
        if(accessAdmO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adm not found.");
        }
        var accessAdmModel = accessAdmO.get();
        BeanUtils.copyProperties(accessUserDto, accessAdmModel);
        return ResponseEntity.status(HttpStatus.OK).body(accessAdmRepository.save(accessAdmModel));
    }

    @DeleteMapping("/access/adm/{id}")
    public ResponseEntity<Object> deleteAccessAdm(@PathVariable(value = "id") UUID id) {
        Optional<AccessUserModel> accessAdmO = accessAdmRepository.findById(id);
        if(accessAdmO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adm not found.");
        }
        accessAdmRepository.delete(accessAdmO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Adm deleted successfully.");
    }
}

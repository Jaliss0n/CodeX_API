package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.*;
import com.codex.codex_api.exceptions.NotFound;
import com.codex.codex_api.external.api.MoodleData;
import com.codex.codex_api.infra.security.TokenService;
import com.codex.codex_api.models.Users;
import com.codex.codex_api.repositories.UserRepository;
import com.codex.codex_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRecordDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Users) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseRecordDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRecordDto data) {
        if(this.repository.findByLogin(data.login()) != null) return  ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Users newUser = new Users(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/unity")
    public ResponseEntity registerUnity(@RequestBody @Valid RegisterUnityDTO data) {
        if(this.repository.findByLogin(data.login()) != null) return  ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Users newUser = new Users(data.login(), encryptedPassword, data.role(), data.name(), data.zipCode(), data.address());

        this.repository.save(newUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/student")
    public ResponseEntity registerStudent(@RequestBody @Valid RegisterStudentDto data) {

        if(this.repository.findByLogin(data.login()) != null) throw new NotFound("User already registred in this database.");

        MoodleData moodleData = userService.getStudentAva(data.login());

        if(!moodleData.getUsers().isEmpty()) {
            System.out.println(moodleData);

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

            for(MoodleData.MoodleUser user : moodleData.getUsers()) {
                Users newUser = new Users(user.getUsername(), encryptedPassword, data.role(), user.getFullname(), user.getCity());
                this.repository.save(newUser);
            }

            return ResponseEntity.ok().build();
        } else {
            throw new NotFound("User not registred in moodle.");
        }
    }
}

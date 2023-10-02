package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.AuthenticationRecordDto;
import com.codex.codex_api.dtos.LoginResponseRecordDto;
import com.codex.codex_api.dtos.RegisterRecordDto;
import com.codex.codex_api.dtos.RegisterUnityDTO;
import com.codex.codex_api.infra.security.TokenService;
import com.codex.codex_api.models.AccessUserModel;
import com.codex.codex_api.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRecordDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((AccessUserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseRecordDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRecordDto data) {
        if(this.repository.findByLogin(data.login()) != null) return  ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        AccessUserModel newUser = new AccessUserModel(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/unity")
    public ResponseEntity registerUnity(@RequestBody @Valid RegisterUnityDTO data) {
        if(this.repository.findByLogin(data.login()) != null) return  ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        AccessUserModel newUser = new AccessUserModel(data.login(), encryptedPassword, data.role(), data.name(), data.zipCode(), data.address());

        this.repository.save(newUser);
        return ResponseEntity.ok().build();
    }
}

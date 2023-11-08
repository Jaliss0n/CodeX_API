package com.codex.codex_api.service;

import com.codex.codex_api.dtos.*;
import com.codex.codex_api.exceptions.NotFound;
import com.codex.codex_api.external.api.MoodleData;
import com.codex.codex_api.infra.security.TokenService;
import com.codex.codex_api.models.Users;
import com.codex.codex_api.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.authentication.AuthenticationManager;



@Service
@RequiredArgsConstructor
public class AuthenticationService  {

    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final TokenService tokenService;
    private final UserService userService;

    public ResponseEntity login(AuthenticationRecordDto data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Users) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseRecordDto(token));
    }

    public ResponseEntity register(RegisterRecordDto data) {
        if(this.repository.findByLogin(data.login()) != null) return  ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Users newUser = new Users(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity registerUnity(@RequestBody @Valid RegisterUnityDTO data) {
        if(this.repository.findByLogin(data.login()) != null) return  ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Users newUser = new Users(data.login(), encryptedPassword, data.role(), data.name(), data.zipCode(), data.address());

        this.repository.save(newUser);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity registerStudent(@RequestBody @Valid RegisterStudentDto data) {

        if(this.repository.findByLogin(data.login()) != null) throw new NotFound("User already registred in this database.");

        MoodleData moodleData = userService.getStudentAva(data.login());

        if(!moodleData.getUsers().isEmpty()) {

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

            for(MoodleData.MoodleUser user : moodleData.getUsers()) {
                Users newUser = new Users(user.getUsername(), encryptedPassword, data.role(), user.getFullname(), user.getCity(), user.getId());
                this.repository.save(newUser);
            }

            return ResponseEntity.ok().build();
        } else {
            throw new NotFound("User not registred in moodle.");
        }
    }
}

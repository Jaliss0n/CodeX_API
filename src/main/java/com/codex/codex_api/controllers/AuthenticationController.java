package com.codex.codex_api.controllers;

import com.codex.codex_api.dtos.*;
import com.codex.codex_api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRecordDto data) {
        return authenticationService.login(data);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRecordDto data) {
        return authenticationService.register(data);
    }

    @PostMapping("/register/unity")
    public ResponseEntity registerUnity(@RequestBody @Valid RegisterUnityDTO data) {
        return authenticationService.registerUnity(data);
    }

    @PostMapping("/register/student")
    public ResponseEntity registerStudent(@RequestBody @Valid RegisterStudentDto data) {
        return authenticationService.registerStudent(data);
    }
}

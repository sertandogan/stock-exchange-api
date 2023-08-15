package com.stockexchangeapi.controller;

import com.stockexchangeapi.model.request.AuthenticationRequest;
import com.stockexchangeapi.model.request.RegisterRequest;
import com.stockexchangeapi.model.response.AuthenticationResponse;
import com.stockexchangeapi.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return service.authenticate(request);
    }


}

package com.stockexchangeapi.service;


import com.stockexchangeapi.domain.User;
import com.stockexchangeapi.exceptions.UserAlreadyExistsException;
import com.stockexchangeapi.model.request.AuthenticationRequest;
import com.stockexchangeapi.model.request.RegisterRequest;
import com.stockexchangeapi.model.response.AuthenticationResponse;
import com.stockexchangeapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(StringUtils.lowerCase(request.getEmail()))
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = repository.findByEmail(StringUtils.lowerCase(request.getEmail()));
        if (user.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        var newUser = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(StringUtils.lowerCase(request.getEmail()))
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(newUser);
        var jwtToken = jwtService.generateToken(newUser);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}

package com.example.FitnessTacker.controller;

import com.example.FitnessTacker.Model.User;
import com.example.FitnessTacker.Security.JWTUtils;
import com.example.FitnessTacker.dto.LoginRequest;
import com.example.FitnessTacker.dto.LoginResponse;
import com.example.FitnessTacker.dto.RegisterRequest;
import com.example.FitnessTacker.dto.UserResponse;
import com.example.FitnessTacker.repository.UserRepository;
import com.example.FitnessTacker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JWTUtils jwtUtils;

    @PostMapping("/register")               // @Valid is used to enable the validation that we add on the DTO Class.
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.register(registerRequest));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        try{
            User user = userService.authenticate(loginRequest);
           String token = jwtUtils.generateToken(user.getId() , user.getRole().name());
           return ResponseEntity.ok(new LoginResponse(
                   token , userService.mapToResponse(user)
           ));

        }catch (AuthenticationException e){
            return ResponseEntity.status(401).build();
        }

    }

}

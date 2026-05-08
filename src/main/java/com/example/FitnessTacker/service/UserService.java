package com.example.FitnessTacker.service;

import com.example.FitnessTacker.Model.User;
import com.example.FitnessTacker.Model.UserRole;
import com.example.FitnessTacker.dto.LoginRequest;
import com.example.FitnessTacker.dto.RegisterRequest;
import com.example.FitnessTacker.dto.UserResponse;
import com.example.FitnessTacker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.bytecode.internal.bytebuddy.PassThroughInterceptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) {
        UserRole role = request.getRole() != null ? request.getRole() : UserRole.USER;
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email Already Exits. Use a Different One");
        }

        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        // here we do not pass createdAt and updatedAt So these value set as NULL.
//        User user = new User(
//                null,
//                request.getEmail(),
//                request.getPassword(),
//                request.getFirstName(),
//                request.getLastName(),
//                Instant.parse("2026-01-03T10:15:30Z").atZone(ZoneOffset.UTC).toLocalDateTime(),
//                Instant.parse("2026-02-03T10:15:30Z").atZone(ZoneOffset.UTC).toLocalDateTime(),
//                List.of(),
//                List.of()
//        );


        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }


    public UserResponse mapToResponse(User savedUser) {
        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setPassword(savedUser.getPassword());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
//        response.setCreatedAt(savedUser.getCreatedAt());
//        response.setUpdatedAt(savedUser.getUpdatedAt());
        return response;
    }


    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // for the check email and password of user during login.
    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("Invalid Email"));


        if(!passwordEncoder.matches(loginRequest.getPassword() , user.getPassword()))
            throw new RuntimeException("Invalid Password");

        return user;
    }
}

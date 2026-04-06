package com.example.FitnessTacker.dto;

import com.example.FitnessTacker.Model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email is Required.")
    @Email(message = "Email is Invalid")
    private String email;

    @NotBlank(message = "Password is Required.")
    private String password;

    private String firstName;
    private String lastName;
    private UserRole role;


}

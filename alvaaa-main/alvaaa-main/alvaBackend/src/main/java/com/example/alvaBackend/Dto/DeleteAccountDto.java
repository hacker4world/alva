package com.example.alvaBackend.Dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public class DeleteAccountDto {
    @NotEmpty(message = "Account email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotEmpty(message = "Account matriculeNumber is required")
    @Size(min = 3, max = 8, message = "Account matriculeNumber must at least have 8 characters long")
    private String matriculeNumber;


    public @NotEmpty(message = "Account email is required") @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "Account email is required") @Email(message = "Invalid email format") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "Account password is required") @Size(min = 3, max = 8, message = "Account password must at least have 8 characters long") String getMatriculeNumber() {
        return matriculeNumber;
    }

    public void setMatriculeNumber(@NotEmpty(message = "Account password is required") @Size(min = 8, max = 8, message = "Account password must at least have 8 characters long") String matriculeNumber) {
        this.matriculeNumber = matriculeNumber;
    }
}

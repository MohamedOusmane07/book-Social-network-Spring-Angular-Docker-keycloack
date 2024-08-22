package com.lamine.book.auth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "firstName is mandatory")
    @NotBlank(message = "firstName is mandatory")
    private String firstname;
    @NotEmpty(message = "lastName is mandatory")
    @NotBlank(message = "lastName is mandatory")
    private String lastname;
    @Email(message = "Email is not formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotEmpty(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters minimum")
    private String password;
}

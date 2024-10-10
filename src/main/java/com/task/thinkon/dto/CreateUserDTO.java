package com.task.thinkon.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateUserDTO {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "\\+?[0-9]+", message = "Phone number should be valid")
    private String phoneNumber;
}

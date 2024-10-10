package com.task.thinkon.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is mandatory")
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "First name is mandatory")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "\\+?[0-9]+", message = "Phone number should be valid")
    @Column(nullable = false)
    private String phoneNumber;
}

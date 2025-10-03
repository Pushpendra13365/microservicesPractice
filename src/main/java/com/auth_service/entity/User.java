package com.auth_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user", schema = "auth")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is mandatory")
    @NotNull(message = "Username must be between 3 to 100 character")
    @Size(min = 3, max = 100)
    @Column(name = "user_name")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @NotNull(message = "Password must be between 10 to 100 character")
    @Size(min = 10, max = 100)
    @Column(name = "password")
    private String password;
    private int active;

    @Column(name = "first_login", nullable = false)
    private int firstLogin;

}

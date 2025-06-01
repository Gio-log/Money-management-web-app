package com.example.pasir_kochanski_cezary.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity // informuje Springą, że klasa jest encją mapowaną na tabelę w bazie danych
@Table(name = "users")
public class User {
    @Id // pole 'id' to klucz główny
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    private String username;

    @Email(message = "Podaj poprawny adres e-mail")
    @NotBlank(message = "Email jest wymagany")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;

    private String currency = "PLN";
}
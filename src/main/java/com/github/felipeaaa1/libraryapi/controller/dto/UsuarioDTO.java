package com.github.felipeaaa1.libraryapi.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UsuarioDTO(
        @NotNull(message = "Campo obrigatório")
        String login,
        @NotNull(message = "Campo obrigatório")
        @Email (message = "Inválido")
        String email,
        @NotNull(message = "Campo obrigatório")
        String senha,
        List<String> roles
) {}

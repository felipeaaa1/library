package com.github.felipeaaa1.libraryapi.controller.dto;

import com.github.felipeaaa1.libraryapi.model.enums.GeneroLivro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ResponseLivroDTO (
        UUID id,
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        AutorDTO autor
){

}

package com.github.felipeaaa1.libraryapi.controller.dto;

import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.model.enums.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RequestLivroDTO (
//                    @ISBN
                    @NotBlank(message = "Campo obrigatório")
                    String isbn,
                    @NotBlank(message = "Campo obrigatório")
                    @Size(min = 2, max = 150, message = "Campo fora do tamanho padrão")
                    String titulo,
                    @NotNull(message = "Campo obrigatório")
                    @Past(message = "Não pode ser uma data futura")
                    LocalDate dataPublicacao,
                    @NotNull(message = "Campo obrigatório")
                    GeneroLivro genero,
                    BigDecimal preco,
                    @NotNull(message = "Campo obrigatório")
                    UUID idAutor
                ) {
}

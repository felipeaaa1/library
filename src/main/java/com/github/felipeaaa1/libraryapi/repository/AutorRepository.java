package com.github.felipeaaa1.libraryapi.repository;

import com.github.felipeaaa1.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
    Optional<Autor> findByNome(String nome);

    ;
}

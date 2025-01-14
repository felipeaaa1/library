package com.github.felipeaaa1.libraryapi.repository;

import com.github.felipeaaa1.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
    List<Autor> findByNomeLike(String nome);

    List<Autor> findByNomeLikeAndNacionalidade(String nome, String nacionalidade);

    List<Autor> findByNacionalidade(String nacionalidade);

    Optional findByNome(String nome);


    ;
}

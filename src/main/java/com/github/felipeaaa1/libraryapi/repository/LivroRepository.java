package com.github.felipeaaa1.libraryapi.repository;

import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.model.enums.GeneroLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {


    boolean existsByAutor(Autor autor);

    List<Livro> findByTitulo(String titulo);

    List<Livro> findByAutorAndTitulo(Autor autor, String titulo);

    List<Livro> findByTituloLikeAndDataPublicacaoAfter(String titulo, LocalDate dataPublicacao);

    @Query("select l from Livro l order by l.titulo")
    List<Livro> selectTdos();

    @Query("select a from Livro l join l.autor a order by a.nome")
    List<Autor> selectComJoin();

    @Query("""
            select l from Livro l
            where l.genero = :genero 
            and dataPublicacao >= :dataInicio
            """)
    List<Livro> findLivrosbyGenero(
            @Param("genero")GeneroLivro generoLivro,
            @Param("dataInicio")LocalDate dataInicio
            );

    @Modifying
    @Transactional
    @Query("delete from Livro where genero = ?1")
    void deleteByGenero(GeneroLivro generoLivro);

    @Modifying
    @Transactional
    @Query("update Livro l set l.titulo = :novoTitulo where l.id = :id ")
    void atualizaPorId(@Param("id") UUID id, @Param("novoTitulo") String titulo);

    Optional<Livro> findByIsbnOrTitulo(String isbn, String titulo);
}

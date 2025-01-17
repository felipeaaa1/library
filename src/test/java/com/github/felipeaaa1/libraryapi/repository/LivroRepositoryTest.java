package com.github.felipeaaa1.libraryapi.repository;

import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.model.enums.GeneroLivro;
import jakarta.transaction.Transactional;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;


    @Test
    public void salvarLivro(){
        Livro livro = new Livro();
        livro.setTitulo("teste datas 2");
        livro.setIsbn("8312219-1212");
        livro.setPreco(BigDecimal.valueOf(91));
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setDataPublicacao(LocalDate.of(2020,10, 31));

        Autor autor = autorRepository.findById(UUID.fromString("3c1ade85-54e9-4bbc-a834-c9d1d182bfe5"))
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
        livro.setAutor(autor);

        livroRepository.save(livro);
    }


    @Test
    public void salvarLivroAutorCascade(){
        Livro livro = new Livro();
        livro.setTitulo("teste livro cascade");
        livro.setIsbn("83829-1212");
        livro.setPreco(BigDecimal.valueOf(91));
        livro.setGenero(GeneroLivro.MISTERIO);
        livro.setDataPublicacao(LocalDate.of(2024,10, 31));

        Autor autor = new Autor();
        autor.setNome("teste cascade");
        autor.setNacionalidade("coreano");
        autor.setDataNascimento(LocalDate.of(1999, Month.APRIL,10));

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    public void atualizarLivro(){
        Autor autorNovo = autorRepository.findById(
                UUID.fromString("368a1d03-c63d-47d8-b990-7644508e667b")
                ).get();

        Livro livro = livroRepository.findById(
                UUID.fromString("d02013dc-e621-4169-b257-c48fbf5a7ade")
                ).get();
        System.out.println("livro: ");
        System.out.println(livro);
        System.out.println("Autor novo:");
        System.out.println(autorNovo);

        livro.setAutor(autorNovo);
        Livro save = livroRepository.save(livro);
        System.out.println("Livro Autolizado");
        System.out.println(save);

    }

    @Test
    public void listarPorAutorETitulo(){


        System.out.println("teste inicializado");
        Autor autor = autorRepository.findById(UUID.fromString("368a1d03-c63d-47d8-b990-7644508e667b")).get();
        livroRepository.findByTituloLikeAndDataPublicacaoAfter( "%teste%", LocalDate.of(2000,01,01)).forEach(System.out::println);
        System.out.println("teste finalizado");
    }

    @Test
    public void listarTodosJQLtest(){
        List<Livro> livros = livroRepository.selectTdos();
        for (Livro livro : livros){
            System.out.println("-------");
            System.out.println("livro: "+livro.getTitulo());
            System.out.println("Escrito por: "+livro.getAutor().getNome());
            System.out.println("-------");
        }
    }

    @Test
    @Transactional
    public void listarAutorestest(){
        List<Autor> autores = livroRepository.selectComJoin();
        for (Autor autor : autores){
            System.out.println("-------");
            System.out.println("Autor: "+autor.getNome());
            System.out.println("primeiro livro: "+autor.getLivros().getFirst() == null?" sem livros escritor": autor.getLivros().getFirst().getTitulo());
            System.out.println("-------");
        }
    }

    @Test
    public void listarByGeneroTest(){
        for(GeneroLivro genero : GeneroLivro.values()){
            System.out.println("listar livros de: "+ genero);
            livroRepository.findLivrosbyGenero(genero, LocalDate.of(2024, 7,01)).forEach(System.out::println);
        }

    }

    @Test
    public void deletePorGenero(){
        livroRepository.deleteByGenero(GeneroLivro.CIENCIA);
    }

    @Test
    public void autualizarTitulo(){
        UUID idLivro = UUID.fromString("9d15df0e-d348-459d-9657-7776d8b3fa2a");
        livroRepository.atualizaPorId(idLivro, "teste editar com id");
    }
}
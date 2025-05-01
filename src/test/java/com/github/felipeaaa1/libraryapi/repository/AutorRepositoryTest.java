package com.github.felipeaaa1.libraryapi.repository;

import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.model.enums.GeneroLivro;
import com.github.felipeaaa1.libraryapi.security.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository repository;

    @Autowired
    SecurityService securityService;

    @Test
    public void salvarTest(){
        Autor autor = new Autor();
        autor.setNome("teste");
        autor.setNacionalidade("brasileiro");
        autor.setDataNascimento(LocalDate.of(1996, Month.OCTOBER,31));
        autor.setUsuario(securityService.obterUsuarioLogado());

        var autorSalvo = repository.save(autor);
        System.out.println("resultado1: "+autorSalvo);

        Autor autor2 = new Autor();
        autor2.setNome("teste nome 2");
        autor2.setNacionalidade("argentino");
        autor2.setDataNascimento(LocalDate.of(1992, Month.AUGUST,14));

        autor2.setUsuario(securityService.obterUsuarioLogado());
        var autorSalvo2 = repository.save(autor2);
        System.out.println("resultado: "+autorSalvo2);


    }

    @Test
    public void atualizarTest(){
        UUID id = UUID.fromString("3c473119-fce3-4794-9cb6-ed85cc878262");
        Optional<Autor> byId = repository.findById(id);
        if(byId.isPresent()){
            System.out.println("autor: "+byId);

            Autor encontrado = byId.get();
            encontrado.setNome("nome atualizado");

            repository.save(encontrado);

        }
    }

    @Test
    public void listarTest(){

        List<Autor> all = repository.findAll();
        all.forEach(System.out::println);

    }

    @Test
    public void contarTest(){
        System.out.println("Contagem atual: "+repository.count());
    }

    @Test
    public void deletarTest(){
        List<Autor> teste = repository.findByNomeLike("teste");
        if (!teste.isEmpty()) {
            Autor encontrado = teste.getFirst();
            System.out.println("Encontrado :");
            System.out.println(encontrado);
            repository.delete(encontrado);
            System.out.println("Removido");
        }
        else
            System.out.println("Autor não encontrado, teste não executado");
    }

    @Test
    public void salvarComLivrosTest(){
        Autor autor = new Autor();
        autor.setNome("teste com livros");
        autor.setNacionalidade("canadense");
        autor.setDataNascimento(LocalDate.of(1976, Month.APRIL,4));

        Livro livro = new Livro();
        livro.setTitulo("teste livro adicionar autor com livro");
        livro.setIsbn("83829-1212");
        livro.setPreco(BigDecimal.valueOf(81));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setDataPublicacao(LocalDate.of(2024,6, 3));
        livro.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setTitulo("teste livro 2 adicionar autor com livro");
        livro2.setIsbn("83829-1212");
        livro2.setPreco(BigDecimal.valueOf(81));
        livro2.setGenero(GeneroLivro.FANTASIA);
        livro2.setDataPublicacao(LocalDate.of(2024,6, 5));
        livro2.setAutor(autor);


        List<Livro> list = new ArrayList<>();
        list.add(livro);
        list.add(livro2);
        autor.setLivros(list);

        var autorSalvo = repository.save(autor);

    }
}

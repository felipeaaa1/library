package com.github.felipeaaa1.libraryapi.service;

import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.model.enums.GeneroLivro;
import com.github.felipeaaa1.libraryapi.repository.AutorRepository;
import com.github.felipeaaa1.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    AutorRepository autorRepository;

    @Autowired
    LivroRepository livroRepository;

    @Transactional
    public void executar(){

        Autor autor = new Autor();
        autor.setNome("teste transacao");
        autor.setNacionalidade("coreano");
        autor.setDataNascimento(LocalDate.of(1999, Month.APRIL,10));

        autorRepository.saveAndFlush(autor);

        Livro livro = new Livro();
        livro.setTitulo("teste livro cascade");
        livro.setIsbn("83829-1212");
        livro.setPreco(BigDecimal.valueOf(91));
        livro.setGenero(GeneroLivro.MISTERIO);
        livro.setDataPublicacao(LocalDate.of(2024,10, 31));
        livro.setAutor(autor);

        livroRepository.saveAndFlush(livro);

        if (autor.getNome().equals("Colocar nome aqui para dar o erro e não salvar mesmo dando flush"))
            throw new RuntimeException("erro para apagar os dados salvos");

    }


    @Transactional//como tem essa anotação o que eu mandar com o set ja vai salvar no banco
    public void atualizarSemRepositorySave(){
        var livro = livroRepository.findById(
                UUID.fromString("d02013dc-e621-4169-b257-c48fbf5a7ade"))
                .orElse(null);

        livro.setDataPublicacao(LocalDate.of(1999,10,20));
    }
}

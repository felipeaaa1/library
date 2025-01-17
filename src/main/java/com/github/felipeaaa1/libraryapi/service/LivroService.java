package com.github.felipeaaa1.libraryapi.service;

import com.github.felipeaaa1.libraryapi.controller.dto.RequestLivroDTO;
import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.repository.AutorRepository;
import com.github.felipeaaa1.libraryapi.repository.LivroRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

//    public Livro salvar(@Valid RequestLivroDTO requestLivroDTO) {
//        Livro livro = requestLivroDTO.retornaLivro();
//        Autor autor = autorRepository.findById(requestLivroDTO.idAutor()).get();
//        livro.setAutor(autor);
//        livroRepository.save(livro);
//        return livro;
//    }
}

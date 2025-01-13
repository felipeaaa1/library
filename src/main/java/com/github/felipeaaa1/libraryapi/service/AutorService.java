package com.github.felipeaaa1.libraryapi.service;

import com.github.felipeaaa1.libraryapi.controller.dto.AutorDTO;
import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Service;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository){
        this.autorRepository = autorRepository;
    }
    
    public Autor salvar(AutorDTO autorDTO){
        return autorRepository.save(autorDTO.retornaAutor());
    }
}

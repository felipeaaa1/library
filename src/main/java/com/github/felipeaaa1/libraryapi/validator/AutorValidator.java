package com.github.felipeaaa1.libraryapi.validator;

import com.github.felipeaaa1.libraryapi.exception.RegistroDuplicadoException;
import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AutorValidator {

    private final AutorRepository autorRepository;

    public void validar( Autor autor){
        if (registroDuplicado(autor)){
            throw new RegistroDuplicadoException("autor: " + autor.getNome()+", ja existe!");
        }
    }

    private boolean registroDuplicado(Autor autor){
        Optional retorno = autorRepository.findByNome(autor.getNome());
        if (retorno.isPresent())
            return true;
        else
            return false;
    }
}

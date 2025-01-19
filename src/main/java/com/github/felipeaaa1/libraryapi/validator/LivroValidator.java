package com.github.felipeaaa1.libraryapi.validator;

import com.github.felipeaaa1.libraryapi.exception.CampoInvalidoException;
import com.github.felipeaaa1.libraryapi.exception.RegistroDuplicadoException;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {
    private final LivroRepository livroRepository;


    public void validar(Livro livro){
        if (livroJaExiste(livro)){
            throw new RegistroDuplicadoException("Livro ja cadastrado: "+livro.toString());
        }

        if (isPrecoInvalido(livro)){
            throw new CampoInvalidoException("Preco", "Para livros depois de 2020 preço é obrigatório");
        }
        
    }

    private boolean isPrecoInvalido(Livro livro) {
        return livro.getPreco() == null &&
                livro.getDataPublicacao().getYear() >= 2020;
    }

    private Boolean livroJaExiste(Livro livro){
        Optional<Livro> byIsbn = livroRepository.findByIsbnOrTitulo(livro.getIsbn(), livro.getTitulo()
        );
        if (livro.getId() == null){
            return byIsbn.isPresent();
        }
        return byIsbn.map(Livro::getId)
                .stream()
                .anyMatch(uuid -> !uuid.equals(livro.getId()));
    }
}

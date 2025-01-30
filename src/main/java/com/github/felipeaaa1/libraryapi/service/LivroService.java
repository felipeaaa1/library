package com.github.felipeaaa1.libraryapi.service;

import com.github.felipeaaa1.libraryapi.exception.OperacaoNaoPermitidaException;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.model.enums.GeneroLivro;
import com.github.felipeaaa1.libraryapi.repository.AutorRepository;
import com.github.felipeaaa1.libraryapi.repository.LivroRepository;
import com.github.felipeaaa1.libraryapi.security.SecurityService;
import com.github.felipeaaa1.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.github.felipeaaa1.libraryapi.repository.specs.LivroSpecs.*;
@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final LivroValidator livroValidator;
    private final SecurityService securityService;

    public Livro salvar(Livro livro) {
            livroValidator.validar(livro);
            livro.setUsuario(securityService.obterUsuarioLogado());
            livroRepository.save(livro);
            return livro;
    }

    public Optional<Livro> getPorId(String id) {
        UUID uuid = UUID.fromString(id);
        return livroRepository.findById(uuid);
    }

    public void deletar(Livro livro){
        livroRepository.delete(livro);
    }

    public Page<Livro> listar(String isbn,
                              String nomeAutor,
                              String titulo,
                              GeneroLivro generoLivro,
                              Integer anoPublicacao,
                              Integer pagina,
                              Integer tamPag
    ){
        Specification<Livro> livroSpecification = Specification.where((root, query, cb) -> cb.conjunction());

        if(isbn != null){
            livroSpecification = livroSpecification.and(isbnEqual(isbn));
        }
        if(titulo != null){
            livroSpecification = livroSpecification.and(tituloLike(titulo));
        }
        if(generoLivro != null){
            livroSpecification = livroSpecification.and(generoEqual(generoLivro));
        }
        if(anoPublicacao != null){
            livroSpecification = livroSpecification.and(anoPublicacaoLike(anoPublicacao));
        }
        if(nomeAutor != null){
            livroSpecification = livroSpecification.and(nomeAutorLike(nomeAutor));
        }
        Pageable pag = PageRequest.of(pagina, tamPag);

        return livroRepository.findAll(livroSpecification, pag);
    }

    public void atualizar(Livro livro) {
        if (livro.getId() == null){
            throw new OperacaoNaoPermitidaException("Para atualizar um livro o ID é necessário");
        }
        livro.setUsuario(securityService.obterUsuarioLogado());
        livroRepository.save(livro);
    }
}

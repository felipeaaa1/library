package com.github.felipeaaa1.libraryapi.service;

import com.github.felipeaaa1.libraryapi.controller.dto.AutorDTO;
import com.github.felipeaaa1.libraryapi.exception.OperacaoNaoPermitidaException;
import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.repository.AutorRepository;
import com.github.felipeaaa1.libraryapi.repository.LivroRepository;
import com.github.felipeaaa1.libraryapi.security.SecurityService;
import com.github.felipeaaa1.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final AutorValidator autorValidator;
    private final SecurityService securityService;


    public Autor salvar(Autor autor) {
        autorValidator.validar(autor);
        autor.setUsuario(securityService.obterUsuarioLogado());
        return autorRepository.save(autor);
    }

    public Optional<Autor> obterPorId(String id) {
        UUID uuid = UUID.fromString(id); // Lança IllegalArgumentException se o UUID for inválido
        Optional autor = autorRepository.findById(uuid);
        if (autor.isPresent())
            return autor;
        else
            throw new NoSuchElementException();
    }

    public void deletar(String id) {
        UUID uuid = UUID.fromString(id);

        Autor autor = autorRepository.findById(uuid) // Lança IllegalArgumentException se o UUID for inválido
                .orElseThrow(NoSuchElementException::new);// Lança exceção se tentar excluir autor com UUID errado
        if (existLivro(autor)) {
            throw new OperacaoNaoPermitidaException("O autor " + autor.getNome() + " possui livros cadastrados");
        }
        autorRepository.delete(autor);
    }

    public List<Autor> pesquisarComExemple(String nome, String nacionalidade) {
        Autor autorExemplo = new Autor();
        autorExemplo.setNome(nome);
        autorExemplo.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "dataCadatro")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Autor> example = Example.of(autorExemplo, matcher);

        return autorRepository.findAll(example);
    }

    //não vou mais usar esse metodo pois a pesquisa com exemples é otimizada
    public List<Autor> pesquisaBurra(String nome, String nacionalidade) {
        if (nome != null && nacionalidade != null) {
            return autorRepository.findByNomeLikeAndNacionalidade("%" + nome + "%", nacionalidade);
        }
        if (nome != null) {
            return autorRepository.findByNomeLike("%" + nome + "%");
        }
        if (nacionalidade != null) {
            return autorRepository.findByNacionalidade(nacionalidade);
        } else
            return autorRepository.findAll().stream().toList();
    }

    public void atualizar(String id, AutorDTO autorDTO) {
        Autor autor = this.obterPorId(id).orElseThrow(NoSuchElementException::new);
        autor.setNacionalidade(autorDTO.nacionalidade());
        autor.setNome(autorDTO.nome());
        autorValidator.validar(autor);
        autor.setUsuario(securityService.obterUsuarioLogado());
        autorRepository.save(autor);
    }

    private boolean existLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }
}

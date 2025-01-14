package com.github.felipeaaa1.libraryapi.service;

import com.github.felipeaaa1.libraryapi.controller.dto.AutorDTO;
import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.repository.AutorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


import java.util.UUID;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository){
        this.autorRepository = autorRepository;
    }
    
    public Autor salvar(AutorDTO autorDTO){
        return autorRepository.save(autorDTO.retornaAutor());
    }

    public Autor obterPorId(String id) {
        UUID uuid = UUID.fromString(id); // Lança IllegalArgumentException se o UUID for inválido
        return autorRepository.findById(uuid)
                .orElseThrow(NoSuchElementException::new); // Lança exceção se não encontrar
    }

    public void deletar(String id) {
        UUID uuid = UUID.fromString(id);
        Autor autor = autorRepository.findById(uuid) // Lança IllegalArgumentException se o UUID for inválido
                .orElseThrow(NoSuchElementException::new); // Lança exceção se tentar excluir autor com UUID errado
        autorRepository.delete(autor);
    }


    public List<Autor> listarAutores(String nome, String nacionalidade) {
        if (nome != null && nacionalidade != null){
            return autorRepository.findByNomeLikeAndNacionalidade("%"+nome+"%", nacionalidade);
        }
        if(nome  != null){
            return autorRepository.findByNomeLike("%"+nome+"%");
        }
        if (nacionalidade  != null){
            return autorRepository.findByNacionalidade(nacionalidade);
        }
        else
            return autorRepository.findAll().stream().toList();
    }

    public void atualizar(String id, AutorDTO autorDTO) {
        Autor autor = this.obterPorId(id);
        autor.setNacionalidade(autorDTO.nacionalidade());
        autor.setNome(autorDTO.nome());
        autorRepository.save(autor);
    }
}

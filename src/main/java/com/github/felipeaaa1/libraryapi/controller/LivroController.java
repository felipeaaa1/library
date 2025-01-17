package com.github.felipeaaa1.libraryapi.controller;

import com.github.felipeaaa1.libraryapi.controller.dto.ErroCampoDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ErroRespostaDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.RequestLivroDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ResponseLivroDTO;
import com.github.felipeaaa1.libraryapi.exception.RegistroDuplicadoException;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("livro")
@RequiredArgsConstructor
public class LivroController {
    private final LivroService livroService;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid RequestLivroDTO requestLivroDTO){
        try{
//            Livro livroCadastrado = livroService.salvar(requestLivroDTO);
//            return ResponseEntity.ok().body(livroCadastrado);
return null;
        }catch (RegistroDuplicadoException e){
            var erro = ErroRespostaDTO.conflito(e.getMessage());
            return ResponseEntity.status(erro.status()).body(erro.mensagem());
        }

    }
}

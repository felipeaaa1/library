package com.github.felipeaaa1.libraryapi.controller;

import com.github.felipeaaa1.libraryapi.controller.dto.UsuarioDTO;
import com.github.felipeaaa1.libraryapi.controller.mappers.UsuarioMapper;
import com.github.felipeaaa1.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid UsuarioDTO usuarioDTO){

        var usuario = usuarioMapper.toEntity(usuarioDTO);
        usuarioService.salvar(usuario);
    }
}

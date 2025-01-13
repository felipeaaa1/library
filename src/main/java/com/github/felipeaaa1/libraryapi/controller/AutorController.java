package com.github.felipeaaa1.libraryapi.controller;

import com.github.felipeaaa1.libraryapi.controller.dto.AutorDTO;
import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.service.AutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("autor")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService){
        this.autorService = autorService;
    }

    @GetMapping
    public ResponseEntity<Autor> getAutores(){
        return null;
    }
    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody AutorDTO autorDTO){
        Autor autorCriado = autorService.salvar(autorDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorCriado.getId())
                .toUri();


        return ResponseEntity.created(uri).build();
    }

}

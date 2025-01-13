package com.github.felipeaaa1.libraryapi.controller.dto;

import com.github.felipeaaa1.libraryapi.model.Autor;

import java.time.LocalDate;

public record AutorDTO(String nome,
                       LocalDate dataNascimento,
                       String nacionalidade) {

    public Autor retornaAutor(){
        Autor a = new Autor();
        a.setNome(this.nome);
        a.setDataNascimento(this.dataNascimento);
        a.setNacionalidade(this.nacionalidade);
        return a;
}

}

package com.github.felipeaaa1.libraryapi.controller.mappers;

import com.github.felipeaaa1.libraryapi.controller.dto.RequestLivroDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ResponseLivroDTO;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java(  autorRepository.findById(dto.idAutor()).orElse(null) )")
    public abstract Livro toEntity(RequestLivroDTO dto);

    public abstract ResponseLivroDTO toDTO(Livro l);
}

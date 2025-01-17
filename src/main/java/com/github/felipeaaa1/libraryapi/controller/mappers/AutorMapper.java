package com.github.felipeaaa1.libraryapi.controller.mappers;

import com.github.felipeaaa1.libraryapi.controller.dto.AutorDTO;
import com.github.felipeaaa1.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    @Mapping(source = "nome",// atributo do parametro
            target = "nome")//atributo do retorno
    Autor toEntity(AutorDTO autorDTO);

    AutorDTO toDTO(Autor autor);
}

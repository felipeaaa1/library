package com.github.felipeaaa1.libraryapi.controller.mappers;


import com.github.felipeaaa1.libraryapi.controller.dto.UsuarioDTO;
import com.github.felipeaaa1.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO usuarioDTO);
}

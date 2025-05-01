package com.github.felipeaaa1.libraryapi.service;

import com.github.felipeaaa1.libraryapi.controller.GenericController;
import com.github.felipeaaa1.libraryapi.model.Usuario;
import com.github.felipeaaa1.libraryapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;

     public void salvar(Usuario usuario){
        var senha = usuario.getSenha();
        usuario.setSenha(encoder.encode(senha));
        usuarioRepository.save(usuario);

     }

     public Usuario obterPorEmail(String email){
         return  usuarioRepository.findByEmail(email);
     }

     public Usuario obterPorLogin(String login){
         return usuarioRepository.findByLogin(login);
     }
}

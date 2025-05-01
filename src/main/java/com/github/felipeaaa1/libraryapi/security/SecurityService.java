package com.github.felipeaaa1.libraryapi.security;

import com.github.felipeaaa1.libraryapi.model.Usuario;
import com.github.felipeaaa1.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UsuarioService usuarioService;
    public Usuario obterUsuarioLogado(){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
//        removendo essa parte para utilizarmos o authentication do spring security
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String login = userDetails.getUsername();
//        Usuario usuario = usuarioService.obterPorLogin(login);

//        colocando custom auth para retornar o usuario
        if (authentication instanceof CustomAuthentication customAuthentication)
            return customAuthentication.getUsuario();

        return null;
    }
}

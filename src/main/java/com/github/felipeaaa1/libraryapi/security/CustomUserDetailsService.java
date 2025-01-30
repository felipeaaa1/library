package com.github.felipeaaa1.libraryapi.security;

import com.github.felipeaaa1.libraryapi.model.Usuario;
import com.github.felipeaaa1.libraryapi.repository.UsuarioRepository;
import com.github.felipeaaa1.libraryapi.service.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario  usuario = service.obterPorLogin(username);
        if (usuario != null){
            return User.builder()
                    .username(usuario.getLogin())
                    .password(usuario.getSenha())
                    .roles(usuario.getRoles().toArray(new String[usuario.getRoles().size()]))
                    .build();
        }
        else
            throw new UsernameNotFoundException("Usuário não encontrado: "+ username);
    }
}

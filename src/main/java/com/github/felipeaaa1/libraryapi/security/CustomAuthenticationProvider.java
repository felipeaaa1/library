package com.github.felipeaaa1.libraryapi.security;

import com.github.felipeaaa1.libraryapi.model.Usuario;
import com.github.felipeaaa1.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.PasswordAuthentication;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String senhaDigitada = authentication.getCredentials().toString();

        Usuario usuarioEncontrado = usuarioService.obterPorLogin(login);
        if (usuarioEncontrado == null)
            throw getUsernameNotFoundException();

        String senhaCriptografada = usuarioEncontrado.getSenha();
        boolean matches = encoder.matches(senhaDigitada, senhaCriptografada);

        if (matches)
            return new CustomAuthentication(usuarioEncontrado);

        throw getUsernameNotFoundException();
    }

    private UsernameNotFoundException getUsernameNotFoundException() {
        return new UsernameNotFoundException("Usuario e/ou senha inválidos");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}

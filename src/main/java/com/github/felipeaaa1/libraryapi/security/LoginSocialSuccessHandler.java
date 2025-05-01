package com.github.felipeaaa1.libraryapi.security;

import com.github.felipeaaa1.libraryapi.model.Usuario;
import com.github.felipeaaa1.libraryapi.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws ServletException, IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User tokenPrincipal = token.getPrincipal();

        String email = tokenPrincipal.getAttribute("email");

        Usuario usuario = usuarioService.obterPorEmail(email);

        if (usuario == null){
            usuario = salvar_usuario(email);
        }
        authentication = new CustomAuthentication(usuario);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private Usuario salvar_usuario(String email) {
        Usuario novo = new Usuario();
        novo.setLogin(email.split("@")[0]);
        novo.setEmail(email);
        novo.setSenha("123");
        novo.setRoles(List.of("USER"));

        usuarioService.salvar(novo);
        return novo;
    }
}

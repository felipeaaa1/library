package com.github.felipeaaa1.libraryapi.security;

import com.github.felipeaaa1.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
                .csrf(AbstractHttpConfigurer::disable)

//                assim o form login pega o default do spring
//                .formLogin(Customizer.withDefaults())

//                assim é para o spring indicar a nossa pagina de login persinalizada
                .formLogin(configurer->{
                    configurer.loginPage("/login").permitAll();
                })
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(HttpMethod.POST,"/usuario").permitAll();
                    authorize.requestMatchers("/login").permitAll();

//                    colocar as autorizações de cada endpoint no proprio controller

//                    authorize.requestMatchers(HttpMethod.POST, "/autor").hasAnyRole("ADMIN");
//                    authorize.requestMatchers( "/autor/**").hasRole("USER");
//                    authorize.requestMatchers("/livro/**").hasAnyRole("ADMIN", "USER");
                    authorize.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public PasswordEncoder encoder(){

        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioService usuarioService){
//        UserDetails user1 = User.builder()
//                .username("user")
//                .password(encoder.encode("123"))
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("user2")
//                .password(encoder.encode("456"))
//                .roles("ADMIN")
//                .build();


        return new CustomUserDetailsService(usuarioService);
    }
}

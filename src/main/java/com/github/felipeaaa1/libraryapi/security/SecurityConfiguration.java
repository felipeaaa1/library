package com.github.felipeaaa1.libraryapi.security;

import com.github.felipeaaa1.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   LoginSocialSuccessHandler successHandler,
                                                   JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter)
            throws Exception{

        return http
                .csrf(AbstractHttpConfigurer::disable)

//                assim o form login pega o default do spring
//                .formLogin(Customizer.withDefaults())

//                assim é para o spring indicar a nossa pagina de login persinalizada
                .formLogin(configurer->{
                    configurer.loginPage("/login").permitAll();
                })
//                removendo para que na documentação não abrir um form
//                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(HttpMethod.POST,"/usuario").permitAll();
                    authorize.requestMatchers("/login").permitAll();

//                    colocar as autorizações de cada endpoint no proprio controller

//                    authorize.requestMatchers(HttpMethod.POST, "/autor").hasAnyRole("ADMIN");
//                    authorize.requestMatchers( "/autor/**").hasRole("USER");
//                    authorize.requestMatchers("/livro/**").hasAnyRole("ADMIN", "USER");
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(
//                        tirar as conigurações padrões para pegar detalhes depois da autenticação google
//                        Customizer.withDefaults()
                        oauth2 ->{
                            oauth2.loginPage("/login")
                             .successHandler(successHandler);})
                .oauth2ResourceServer(
                        //só colocar assim para dizer que vamos autenticar com jwt com configurações padrao
                        oauth2RS -> oauth2RS.jwt(Customizer.withDefaults()))
                // colocando esse filter pra transformar o auth no nosso custom auth
                .addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }
    //metodo para o filterchain do websecurity ignorar os endpoints abaixo (diferente de permit-all pq n passa por nada da segurança)
    // só isso e a dependencia no pom ja tinha dado certo, o resto é firula
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer (){
        return  web -> {
            web.ignoring().requestMatchers(
                  "/v2/api-docs/**",
                    "/v3/api-docs/**",
                  "/swagger-resources/**",
                  "/swagger-ui.html",
                  "/swagger-ui/**",
                  "/webjars/**"

          );
        };
    }

    //    metodo para alterar o prefixo da authenticação para nada, basicamente tirari o ROLE_
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        var authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);

        return converter;
    }









/* colocando o passencoder no AuthorizationServerConfiguration
    @Bean
    public PasswordEncoder encoder(){

        return new BCryptPasswordEncoder(10);
    }
*/

//    @Bean
//    colocando esse metodo com a anotação bean a gente começa a fazer a autenticação manualmente
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

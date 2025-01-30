package com.github.felipeaaa1.libraryapi.controller;

import com.github.felipeaaa1.libraryapi.controller.dto.ErroCampoDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ErroRespostaDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.RequestLivroDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ResponseLivroDTO;
import com.github.felipeaaa1.libraryapi.controller.mappers.LivroMapper;
import com.github.felipeaaa1.libraryapi.exception.RegistroDuplicadoException;
import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.model.enums.GeneroLivro;
import com.github.felipeaaa1.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("livro")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class LivroController implements GenericController {
    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @GetMapping("{id}")
    public ResponseEntity<?> retornarLivro(@PathVariable(name = "id") String id) {

        return livroService.getPorId(id)
                .map(livro -> {
                    var dto = livroMapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> listarLivros(
            @RequestParam(name = "isbn",required = false)
            String isbn,
            @RequestParam(name = "nomeAutor",required = false)
            String nomeAutor,
            @RequestParam(name = "titulo",required = false)
            String titulo,
            @RequestParam(name = "generoLivro",required = false)
            GeneroLivro generoLivro,
            @RequestParam(name = "anoPublicacao",required = false)
            Integer anoPublicacao,
            @RequestParam(name = "pagina",required = false, defaultValue = "0")
            Integer pagina,
            @RequestParam(name = "tamPagina",required = false, defaultValue = "10")
            Integer tamPagina){

        var resultado = livroService.listar(isbn, nomeAutor, titulo, generoLivro, anoPublicacao, pagina, tamPagina);

        Page<ResponseLivroDTO> resultadoPag = resultado.map(livroMapper::toDTO);

//        var lista = resultado.stream()
//                .map(livroMapper::toDTO)
//                .collect(Collectors.toList());
        return ResponseEntity.ok(resultadoPag);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> salvar(@RequestBody @Valid RequestLivroDTO requestLivroDTO) {
        Livro livro = livroMapper.toEntity(requestLivroDTO);
        Livro livroCadastrado = livroService.salvar(livro);
        var url = gerarHeaderLocation(livroCadastrado.getId());
        return ResponseEntity.created(url).build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarLivro(@PathVariable(name = "id") String id,
                                            @RequestBody @Valid RequestLivroDTO dto){
        return livroService.getPorId(id)
                .map(livro -> {
                    Livro entity = livroMapper.toEntity(dto);

                    livro.setDataPublicacao(entity.getDataPublicacao());
                    livro.setIsbn(entity.getIsbn());
                    livro.setPreco(entity.getPreco());
                    livro.setGenero(entity.getGenero());
                    livro.setTitulo(entity.getTitulo());
                    livro.setAutor(entity.getAutor());

                    livroService.atualizar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletar(@PathVariable(name = "id") String id){
        return livroService.getPorId(id)
                .map( livro -> {
                    livroService.deletar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }


}

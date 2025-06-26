package com.github.felipeaaa1.libraryapi.controller;

import com.github.felipeaaa1.libraryapi.controller.dto.AutorDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ErroRespostaDTO;
import com.github.felipeaaa1.libraryapi.controller.mappers.AutorMapper;
import com.github.felipeaaa1.libraryapi.exception.OperacaoNaoPermitidaException;
import com.github.felipeaaa1.libraryapi.exception.RegistroDuplicadoException;
import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.model.Usuario;
import com.github.felipeaaa1.libraryapi.service.AutorService;
import com.github.felipeaaa1.libraryapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autor")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@Tag(name = "Autores")
public class AutorController implements GenericController {

    private final AutorService autorService;
    private final AutorMapper autorMapper;


    @GetMapping
    @Operation(summary = "Listar", description = "End-point para listar todos os autores")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Realiza pesquisa de autores")
    })
    public ResponseEntity<List<AutorDTO>> listarAutores(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "nacionalidade", required = false) String nacionalidade) {

        
        List<Autor> lista = autorService.pesquisarComExemple(nome, nacionalidade);
        List<AutorDTO> listaDTO =
                lista.stream().map(
                        autorMapper::toDTO
                ).collect(Collectors.toList());

        return ResponseEntity.ok(listaDTO);
    }

    @GetMapping("{id}")
    @Operation(summary = "Obter detalhes", description = "End-point para retornar detalhes de um autor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "401", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    public ResponseEntity<?> getAutor(@PathVariable(name = "id") String id) {
//TODO: validar o retorno que aqui era pra retornar só um autor e no swagger ta retornando um JSON maluco
        return ResponseEntity.ok(autorService.obterPorId(id));

//        return autorService.obterPorId(id)
//                .map(autor -> {
//                    AutorDTO dto = autorMapper.toDTO(autor);
//                    return ResponseEntity.ok(dto);
//                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Salvar", description = "End-point para salvar um novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cadastrado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de validação"),
            @ApiResponse(responseCode = "409", description = "Autor ja cadstrado")
    })
    public ResponseEntity<?> salvar(@RequestBody @Valid AutorDTO autorDTO) {

        Autor autor = autorMapper.toEntity(autorDTO);
        Autor autorCriado = autorService.salvar(autor);

        URI uri = gerarHeaderLocation(autor.getId());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Excluir", description = "End-point para excluir um autor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "400", description = "Autor possui livros cadastrados")
    })
    public ResponseEntity<?> deletarAutor(@PathVariable(name = "id") String id) {
        autorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar autor", description = "End-point para atualizar dados de um autor")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "409", description = "Autor ja cadastrado")

    })
    public ResponseEntity<?> atualizarAutor(@PathVariable(name = "id") String id
            , @RequestBody @Valid AutorDTO autorDTO) {

        autorService.atualizar(id, autorDTO);
        return this.getAutor(id);
    }


}

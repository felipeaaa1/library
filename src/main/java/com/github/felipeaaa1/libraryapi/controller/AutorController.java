package com.github.felipeaaa1.libraryapi.controller;

import com.github.felipeaaa1.libraryapi.controller.dto.AutorDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ErroRespostaDTO;
import com.github.felipeaaa1.libraryapi.exception.OperacaoNaoPermitida;
import com.github.felipeaaa1.libraryapi.exception.RegistroDuplicadoException;
import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autor")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;



    @GetMapping
    public ResponseEntity<List<AutorDTO>> listarAutores(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "nacionalidade", required = false) String nacionalidade){
        List<Autor> lista = autorService.pesquisarComExemple(nome, nacionalidade);
        List<AutorDTO> listaDTO =
                lista.stream().map(autor -> new AutorDTO(
                        autor.getId(),
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade())
                ).collect(Collectors.toList());

        return ResponseEntity.ok(listaDTO);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getAutor(@PathVariable(name = "id") String id) {
        try {
            Autor autor = autorService.obterPorId(id);
            return ResponseEntity.ok(autor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("UUID inválido: "+id); // UUID inválido
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor não encontrado"); // Autor não encontrado
        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid AutorDTO autorDTO){
        try{
            Autor autorCriado = autorService.salvar(autorDTO.retornaAutor());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorCriado.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
        }catch (RegistroDuplicadoException e){
            var erro =  ErroRespostaDTO.conflito(e.getMessage());
            return ResponseEntity.status(erro.status()).body(erro.mensagem());// UUID inválido
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarAutor(@PathVariable(name = "id") String id){
        try{
            autorService.deletar(id);
        return ResponseEntity.noContent().build();
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("UUID inválido: " + id);// UUID inválido
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor não encontrado"); // Autor não encontrado
        } catch (OperacaoNaoPermitida e){
            var erro = new ErroRespostaDTO(HttpStatus.UNAUTHORIZED.value(),e.getMessage(),List.of());
            return ResponseEntity.status(erro.status()).body(erro.mensagem());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizarAutor(@PathVariable(name = "id") String id
            ,@RequestBody @Valid AutorDTO autorDTO){
        try{
            autorService.atualizar(id, autorDTO);
            return this.getAutor(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("UUID inválido: " + id);// UUID inválido
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor não encontrado"); // Autor não encontrado
        }catch (RegistroDuplicadoException e){
            var erro =  ErroRespostaDTO.conflito(e.getMessage());
            return ResponseEntity.status(erro.status()).body(erro.mensagem());// UUID inválido
        }
    }


}

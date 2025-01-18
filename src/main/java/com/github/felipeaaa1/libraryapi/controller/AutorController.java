package com.github.felipeaaa1.libraryapi.controller;

import com.github.felipeaaa1.libraryapi.controller.dto.AutorDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ErroRespostaDTO;
import com.github.felipeaaa1.libraryapi.controller.mappers.AutorMapper;
import com.github.felipeaaa1.libraryapi.exception.OperacaoNaoPermitidaException;
import com.github.felipeaaa1.libraryapi.exception.RegistroDuplicadoException;
import com.github.felipeaaa1.libraryapi.model.Autor;
import com.github.felipeaaa1.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autor")
@RequiredArgsConstructor
public class AutorController implements GenericController {

    private final AutorService autorService;

    private final AutorMapper autorMapper;


    @GetMapping
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
    public ResponseEntity<?> getAutor(@PathVariable(name = "id") String id) {

        return autorService.obterPorId(id)
                .map(autor -> {
                    AutorDTO dto = autorMapper.toDTO(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());


//            Autor autor = autorService.obterPorId(id);
//            AutorDTO dto = autorMapper.toDTO(autor);
//            return ResponseEntity.ok(dto);
//        catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("UUID inválido: "+id); // UUID inválido
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor não encontrado"); // Autor não encontrado
//        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid AutorDTO autorDTO) {

        Autor autor = autorMapper.toEntity(autorDTO);
        Autor autorCriado = autorService.salvar(autor);
//            coloquei o gerador de URI num controlador generico para limpar o cod.
        URI uri = gerarHeaderLocation(autor.getId());
//            porém vou deixar aqui pra ver o que ta sendo feito la e vai ficar sujo mesmo (y)
//        URI uri = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(autorCriado.getId())
//                .toUri();
        return ResponseEntity.created(uri).build();
//        }catch (RegistroDuplicadoException e){
//            var erro =  ErroRespostaDTO.conflito(e.getMessage());
//            return ResponseEntity.status(erro.status()).body(erro.mensagem());// UUID inválido
//        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarAutor(@PathVariable(name = "id") String id) {
        autorService.deletar(id);
        return ResponseEntity.noContent().build();
//        }catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("UUID inválido: " + id);// UUID inválido
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor não encontrado"); // Autor não encontrado
//        } catch (OperacaoNaoPermitidaException e){
//            var erro = new ErroRespostaDTO(HttpStatus.UNAUTHORIZED.value(),e.getMessage(),List.of());
//            return ResponseEntity.status(erro.status()).body(erro.mensagem());
//        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizarAutor(@PathVariable(name = "id") String id
            , @RequestBody @Valid AutorDTO autorDTO) {
        autorService.atualizar(id, autorDTO);
        return this.getAutor(id);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("UUID inválido: " + id);// UUID inválido
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor não encontrado"); // Autor não encontrado
//        }catch (RegistroDuplicadoException e){
//            var erro =  ErroRespostaDTO.conflito(e.getMessage());
//            return ResponseEntity.status(erro.status()).body(erro.mensagem());// UUID inválido
//        }
    }


}

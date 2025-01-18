package com.github.felipeaaa1.libraryapi.common;

import com.github.felipeaaa1.libraryapi.controller.dto.ErroCampoDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ErroRespostaDTO;
import com.github.felipeaaa1.libraryapi.exception.OperacaoNaoPermitidaException;
import com.github.felipeaaa1.libraryapi.exception.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroRespostaDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        System.err.println(e.getMessage());
        List<FieldError> fieldError = e.getFieldErrors();
        List<ErroCampoDTO> erros = fieldError.stream().map(fe -> new ErroCampoDTO(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErroRespostaDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação",
                erros
        );
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroRespostaDTO hanleRegistroDuplicadoException(RegistroDuplicadoException e){
        return  ErroRespostaDTO.conflito(e.getMessage());

    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostaDTO handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e){
        return ErroRespostaDTO.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostaDTO handleIllegalArgumentException(IllegalArgumentException e) {
        String mensagem = null;
        if (e.getMessage().contains("Invalid UUID string")) {
            mensagem = Arrays.stream(e.getMessage().split(":")).toList().getLast();
            mensagem = "UUID inválido: " + mensagem;
        }
        return new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                mensagem==null? e.getMessage(): mensagem,
                List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroRespostaDTO handleExcecaonaotratada(RuntimeException e){
        return new ErroRespostaDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Parabén usuário, vc achou um erro não tratado, por favor contate o suporte mandando o que aconteceu: "+e.getMessage(),
                List.of());
    }
}

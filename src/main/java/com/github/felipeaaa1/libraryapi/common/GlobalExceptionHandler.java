package com.github.felipeaaa1.libraryapi.common;

import com.github.felipeaaa1.libraryapi.controller.dto.ErroCampoDTO;
import com.github.felipeaaa1.libraryapi.controller.dto.ErroRespostaDTO;
import com.github.felipeaaa1.libraryapi.exception.CampoInvalidoException;
import com.github.felipeaaa1.libraryapi.exception.OperacaoNaoPermitidaException;
import com.github.felipeaaa1.libraryapi.exception.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroRespostaDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
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
    public ErroRespostaDTO hanleRegistroDuplicadoException(RegistroDuplicadoException e) {
        System.err.println(e.getMessage());

        return ErroRespostaDTO.conflito(e.getMessage());

    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostaDTO handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e) {
        System.err.println(e.getMessage());

        return ErroRespostaDTO.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostaDTO handleIllegalArgumentException(IllegalArgumentException e) {
        System.err.println(e.getMessage());
        String mensagem = null;
        if (e.getMessage().contains("Invalid UUID string")) {
            mensagem = Arrays.stream(e.getMessage().split(":")).toList().getLast();
            mensagem = "UUID inválido: " + mensagem;
        }
        return new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                mensagem == null ? e.getMessage() : mensagem,
                List.of());
    }



    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErroRespostaDTO handleExcecaonaotratada(RuntimeException e) {
        System.err.println(e.getMessage());
        return new ErroRespostaDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Parabéns usuário, vc achou um erro não tratado, por favor contate o suporte mandando o que aconteceu: --mensagem:" + e.getMessage()+" causa: "+e.getCause(),
                List.of());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErroRespostaDTO handleAcaoNaoPermitida(AccessDeniedException e) {
        System.err.println(e.getMessage());
        return new ErroRespostaDTO(
                HttpStatus.FORBIDDEN.value(),
                "Ação não permitida para esse usuário",
                List.of());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ErroRespostaDTO handleNoSuchElementException(NoSuchElementException e) {
        System.err.println(e.getMessage());
        return new ErroRespostaDTO(
                HttpStatus.NOT_FOUND.value(),
                "Elemento com id fornecido não encontrado",
                List.of()
        );
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CampoInvalidoException.class)
    public ErroRespostaDTO handleCampoInvalidoException(CampoInvalidoException e) {
        System.err.println(e.getMessage());
        return new ErroRespostaDTO(
                HttpStatus.NOT_FOUND.value(),
                "Campo Obrigatório",
                List.of(new ErroCampoDTO(e.getCampo(), e.getMessage()))
        );
    }
}

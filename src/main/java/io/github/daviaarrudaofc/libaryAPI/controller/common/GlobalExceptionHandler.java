package io.github.daviaarrudaofc.libaryAPI.controller.common;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.ErroCampo;
import io.github.daviaarrudaofc.libaryAPI.controller.dto.ErroResposta;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice // Centraliza o tratamento global das exceções lançadas pelos controllers da API.
@ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErroCampo> listaErros = fieldErrors
                .stream()
                .map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErroResposta(HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Erro de validação",
                listaErros);

    }
}

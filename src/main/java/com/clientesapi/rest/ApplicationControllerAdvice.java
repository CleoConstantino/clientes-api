package com.clientesapi.rest;

import com.clientesapi.rest.exception.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors retornoErroEntidade(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        List<String> mensagensDeErro = bindingResult
                                            .getAllErrors()
                                            .stream()
                                             .map(objetocomErro -> objetocomErro.getDefaultMessage())
                                             .collect(Collectors.toList());
        return new ApiErrors(mensagensDeErro);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors retornoErroNotFound(ResponseStatusException ex) {
        return new ApiErrors(ex.getMessage());
    }
}

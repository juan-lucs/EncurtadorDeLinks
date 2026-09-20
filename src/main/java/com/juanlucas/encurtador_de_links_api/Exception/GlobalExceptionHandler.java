package com.juanlucas.encurtador_de_links_api.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail UrlVazia(MethodArgumentNotValidException m ) {
        String mensagemTratada = m.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
        var problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, mensagemTratada);
        problemDetail.setTitle("Url inválida!");
        return problemDetail;
    }

    @ExceptionHandler(EncurtadorNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail UrlNaoEncontrada(EncurtadorNaoEncontradoException m) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, m.getMessage());
        problemDetail.setTitle("Link encurtado não encontrado");
        return problemDetail;
    }

    @ExceptionHandler(UrlInvalidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail UrlInvalida(UrlInvalidaException m) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, m.getMessage());
        problemDetail.setTitle("Formato da URL não aceito ou inválido!");
        return problemDetail;
    }
}

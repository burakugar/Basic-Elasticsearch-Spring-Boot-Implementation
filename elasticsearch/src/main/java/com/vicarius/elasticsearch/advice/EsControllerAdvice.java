package com.vicarius.elasticsearch.advice;

import com.vicarius.elasticsearch.exception.DocumentNotFoundException;
import com.vicarius.elasticsearch.exception.ElasticsearchOperationException;
import com.vicarius.elasticsearch.exception.InvalidFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class EsControllerAdvice {
    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<String> handleDocumentNotFoundException(final DocumentNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ElasticsearchOperationException.class)
    public ResponseEntity<String> handleElasticsearchOperationException(final ElasticsearchOperationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        throw new InvalidFieldException(errors);
    }
}

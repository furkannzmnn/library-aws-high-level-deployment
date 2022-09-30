package com.example.lib.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    // handleHttpMediaTypeNotSupported : triggers when the JSON is invalid
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> details = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        details.add(builder.toString());

        Map<String, Object> errors = new HashMap<>();
        errors.put("errorMessage", details);
        errors.put("errorCode", status.value());
        errors.put("httpStatus", status);

        return ResponseEntity.status(status).body(errors);

    }

    // handleHttpMessageNotReadable : triggers when the JSON is malformed
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("errorMessage", ex.getMessage());
        errors.put("errorCode", status.value());
        errors.put("httpStatus", status);

        return ResponseEntity.status(status).body(errors);
    }

    // handleMissingServletRequestParameter : triggers when there are missing parameters
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("errorMessage", ex.getParameterName() + " parameter is missing");
        errors.put("errorCode", status.value());
        errors.put("httpStatus", status);

        return ResponseEntity.status(status).body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(errors);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<?> handleException(GenericException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error", ex.getErrorMessage());
        errors.put("errorCode", ex.getErrorCode());
        return ResponseEntity
                .status(ex.getHttpStatus() != null ?  ex.getHttpStatus() : HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}

package com.techstore.ecommerce.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.techstore.ecommerce.object.wrapper.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse response = new ErrorResponse();

        response.setResponseCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());

        exception.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception
                .getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        ErrorResponse response = new ErrorResponse();
        response.setResponseCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(
                "Something were wrong with your query, " + "please check errors and try again.");
        response.setErrors(errors);

        exception.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            AccessDeniedException exception) {
        ErrorResponse response = new ErrorResponse();

        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        exception.printStackTrace();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            response.setResponseCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else {
            response.setResponseCode(HttpStatus.FORBIDDEN.value());
            response.setMessage(exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenException(
            TokenExpiredException exception) {
        ErrorResponse response = new ErrorResponse();

        response.setResponseCode(HttpStatus.UNAUTHORIZED.value());
        response.setMessage(exception.getMessage());

        exception.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            ResourceNotFoundException exception) {
        ErrorResponse response = new ErrorResponse();

        response.setResponseCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(
                ObjectUtils.isEmpty(exception.getMessage())
                ? "Resource not found"
                : exception.getMessage());

        exception.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException exception) {
        ErrorResponse response = new ErrorResponse();

        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(exception.getMessage());

        exception.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintException(ConstraintViolationException exception) {
        ErrorResponse response = new ErrorResponse();

        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(exception.getMessage());

        exception.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorResponse> handleServerException(ServletException exception) {
        ErrorResponse response = new ErrorResponse();

        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(exception.getMessage());

        exception.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

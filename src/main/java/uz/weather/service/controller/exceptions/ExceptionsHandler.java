package uz.weather.service.controller.exceptions;

import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import uz.weather.service.dto.ResponseDTO;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler
    public Mono<ResponseDTO<Void>> dataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Started exceptionHandler dataIntegrityViolationException. Message: {}", e.getMessage());
        return Mono.just(ResponseDTO.<Void>builder()
                        .message("Error while saving data: " + cause(e))
                .build());
    }

    @ExceptionHandler
    public Mono<ResponseDTO<Void>> illegalArgumentException(IllegalArgumentException e) {
        log.error("Started exceptionHandler illegalArgumentException. Message: {}", e.getMessage());
        return Mono.just(ResponseDTO.<Void>builder()
                        .message("Illegal argument exception: " + cause(e))
                .build());
    }

    @ExceptionHandler
    public Mono<ResponseDTO<Void>> unexpectedException(Exception e) {
        log.error("Started exceptionHandler unexpectedException. Message: {}", e.getMessage());
        e.printStackTrace();
        return Mono.just(ResponseDTO.<Void>builder()
                .message("Unexpected exception occurred: " + cause(e))
                .build());
    }

    @ExceptionHandler
    public Mono<ResponseEntity<String>> tokenInvalidException(MalformedJwtException e) {
        log.error("Started exceptionHandler tokenInvalidException. Message: {}", e.getMessage());
        return Mono.just(ResponseEntity.badRequest().body("Malformed JWT token"));
    }

    @ExceptionHandler
    public Mono<ResponseEntity<String>> forbiddenException(AuthenticationCredentialsNotFoundException e) {
        log.error("Started exceptionHandler forbiddenException. Message: {}", e.getMessage());
        return Mono.just(
                new ResponseEntity<>("This url is forbidden for users",
                        HttpStatus.FORBIDDEN)
        );
    }

    @ExceptionHandler
    public Mono<ResponseEntity<String>> AccessDeniedException(AccessDeniedException e) {
        log.error("Started exceptionHandler forbiddenException. Message: {}", e.getMessage());
        return Mono.just(
                new ResponseEntity<>("This url is forbidden for users",
                        HttpStatus.FORBIDDEN)
        );
    }

    private String cause(Exception e) {
        StringBuilder cause = new StringBuilder(e.getMessage());
        Throwable t = e.getCause();

        cause.append(" Caused by: ");
        while (t != null){
            cause.append(t.getMessage());
            t = t.getCause();
        }
        return cause.toString();
    }
}

package sn.ods.obi.infrastructure.config.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sn.ods.obi.presentation.dto.responses.APIMessage;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.responses.ValidationRspError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultHandlerException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ResourceNotFoundException ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

    }



    //pour les contraintes de validations au niveau des méthodes
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<ValidationRspError> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> ValidationRspError.builder().field(fieldError.getField()).message(fieldError.getDefaultMessage()).build())
                .collect(Collectors.toList());

        // LOGGER.error("{}", errors);

        return ResponseEntity.badRequest().body(APIResponse.error(APIMessage.CONSTRAINT_VIOLATION).errors(errors));
    }

    //pour les contraintes de validations au niveau des objets
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse> handleViolationException(ConstraintViolationException exception) {
        List<ValidationRspError> errors = exception.getConstraintViolations()
                .stream()
                .map(fieldError -> ValidationRspError.builder().field(fieldError.getPropertyPath().toString()).message(fieldError.getMessage()).build())
                .collect(Collectors.toList());

        // LOGGER.error("constraints", errors);
        return ResponseEntity.badRequest().body(APIResponse.error(APIMessage.CONSTRAINT_VIOLATION).errors(errors));

    }



    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiError> handleException(InsufficientAuthenticationException ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
          request.getRequestURI(),
          ex.getMessage(),
          HttpStatus.FORBIDDEN.value(),
          LocalDateTime.now());
          return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);

  }



    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                ex.getMessage() != null ? ex.getMessage() : "Requête invalide",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleException(BadCredentialsException ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
        request.getRequestURI(),
        ex.getMessage(),
        HttpStatus.UNAUTHORIZED.value(),
        LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);

    }

    /** Méthode HTTP non autorisée (ex. GET sur une URL qui n'accepte que POST) — retourne un JSON avec message pour le frontend. */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<APIResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        String msg = "Méthode HTTP non autorisée pour cette URL.";
        if (ex.getSupportedHttpMethods() != null && !ex.getSupportedHttpMethods().isEmpty()) {
            String supported = ex.getSupportedHttpMethods().stream()
                    .map(org.springframework.http.HttpMethod::name)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            msg = ex.getMethod() + " n'est pas supporté. Méthode(s) autorisée(s) : " + supported;
        }
        APIResponse response = APIResponse.builder().success(false).message(msg).build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}

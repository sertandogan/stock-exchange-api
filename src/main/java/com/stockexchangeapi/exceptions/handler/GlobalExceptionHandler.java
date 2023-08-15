package com.stockexchangeapi.exceptions.handler;

import com.stockexchangeapi.enums.ErrorType;
import com.stockexchangeapi.exceptions.BaseException;
import com.stockexchangeapi.model.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Set<ErrorResponse>> handle(Exception ex, HttpServletRequest request) {
        var errorType = ErrorType.UNKNOWN_ERROR;
        log.error(errorType.name(), request, errorType.getHttpStatus(), ex);
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(Set.of(new ErrorResponse(errorType.getErrorCode(), getMessage(errorType.getErrorMessageKey()))));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Set<ErrorResponse>> handleBaseException(BaseException ex, HttpServletRequest request) {
        var errorType = Objects.nonNull(ex.getErrorType()) ? ex.getErrorType() : ErrorType.UNKNOWN_ERROR;

        log.error(errorType.name(), request, errorType.getHttpStatus(), ex);
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(Set.of(new ErrorResponse(errorType.getErrorCode(), getMessage(errorType.getErrorMessageKey()))));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Set<ErrorResponse>> handleBadCredentialsException(BaseException ex, HttpServletRequest request) {
        var errorType = ErrorType.UNAUTHORIZED_EXCEPTION;
        log.error(errorType.name(), request, errorType.getHttpStatus(), ex);
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(Set.of(new ErrorResponse(errorType.getErrorCode(), getMessage(errorType.getErrorMessageKey()))));
    }
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Set<ErrorResponse>> handleOptimisticLockingFailureException(BaseException ex, HttpServletRequest request) {
        var errorType = ErrorType.OPTIMISTIC_LOCKING_EXCEPTION;
        log.error(errorType.name(), request, errorType.getHttpStatus(), ex);
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(Set.of(new ErrorResponse(errorType.getErrorCode(), getMessage(errorType.getErrorMessageKey()))));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Set<ErrorResponse>> handle(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error(request.toString(), HttpStatus.BAD_REQUEST, ex);

        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .filter(it -> it.getDefaultMessage() != null)
                .map(it -> new ErrorResponse(it.getField(), getMessage(it)))
                .collect(Collectors.toSet());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    private String getMessage(FieldError error) {
        return getMessage(error.getDefaultMessage(), error.getArguments());
    }

    private String getMessage(String key) {
        return getMessage(key, null);
    }

    private String getMessage(String messageKey, Object[] arguments) {
        try {
            return messageSource.getMessage(messageKey, arguments, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return messageKey;
        }
    }
}

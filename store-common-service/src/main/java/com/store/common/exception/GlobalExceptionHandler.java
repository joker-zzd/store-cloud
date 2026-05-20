package com.store.common.exception;

import com.store.common.resultvo.ResultVO;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultVO<Void> handleBusinessException(BusinessException exception) {
        log.error("business exception", exception);
        return ResultVO.fail(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldError() != null
                ? exception.getBindingResult().getFieldError().getDefaultMessage()
                : "Parameter validation failed";
        return ResultVO.fail(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResultVO<Void> handleConstraintViolationException(ConstraintViolationException exception) {
        return ResultVO.fail(exception.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResultVO<Void> handleResponseStatusException(ResponseStatusException exception) {
        log.warn("response status exception", exception);
        String message = exception.getReason() != null ? exception.getReason() : exception.getMessage();
        return ResultVO.fail(exception.getStatusCode().value(), message);
    }

    @ExceptionHandler(Exception.class)
    public ResultVO<Void> handleException(Exception exception) {
        if (isNoResourceFoundException(exception)) {
            log.warn("resource not found", exception);
            return ResultVO.fail(404, exception.getMessage());
        }
        log.error("system exception", exception);
        String message = exception.getMessage() == null || exception.getMessage().isBlank()
                ? exception.getClass().getSimpleName()
                : exception.getClass().getSimpleName() + ": " + exception.getMessage();
        return ResultVO.fail("System exception: " + message);
    }

    private boolean isNoResourceFoundException(Exception exception) {
        return exception != null
                && "NoResourceFoundException".equals(exception.getClass().getSimpleName());
    }
}
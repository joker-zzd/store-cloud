package com.store.exception;

import com.store.common.exception.BusinessException;
import com.store.common.resultvo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultVO<Void> handleBusinessException(BusinessException exception) {
        return ResultVO.fail(exception.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResultVO<Void> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException exception) {
        return ResultVO.fail("Uploaded file exceeds the system size limit");
    }

    @ExceptionHandler(Exception.class)
    public ResultVO<Void> handleException(Exception exception) {
        log.error("file-service error", exception);
        return ResultVO.fail("System is busy, please try again later");
    }
}

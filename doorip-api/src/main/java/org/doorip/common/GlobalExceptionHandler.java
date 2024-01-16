package org.doorip.common;

import lombok.extern.slf4j.Slf4j;
import org.doorip.exception.BusinessException;
import org.doorip.message.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(">>> handle: MethodArgumentNotValidException ", e);
        return ApiResponseUtil.failure(ErrorMessage.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<BaseResponse<?>> handleBindException(final BindException e) {
        log.error(">>> handle: BindException ", e);
        return ApiResponseUtil.failure(ErrorMessage.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<BaseResponse<?>> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error(">>> handle: MethodArgumentTypeMismatchException ", e);
        return ApiResponseUtil.failure(ErrorMessage.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<BaseResponse<?>> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error(">>> handle: HttpRequestMethodNotSupportedException ", e);
        return ApiResponseUtil.failure(ErrorMessage.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<BaseResponse<?>> handleBusinessException(final BusinessException e) {
        log.error(">>> handle: BusinessException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseResponse<?>> handleException(final Exception e) {
        log.error(">>> handle: Exception ", e);
        return ApiResponseUtil.failure(ErrorMessage.INTERNAL_SERVER_ERROR);
    }
}
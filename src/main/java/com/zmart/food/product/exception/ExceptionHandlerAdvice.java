package com.zmart.food.product.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

import static com.zmart.food.product.exception.AppHttpMessageNotReadableException.NOT_READABLE_CLASS_NAME;
import static com.zmart.food.product.exception.AppHttpMessageNotReadableException.NOT_READABLE_ILLEGAL_ARG_MSG;
import static com.zmart.food.product.exception.AppHttpRequestMethodNotSupportedException.METHOD_NOT_ALLOWED_CLASS_NAME;
import static com.zmart.food.product.exception.AppHttpRequestMethodNotSupportedException.METHOD_NOT_ALLOWED_MSG;
import static com.zmart.food.product.exception.AppIllegalArgumentException.ILLEGAL_ARG_MSG;
import static com.zmart.food.product.exception.AppInternalServerErrorException.INTERNAL_SERVER_ERROR_CLASS_NAME;
import static com.zmart.food.product.exception.AppInternalServerErrorException.INTERNAL_SERVER_ERROR_MSG;
import static com.zmart.food.product.exception.AppNullPointerException.NULL_POINTER_MSG;
import static com.zmart.food.product.exception.AppSpecialCaseException.SPECIAL_CASE_MSG;

/** Controller advice to translate the server side exceptions to client-friendly json structures. */
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

  private static final String EX_FQN_FILTER_INCLUDE = "com.zmart.food";
  private static final String EX_FQN_FILTER_EXCLUDE = "exception";
  private final AppNullPointerException appNullPointerException;
  private final AppIllegalArgumentException appIllegalArgumentException;
  private final AppInternalServerErrorException appInternalServerErrorException;
  private final AppSpecialCaseException appSpecialCaseException;
  private StackTraceElement[] filteredExUtilElements;

  @Autowired
  ExceptionHandlerAdvice(
      final AppNullPointerException appNullPointerException,
      final AppIllegalArgumentException appIllegalArgumentException,
      final AppInternalServerErrorException appInternalServerErrorException,
      final AppSpecialCaseException appSpecialCaseException) {
    this.appNullPointerException = appNullPointerException;
    this.appIllegalArgumentException = appIllegalArgumentException;
    this.appInternalServerErrorException = appInternalServerErrorException;
    this.appSpecialCaseException = appSpecialCaseException;
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<?> handleException(final Exception ex) {
    if (ex instanceof AppNullPointerException) {
      return handleAppNullPointerException((AppNullPointerException) ex);
    } else if (ex instanceof AppIllegalArgumentException) {
      return handleAppIllegalArgumentException((AppIllegalArgumentException) ex);
    } else if (ex instanceof AppSpecialCaseException) {
      return handleAppSpecialCaseException((AppSpecialCaseException) ex);
    } else {
      return handleExceptionInternal(ex);
    }
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      @NotNull final HttpMessageNotReadableException ex,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    filterExceptionUtilTraceElements(ex);
    final ExceptionBuilder exBuilder = new ExceptionBuilder();
    exBuilder
        .withCode(String.valueOf(status))
        .withMessage(NOT_READABLE_ILLEGAL_ARG_MSG)
        .withException(NOT_READABLE_CLASS_NAME)
        .withNestedException(getExceptionName(ex))
        .withCauseMessage(getMessage(ex).split(" nested exception is")[0])
        .withDeclaringClass(getDeclaringClass())
        .withMethodCaller(getMethodCaller())
        .withMethodName(getMethodName())
        .withLineNumber(getLineNumber())
        .build();
    return new ResponseEntity<>(exBuilder, status);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      final HttpRequestMethodNotSupportedException ex,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    filterExceptionUtilTraceElements(ex);
    final ExceptionBuilder exBuilder = new ExceptionBuilder();
    exBuilder
        .withCode(String.valueOf(status))
        .withMessage(METHOD_NOT_ALLOWED_MSG)
        .withException(METHOD_NOT_ALLOWED_CLASS_NAME)
        .withNestedException(getExceptionName(ex))
        .withCauseMessage(getMessage(ex))
        .withDeclaringClass(getDeclaringClass())
        .withMethodCaller(getMethodCaller())
        .withMethodName(getMethodName())
        .withLineNumber(getLineNumber())
        .build();
    return new ResponseEntity<>(exBuilder, status);
  }

  protected ResponseEntity<?> handleAppNullPointerException(final AppNullPointerException ex) {
    final HttpStatus status = HttpStatus.BAD_REQUEST;
    final ExceptionBuilder exBuilder = new ExceptionBuilder();
    filterExceptionUtilTraceElements(ex);
    exBuilder
        .withCode(String.valueOf(status))
        .withMessage(NULL_POINTER_MSG)
        .withException(getExceptionName(ex))
        .withCauseMessage(getMessage(ex))
        .withDeclaringClass(getDeclaringClass())
        .withMethodCaller(getMethodCaller())
        .withMethodName(getMethodName())
        .withLineNumber(getLineNumber())
        .build();
    return new ResponseEntity<>(exBuilder, status);
  }

  protected ResponseEntity<?> handleAppIllegalArgumentException(
      final AppIllegalArgumentException ex) {
    final HttpStatus status = HttpStatus.BAD_REQUEST;
    final ExceptionBuilder exBuilder = new ExceptionBuilder();
    filterExceptionUtilTraceElements(ex);
    exBuilder
        .withCode(String.valueOf(status))
        .withMessage(ILLEGAL_ARG_MSG)
        .withException(getExceptionName(ex))
        .withCauseMessage(getMessage(ex))
        .withDeclaringClass(getDeclaringClass())
        .withMethodCaller(getMethodCaller())
        .withMethodName(getMethodName())
        .withLineNumber(getLineNumber())
        .build();
    return new ResponseEntity<>(exBuilder, status);
  }

  protected ResponseEntity<?> handleAppSpecialCaseException(final AppSpecialCaseException ex) {
    final HttpStatus status = HttpStatus.BAD_REQUEST;
    final ExceptionBuilder exBuilder = new ExceptionBuilder();
    filterExceptionUtilTraceElements(ex);
    exBuilder
        .withCode(String.valueOf(status))
        .withMessage(SPECIAL_CASE_MSG)
        .withException(getExceptionName(ex))
        .withCauseMessage(getMessage(ex))
        .withDeclaringClass(getDeclaringClass())
        .withMethodCaller(getMethodCaller())
        .withMethodName(getMethodName())
        .withLineNumber(getLineNumber())
        .build();
    return new ResponseEntity<>(exBuilder, status);
  }

  protected ResponseEntity<Object> handleExceptionInternal(final Exception ex) {
    final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    final ExceptionBuilder exBuilder = new ExceptionBuilder();
    filterExceptionUtilTraceElements(ex);
    exBuilder
        .withCode(String.valueOf(status))
        .withMessage(INTERNAL_SERVER_ERROR_MSG)
        .withException(INTERNAL_SERVER_ERROR_CLASS_NAME)
        .withNestedException(getExceptionName(ex))
        .withCauseMessage(getMessage(ex))
        .withDeclaringClass(getDeclaringClass())
        .withMethodCaller(getMethodCaller())
        .withMethodName(getMethodName())
        .withLineNumber(getLineNumber())
        .build();
    return new ResponseEntity<>(exBuilder, status);
  }

  /**
   * Filters stackTrace elements to:
   *
   * <p>include only project-specific packages &
   *
   * <p>exclude exception classes
   *
   * @param ex
   */
  private void filterExceptionUtilTraceElements(final Exception ex) {
    filteredExUtilElements =
        Arrays.stream(ex.getStackTrace())
            .filter(
                element ->
                    element.getClassName().contains(EX_FQN_FILTER_INCLUDE)
                        && !element.getClassName().contains(EX_FQN_FILTER_EXCLUDE))
            .toArray(StackTraceElement[]::new);
  }

  // Also used for nestedException on 500 error
  private String getExceptionName(final Exception ex) {
    return ex.getClass().getSimpleName();
  }

  private String getMessage(final Exception ex) {
    return ex.getMessage();
  }

  private String getMethodName() {
    if (checkArrayNotEmpty()) {
      return filteredExUtilElements[0].getMethodName();
    }
    return null;
  }

  private String getMethodCaller() {
    if (checkArrayNotEmpty()) {
      return filteredExUtilElements[1].getMethodName();
    }
    return null;
  }

  private String getDeclaringClass() {
    if (checkArrayNotEmpty()) {
      return filteredExUtilElements[0].getClassName();
    }
    return null;
  }

  /**
   * negative numbers indicate line number unavailable; could be library class -2 indicates native
   * method
   */
  private Integer getLineNumber() {
    if (checkArrayNotEmpty()) {
      return filteredExUtilElements[0].getLineNumber();
    }
    return null;
  }

  @Nullable
  private Boolean checkArrayNotEmpty() {
    return filteredExUtilElements.length > 0;
  }
}

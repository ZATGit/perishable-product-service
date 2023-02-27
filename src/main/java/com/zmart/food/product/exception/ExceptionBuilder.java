package com.zmart.food.product.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@NoArgsConstructor
@JsonInclude(NON_NULL)
@JsonPropertyOrder({"code", "message", "exception", "cause"})
public class ExceptionBuilder {
  private final Cause cause = new Cause();
  private String code;
  private String message;
  private String exception;

  public ExceptionBuilder withCode(@Nullable final String errorCode) {
    this.code = errorCode;
    return this;
  }

  public ExceptionBuilder withMessage(@Nullable final String message) {
    this.message = message;
    return this;
  }

  public ExceptionBuilder withException(@Nullable final String exceptionName) {
    this.exception = exceptionName;
    return this;
  }

  public ExceptionBuilder withCauseMessage(@Nullable final String message) {
    cause.message = message;
    return this;
  }

  public ExceptionBuilder withNestedException(@Nullable final String nestedException) {
    cause.nestedException = nestedException;
    return this;
  }

  public ExceptionBuilder withDeclaringClass(@Nullable final String declaringClass) {
    cause.declaringClass = declaringClass;
    return this;
  }

  public ExceptionBuilder withMethodName(@Nullable final String methodName) {
    cause.methodName = methodName;
    return this;
  }

  public ExceptionBuilder withMethodCaller(@Nullable final String methodCaller) {
    cause.methodCaller = methodCaller;
    return this;
  }

  public ExceptionBuilder withLineNumber(@Nullable final Integer lineNumber) {
    cause.lineNumber = lineNumber;
    return this;
  }

  public void build() {
    validate();
  }

  private void validate() {
    if (code == null) {
      this.code = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Getter
  @NoArgsConstructor
  @JsonInclude(NON_NULL)
  class Cause {
    private String nestedException;
    private String message;

    @JsonProperty("class")
    private String declaringClass;

    @JsonProperty("method")
    private String methodName;

    @JsonProperty("caller")
    private String methodCaller;

    @JsonProperty("line")
    private Integer lineNumber;
  }
}

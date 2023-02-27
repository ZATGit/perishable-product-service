package com.zmart.food.product.exception;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

import static com.zmart.food.product.exception.ExceptionUtils.getClassName;

@NoArgsConstructor
@Component
public class AppHttpMessageNotReadableException extends RuntimeException {
  protected static final String NOT_READABLE_ILLEGAL_ARG_MSG =
      "Request body contains illegal argument or value type";
  protected static final String NOT_READABLE_CLASS_NAME =
      getClassName(MethodHandles.lookup().lookupClass());
}

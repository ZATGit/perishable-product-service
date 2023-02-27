package com.zmart.food.product.exception;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

import static com.zmart.food.product.exception.ExceptionUtils.getClassName;

@NoArgsConstructor
@Component
public class AppHttpRequestMethodNotSupportedException extends RuntimeException {
  protected static final String METHOD_NOT_ALLOWED_MSG = "Incorrect HTTP verb";
  protected static final String METHOD_NOT_ALLOWED_CLASS_NAME =
      getClassName(MethodHandles.lookup().lookupClass());
}

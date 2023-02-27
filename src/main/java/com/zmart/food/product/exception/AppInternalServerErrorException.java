package com.zmart.food.product.exception;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

import static com.zmart.food.product.exception.ExceptionUtils.getClassName;

@NoArgsConstructor
@Component
public class AppInternalServerErrorException extends RuntimeException {
  protected static final String INTERNAL_SERVER_ERROR_MSG = "Internal server error";
  protected static final String INTERNAL_SERVER_ERROR_CLASS_NAME =
      getClassName(MethodHandles.lookup().lookupClass());
}

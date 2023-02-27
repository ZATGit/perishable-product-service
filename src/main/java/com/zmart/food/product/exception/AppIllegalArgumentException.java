package com.zmart.food.product.exception;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class AppIllegalArgumentException extends RuntimeException {
  protected static final String ILLEGAL_ARG_MSG =
      "Request body contains illegal argument or value type";
}

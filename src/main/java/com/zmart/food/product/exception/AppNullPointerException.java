package com.zmart.food.product.exception;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class AppNullPointerException extends RuntimeException {
  protected static final String NULL_POINTER_MSG = "Request body contains null key or value";
}

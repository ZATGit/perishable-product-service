package com.zmart.food.product.cache;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})
public @interface NoCache {
  String reason();
}

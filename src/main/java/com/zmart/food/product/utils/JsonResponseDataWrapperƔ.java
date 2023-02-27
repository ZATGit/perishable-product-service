package com.zmart.food.product.utils;

import java.lang.annotation.*;

import static com.zmart.food.product.utils.UtilConstants.DATA_STRING;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface JsonResponseDataWrapperƔ {
  String value() default DATA_STRING;
}

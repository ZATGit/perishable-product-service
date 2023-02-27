package com.zmart.food.product.cache;

import org.springframework.cache.annotation.CacheEvict;

import java.lang.annotation.*;

import static com.zmart.food.product.utils.UtilConstants.PRODUCTS_STRING;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})
@CacheEvict(value = PRODUCTS_STRING, allEntries = true)
public @interface CacheEvictCreateOrUpdate {}

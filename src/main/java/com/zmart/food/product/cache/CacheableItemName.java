package com.zmart.food.product.cache;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})
@Cacheable(
    key =
        "{#root.targetClass.canonicalName.substring("
            + "#root.targetClass.canonicalName.lastIndexOf('.') + 1),"
            + "#root.methodName, "
            + "#request.itemName}",
    sync = true)
public @interface CacheableItemName {}

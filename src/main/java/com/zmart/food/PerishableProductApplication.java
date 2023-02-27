package com.zmart.food;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.SpringVersion;

@Slf4j
@SpringBootApplication
@EnableCaching
public class PerishableProductApplication {
  public static void main(final String[] args) {
    SpringApplication.run(PerishableProductApplication.class, args);
    log.info("Spring Version: ".concat(SpringVersion.getVersion()));
  }
}

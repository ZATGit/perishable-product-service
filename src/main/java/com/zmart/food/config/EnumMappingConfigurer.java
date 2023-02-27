package com.zmart.food.config;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EnumMappingConfigurer implements WebMvcConfigurer {
  @Override
  public void addFormatters(final FormatterRegistry registry) {
    ApplicationConversionService.configure(registry);
  }
}

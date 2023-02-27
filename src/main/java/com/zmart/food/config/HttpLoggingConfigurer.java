package com.zmart.food.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class HttpLoggingConfigurer implements WebMvcConfigurer {

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(new LoggerInterceptor());
  }
}

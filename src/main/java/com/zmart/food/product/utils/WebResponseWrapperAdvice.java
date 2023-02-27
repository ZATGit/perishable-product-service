package com.zmart.food.product.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class WebResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(
      final MethodParameter returnType,
      final Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      final Object responseBody,
      final MethodParameter returnType,
      final MediaType selectedContentType,
      final Class<? extends HttpMessageConverter<?>> selectedConverterType,
      final ServerHttpRequest request,
      final ServerHttpResponse response) {

    if (selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
      return mapJsonResponseDataWrapper(responseBody, returnType);
    } else {
      return responseBody;
    }
  }

  /**
   * Checks for use of {@link JsonResponseDataWrapperƔ} in a response and wraps if needed.
   *
   * @param responseBody responseBody to be written in response
   * @param returnType controller method return type
   * @return
   */
  private Object mapJsonResponseDataWrapper(
      final Object responseBody, final MethodParameter returnType) {
    final Map<String, Object> responseMap = new HashMap<>(1);
    final JsonResponseDataWrapperƔ methodResponseWrapper =
        AnnotationUtils.findAnnotation(returnType.getMethod(), JsonResponseDataWrapperƔ.class);
    try {
      responseMap.put(methodResponseWrapper.value(), responseBody);
      log.trace(
          "Wrapped web response for controller method "
              + returnType.getMethod().getName()
              + "() with data{}");
      return responseMap;
    } catch (final RuntimeException ex) {
      return responseBody;
    }
  }
}

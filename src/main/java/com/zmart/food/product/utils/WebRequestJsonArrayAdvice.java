package com.zmart.food.product.utils;

import com.zmart.food.product.exception.AppNullPointerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

import static com.zmart.food.product.utils.UtilConstants.PRODUCTS_STRING;

@Slf4j
@ControllerAdvice
public class WebRequestJsonArrayAdvice implements RequestBodyAdvice {

  @Override
  public boolean supports(
      final MethodParameter methodParameter,
      final Type targetType,
      final Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public HttpInputMessage beforeBodyRead(
      final HttpInputMessage inputMessage,
      final MethodParameter parameter,
      final Type targetType,
      final Class<? extends HttpMessageConverter<?>> converterType)
      throws IOException {
    return inputMessage;
  }

  @Override
  public Object afterBodyRead(
      final Object requestBody,
      final HttpInputMessage inputMessage,
      final MethodParameter parameter,
      final Type targetType,
      final Class<? extends HttpMessageConverter<?>> converterType) {
    try {
      return checkProductsArrayNameUsedInRequestBody((LinkedHashMap) requestBody);
      // Catches simple non-HashMap JSON body
    } catch (final ClassCastException ex) {
      return requestBody;
    }
  }

  @Override
  public Object handleEmptyBody(
      final Object body,
      final HttpInputMessage inputMessage,
      final MethodParameter parameter,
      final Type targetType,
      final Class<? extends HttpMessageConverter<?>> converterType) {
    return body;
  }

  /**
   * Checks for malformed Json Array name.
   *
   * @param requestBody
   * @return requestBody
   */
  private Object checkProductsArrayNameUsedInRequestBody(final LinkedHashMap requestBody) {
    if (!((requestBody.get(PRODUCTS_STRING)) instanceof Object)) {
      throw new AppNullPointerException();
    }
    return requestBody;
  }
}

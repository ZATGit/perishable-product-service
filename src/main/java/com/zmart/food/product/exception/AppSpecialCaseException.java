package com.zmart.food.product.exception;

import com.zmart.food.product.utils.SpecialCaseProductsEnum;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class AppSpecialCaseException extends RuntimeException {
  protected static final String SPECIAL_CASE_MSG =
      "Illegal special case number for one or more provided products. "
          + "Available options: 0-"
          + SpecialCaseProductsEnum.values().length;
}

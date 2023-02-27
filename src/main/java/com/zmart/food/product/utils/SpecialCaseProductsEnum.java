package com.zmart.food.product.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SpecialCaseProductsEnum {
  SPECIAL_CASE_1(
      1,
      "Quality increments daily by 1 as the sell by date "
          + "approaches 0 and continues to increase in quality."),
  SPECIAL_CASE_2(2, "Never needs to be sold." + "Never decreases in quality."),
  SPECIAL_CASE_3(
      3,
      "As sell by date approaches, quality increments by 1.\n"
          + "Quality increments by 2 when 10 days or less.\n"
          + "The quality drops to 0 after the sell by date.\n");

  private Integer caseNumber;
  private String caseDescription;
}

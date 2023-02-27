package com.zmart.food.product.model.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductByQualityRequest {
  private Integer quality;
  private String orderByAttribute;
  private String orderByAscOrDesc;
}

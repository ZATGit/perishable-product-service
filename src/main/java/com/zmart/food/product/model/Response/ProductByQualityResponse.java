package com.zmart.food.product.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zmart.food.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import java.io.Serializable;
import java.util.List;

import static com.zmart.food.product.utils.UtilConstants.PRODUCTS_STRING;

@With
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductByQualityResponse implements Serializable {
  private Integer count;

  @JsonProperty(PRODUCTS_STRING)
  private List<Product> productList;
}

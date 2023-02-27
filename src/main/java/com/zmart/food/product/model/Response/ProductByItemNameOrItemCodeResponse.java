package com.zmart.food.product.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zmart.food.product.model.Product;
import lombok.*;

import java.io.Serializable;
import java.util.List;

import static com.zmart.food.product.utils.UtilConstants.PRODUCTS_STRING;

@With
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductByItemNameOrItemCodeResponse implements Serializable {
  @JsonProperty(PRODUCTS_STRING)
  List<Product> productList;

  private Integer count;
}

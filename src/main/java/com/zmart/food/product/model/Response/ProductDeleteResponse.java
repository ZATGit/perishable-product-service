package com.zmart.food.product.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zmart.food.product.repository.ProductProjectionIdsAndNamesOnly;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Collection;

@With
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDeleteResponse {
  private Integer count;

  @JsonProperty("deleted")
  private Collection<ProductProjectionIdsAndNamesOnly> deletedProducts;
}

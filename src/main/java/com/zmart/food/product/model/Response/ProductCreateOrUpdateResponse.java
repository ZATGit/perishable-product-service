package com.zmart.food.product.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zmart.food.product.model.Product;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@With
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateOrUpdateResponse implements Serializable {
  private Integer count;

  @JsonProperty("modified")
  private List<Product> productList;
}

package com.zmart.food.product.model.Request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDeleteRequest {
  @JsonAlias({"ids", "IDs"})
  private List<Long> idList;
}

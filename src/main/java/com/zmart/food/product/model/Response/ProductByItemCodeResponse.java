package com.zmart.food.product.model.Response;

import com.zmart.food.product.model.Product;
import lombok.*;

import java.io.Serializable;

@With
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductByItemCodeResponse implements Serializable {
  private Product product;
}

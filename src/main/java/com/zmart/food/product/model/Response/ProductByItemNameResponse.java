package com.zmart.food.product.model.Response;

import com.zmart.food.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import java.io.Serializable;

@With
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductByItemNameResponse implements Serializable {
  private Product product;
}

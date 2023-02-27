package com.zmart.food.product.model.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductByItemNameOrItemCodeRequest implements Serializable {
  private String itemName;
  private String itemCode;
}

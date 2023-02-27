package com.zmart.food.product.service;

import com.zmart.food.product.model.Request.*;
import com.zmart.food.product.model.Response.*;

public interface PerishableProductInventoryService {
  /**
   * @description Get All items.
   * @return
   */
  ProductAllProductsResponse getAllProductsInventory();
  /**
   * @description Get all items and their quality after ${x} days.
   * @param day
   * @return
   */
  ProductFutureDateResponse getAllProductsForFutureDateInventory(ProductFutureDateRequest request);

  /**
   * @description Get All items quality.
   * @param quality
   * @return
   */
  ProductByQualityResponse getAllProductsByQualityInventory(ProductByQualityRequest request);

  /**
   * @description Get item by its item name.
   * @param itemName
   * @return
   */
  ProductByItemNameResponse getProductByItemNameInventory(ProductByItemNameRequest request);

  /**
   * @return
   */
  ProductByItemCodeResponse getProductByItemCodeInventory(ProductByItemCodeRequest request);

  /**
   * @param itemName
   * @param itemCode
   * @return
   */
  ProductByItemNameOrItemCodeResponse getProductByItemNameOrItemCodeInventory(
      ProductByItemNameOrItemCodeRequest request);

  /**
   * @param productList
   * @return
   */
  ProductCreateOrUpdateResponse createOrUpdateProductsInventory(
      ProductCreateOrUpdateRequest request);

  /**
   * @param productList
   * @return
   */
  ProductDeleteResponse deleteProductsInventory(ProductDeleteRequest request);
}

package com.zmart.food.product.controller;

import com.zmart.food.product.model.Request.*;
import com.zmart.food.product.service.PerishableProductInventoryService;
import com.zmart.food.product.utils.JsonResponseDataWrapperƔ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.zmart.food.product.utils.UtilConstants.API_VERSION;
import static com.zmart.food.product.utils.UtilConstants.STORE_API_ENDPOINT;

@Slf4j
@RestController
@RequestMapping(
    path = STORE_API_ENDPOINT,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE + API_VERSION)
public class ProductRestController {

  private final PerishableProductInventoryService perishableProductInventoryService;

  public ProductRestController(
      final PerishableProductInventoryService perishableProductInventoryService) {
    this.perishableProductInventoryService = perishableProductInventoryService;
  }

  @PostMapping
  @JsonResponseDataWrapperƔ
  public ResponseEntity<?> getAllProducts() {
    log.debug("Get all products");
    return new ResponseEntity<>(
        perishableProductInventoryService.getAllProductsInventory(), HttpStatus.OK);
  }

  @PostMapping("/future/day")
  @JsonResponseDataWrapperƔ
  public ResponseEntity<?> getAllProductsForFutureDate(
      @RequestBody final ProductFutureDateRequest request) {
    log.debug("Get all products based on provided day(s) in the future");
    return new ResponseEntity<>(
        perishableProductInventoryService.getAllProductsForFutureDateInventory(request),
        HttpStatus.OK);
  }

  @PostMapping("/itemname")
  @JsonResponseDataWrapperƔ
  public ResponseEntity<?> getProductByItemName(
      @RequestBody final ProductByItemNameRequest request) {
    log.debug("Get all product items by item name");
    return new ResponseEntity<>(
        perishableProductInventoryService.getProductByItemNameInventory(request), HttpStatus.OK);
  }

  @PostMapping("/itemcode")
  @JsonResponseDataWrapperƔ
  public ResponseEntity<?> getProductByItemCode(
      @RequestBody final ProductByItemCodeRequest request) {
    log.debug("Get all products by item code");
    return new ResponseEntity<>(
        perishableProductInventoryService.getProductByItemCodeInventory(request), HttpStatus.OK);
  }

  @PostMapping("/itemname-or-itemcode")
  @JsonResponseDataWrapperƔ
  public ResponseEntity<?> getProductByItemNameOrItemCode(
      @RequestBody final ProductByItemNameOrItemCodeRequest request) {
    log.debug("Get all products by item name or item code");
    return new ResponseEntity<>(
        perishableProductInventoryService.getProductByItemNameOrItemCodeInventory(request),
        HttpStatus.OK);
  }

  @PostMapping("/quality")
  @JsonResponseDataWrapperƔ
  public ResponseEntity<?> getAllProductsByQuality(
      @RequestBody final ProductByQualityRequest productQualityRequest) {
    log.debug("Get all products filtered by quality");
    return new ResponseEntity<>(
        perishableProductInventoryService.getAllProductsByQualityInventory(productQualityRequest),
        HttpStatus.OK);
  }

  @PutMapping({"/create-or-update", "/create", "/update"})
  @JsonResponseDataWrapperƔ
  public ResponseEntity<?> createOrUpdateProducts(
      @RequestBody final ProductCreateOrUpdateRequest productCreateOrUpdateRequest) {
    log.debug("Create or update product(s)");
    return new ResponseEntity<>(
        perishableProductInventoryService.createOrUpdateProductsInventory(
            productCreateOrUpdateRequest),
        HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  @JsonResponseDataWrapperƔ
  public ResponseEntity<?> deleteProducts(
      @RequestBody final ProductDeleteRequest productDeleteRequest) {
    log.debug("Delete product(s)");
    return new ResponseEntity<>(
        perishableProductInventoryService.deleteProductsInventory(productDeleteRequest),
        HttpStatus.OK);
  }
}

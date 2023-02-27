package com.zmart.food.product.service;

import com.zmart.food.app.PerishableProductApplicationProduct;
import com.zmart.food.product.model.Product;
import com.zmart.food.product.model.Request.*;
import com.zmart.food.product.model.Response.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PerishableProductServiceImplProductTest extends PerishableProductApplicationProduct {

  @Mock private PerishableProductInventoryService perishableProductInventoryService;

  @BeforeAll
  public static void setUpBeforeClass() throws Exception {}

  @BeforeEach
  public void setUp() throws Exception {}

  @AfterEach
  public void tearDown() throws Exception {}

  @Test
  public void testMockCreation() {
    assertNotNull(perishableProductInventoryService);
  }

  @Test
  public void testGetAllProductsInventory() {
    when(perishableProductInventoryService.getAllProductsInventory())
        .thenReturn(new ProductAllProductsResponse().withProductList(MOCK_PRODUCT_LIST));
    assertTrue(
        perishableProductInventoryService.getAllProductsInventory().getProductList().size() > 1);
    verify(perishableProductInventoryService, times(1)).getAllProductsInventory();
  }

  @Test
  public void testGetAllProductsForFutureDateInventory() {
    final ProductFutureDateRequest request = new ProductFutureDateRequest();
    when(perishableProductInventoryService.getAllProductsForFutureDateInventory(request))
        .thenReturn(
            new ProductFutureDateResponse().withProductList(MOCK_PRODUCT_LIST_OFFSET_50_DAYS));
    assertTrue(
        perishableProductInventoryService
            .getAllProductsForFutureDateInventory(request)
            .getProductList()
            .get(3)
            .getQuality()
            .equals(80));
    verify(perishableProductInventoryService, times(1))
        .getAllProductsForFutureDateInventory(request);
  }

  @Test
  public void testGetProductByItemNameInventory() {
    final String appleGranItemCode = "ApplesGran";
    final Integer applesGranIndex = 2;
    final ProductByItemNameRequest request = new ProductByItemNameRequest();
    when(perishableProductInventoryService.getProductByItemNameInventory(request))
        .thenReturn(new ProductByItemNameResponse(MOCK_PRODUCT_LIST.get(applesGranIndex)));
    assertAll(
        () ->
            assertTrue(
                perishableProductInventoryService
                    .getProductByItemNameInventory(request)
                    .getProduct()
                    .getQuality()
                    .equals(7)),
        () ->
            assertTrue(
                perishableProductInventoryService
                    .getProductByItemNameInventory(request)
                    .getProduct()
                    .getItemCode()
                    .equals(appleGranItemCode)));
    verify(perishableProductInventoryService, times(2)).getProductByItemNameInventory(request);
  }

  @Test
  public void testGetProductByItemCodeInventory() {
    final String appleGranItemName = "Grannysmith Apple";
    final Integer applesGranIndex = 2;
    final ProductByItemCodeRequest request = new ProductByItemCodeRequest();
    when(perishableProductInventoryService.getProductByItemCodeInventory(request))
        .thenReturn(new ProductByItemCodeResponse(MOCK_PRODUCT_LIST.get(applesGranIndex)));
    assertAll(
        () ->
            assertTrue(
                perishableProductInventoryService
                    .getProductByItemCodeInventory(request)
                    .getProduct()
                    .getQuality()
                    .equals(7)),
        () ->
            assertTrue(
                perishableProductInventoryService
                    .getProductByItemCodeInventory(request)
                    .getProduct()
                    .getItemName()
                    .equals(appleGranItemName)));
    verify(perishableProductInventoryService, times(2)).getProductByItemCodeInventory(request);
  }

  @Test
  public void testGetProductByItemNameOrItemCodeInventory() {
    final String appleGranItemName = "ApplesGran";
    final Integer applesGranIndex = 2;
    final Integer applesGranQuality = 7;
    final String orgSpinItemName = "Farm2Table Organic Spinach";
    final Integer orgSpinIndex = 0;
    final Integer orgSpinQuality = 20;
    final List<Product> productList =
        List.of(MOCK_PRODUCT_LIST.get(orgSpinIndex), MOCK_PRODUCT_LIST.get(applesGranIndex));
    final ProductByItemNameOrItemCodeRequest request = new ProductByItemNameOrItemCodeRequest();
    when(perishableProductInventoryService.getProductByItemNameOrItemCodeInventory(request))
        .thenReturn(new ProductByItemNameOrItemCodeResponse().withProductList(productList));
    assertAll(
        () ->
            assertTrue(
                perishableProductInventoryService
                    .getProductByItemNameOrItemCodeInventory(request)
                    .getProductList()
                    .get(0)
                    .getQuality()
                    .equals(orgSpinQuality)),
        () ->
            perishableProductInventoryService
                .getProductByItemNameOrItemCodeInventory(request)
                .getProductList()
                .get(1)
                .getQuality()
                .equals(applesGranQuality),
        () ->
            perishableProductInventoryService
                .getProductByItemNameOrItemCodeInventory(request)
                .getProductList()
                .get(0)
                .getItemName()
                .equals(orgSpinItemName),
        () ->
            perishableProductInventoryService
                .getProductByItemNameOrItemCodeInventory(request)
                .getProductList()
                .get(1)
                .getItemName()
                .equals(appleGranItemName));
    verify(perishableProductInventoryService, times(4))
        .getProductByItemNameOrItemCodeInventory(request);
  }

  @Test
  public void testGetAllProductsByQualityInventory() {
    final Integer quality = 20;
    final String orderByAttribute = "sellBy";
    final String orderByAscOrDesc = "asc";
    final ProductByQualityRequest productQualityRequest =
        new ProductByQualityRequest(quality, orderByAttribute, orderByAscOrDesc);
    when(perishableProductInventoryService.getAllProductsByQualityInventory(productQualityRequest))
        .thenReturn(new ProductByQualityResponse().withProductList(getQualityOfTwenty(quality)));
    assertTrue(
        perishableProductInventoryService
                .getAllProductsByQualityInventory(productQualityRequest)
                .getProductList()
                .size()
            == 2);
    verify(perishableProductInventoryService, times(1))
        .getAllProductsByQualityInventory(productQualityRequest);
  }

  // UTILITY
  private List<Product> getQualityOfTwenty(final Integer quality) {
    return MOCK_PRODUCT_LIST.stream()
        .filter(p -> p.getQuality().equals(quality))
        .collect(Collectors.toList());
  }
}

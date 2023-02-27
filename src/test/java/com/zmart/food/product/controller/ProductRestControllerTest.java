package com.zmart.food.product.controller;

import com.zmart.food.app.PerishableProductApplicationProduct;
import com.zmart.food.product.model.Product;
import com.zmart.food.product.model.Request.*;
import com.zmart.food.product.model.Response.*;
import com.zmart.food.product.service.PerishableProductInventoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProductRestControllerTest extends PerishableProductApplicationProduct {

  private final String itemNameSpinach = "Farm2Table Organic Spinach";
  private final String itemCodeSpinach = "OrgSpinach";
  @Mock private MockHttpServletRequest mockHttpServletRequest;
  @Mock private PerishableProductInventoryService perishableProductService;
  @InjectMocks private ProductRestController perishableProductController;

  // UTILITY
  private static List<Product> getQualityArrayListWithSeven(final Integer quality) {
    return (MOCK_PRODUCT_LIST.stream().filter(p -> p.getQuality().equals(quality)))
        .collect(Collectors.toList());
  }

  @BeforeEach
  public void setup() {
    final HttpServletRequest mockRequest = new MockHttpServletRequest();
    final ServletRequestAttributes servletRequestAttributes =
        new ServletRequestAttributes(mockRequest);
    RequestContextHolder.setRequestAttributes(servletRequestAttributes);
  }

  @AfterEach
  public void teardown() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  public void testPerishableProductController() {
    assertAll(
        () -> assertNotNull(mockHttpServletRequest),
        () -> assertNotNull(perishableProductService),
        () -> assertNotNull(perishableProductController));
  }

  @Test
  public void testGetAllInventory() {
    when(perishableProductService.getAllProductsInventory())
        .thenReturn(new ProductAllProductsResponse().withProductList(MOCK_PRODUCT_LIST));
    final ResponseEntity<?> results = perishableProductController.getAllProducts();
    ProductAllProductsResponse resultsBody = (ProductAllProductsResponse) results.getBody();
    assertAll(
        () -> assertTrue((resultsBody.getProductList()).size() == 5, "Found values"),
        () -> assertEquals(HttpStatus.OK, results.getStatusCode()));
  }

  @Test
  public void testGetAllProductsForFutureDate() {
    final ProductFutureDateRequest request = new ProductFutureDateRequest();
    final Integer dayOffset = 50;
    final String itemName = "CornCob";
    when(perishableProductService.getAllProductsForFutureDateInventory(request))
        .thenReturn(
            new ProductFutureDateResponse().withProductList(MOCK_PRODUCT_LIST_OFFSET_50_DAYS));
    final ResponseEntity<?> results =
        perishableProductController.getAllProductsForFutureDate(request);
    final ProductFutureDateResponse resultsBody = (ProductFutureDateResponse) results.getBody();
    assertAll(
        () ->
            assertTrue(
                resultsBody.getProductList().get(1).getItemCode().equals(itemName), "Found "),
        () -> assertTrue(resultsBody.getProductList().get(1).getQuality().equals(dayOffset)),
        () -> assertEquals(HttpStatus.OK, results.getStatusCode(), "Found "));
  }

  @Test
  public void testGetProductByItemName() {
    final ProductByItemNameRequest request = new ProductByItemNameRequest(itemNameSpinach);
    when(perishableProductService.getProductByItemNameInventory(request))
        .thenReturn(new ProductByItemNameResponse(MOCK_PRODUCT_LIST.get(0)));
    final ResponseEntity<?> results = perishableProductController.getProductByItemName(request);
    final ProductByItemNameResponse resultsBody = (ProductByItemNameResponse) results.getBody();
    assertAll(
        () -> assertTrue(resultsBody.getProduct().getItemName().equals(itemNameSpinach), "Found "),
        () -> assertTrue(resultsBody.getProduct().getQuality().equals(20), "Found "),
        () -> assertEquals(HttpStatus.OK, results.getStatusCode()));
  }

  @Test
  public void testGetProductByItemCode() {
    final ProductByItemCodeRequest request = new ProductByItemCodeRequest(itemCodeSpinach);
    when(perishableProductService.getProductByItemCodeInventory(request))
        .thenReturn(new ProductByItemCodeResponse(MOCK_PRODUCT_LIST.get(0)));
    final ResponseEntity<?> results = perishableProductController.getProductByItemCode(request);
    final ProductByItemCodeResponse resultsBody = (ProductByItemCodeResponse) results.getBody();
    assertAll(
        () -> assertTrue(resultsBody.getProduct().getItemCode().equals(itemCodeSpinach), "Found "),
        () -> assertTrue(resultsBody.getProduct().getQuality().equals(20), "Found "),
        () -> assertEquals(HttpStatus.OK, results.getStatusCode()));
  }

  @Test
  public void testGetAllInventoryByQuality() {
    final ProductByQualityRequest request = new ProductByQualityRequest();
    final Integer quality = 7;
    when(perishableProductService.getAllProductsByQualityInventory(request))
        .thenReturn(
            new ProductByQualityResponse().withProductList(getQualityArrayListWithSeven(quality)));
    final ResponseEntity<?> results = perishableProductController.getAllProductsByQuality(request);
    final ProductByQualityResponse resultsBody = (ProductByQualityResponse) results.getBody();
    assertAll(
        () -> assertTrue(resultsBody.getProductList().size() == 1, "Found "),
        () -> assertTrue(resultsBody.getProductList().get(0).getQuality().equals(quality)),
        () -> assertEquals(HttpStatus.OK, results.getStatusCode()));
  }

  @Test
  public void testGetItemByItemNameOrItemCode() {
    final String itemCodeApples = "CornCob";
    final ProductByItemNameOrItemCodeRequest request =
        new ProductByItemNameOrItemCodeRequest(itemNameSpinach, itemCodeApples);
    when(perishableProductService.getProductByItemNameOrItemCodeInventory(request))
        .thenReturn(
            new ProductByItemNameOrItemCodeResponse()
                .withProductList(MOCK_PRODUCT_LIST.subList(0, 2)));
    final ResponseEntity<?> results =
        perishableProductController.getProductByItemNameOrItemCode(request);
    final ProductByItemNameOrItemCodeResponse resultsBody =
        (ProductByItemNameOrItemCodeResponse) results.getBody();
    final List<Product> productList = resultsBody.getProductList();
    assertAll(
        () -> assertTrue((productList).get(0).getItemName().contains(itemNameSpinach), "Found "),
        () -> assertTrue((productList).get(1).getItemCode().contains(itemCodeApples), "Found "),
        () -> assertEquals(HttpStatus.OK, results.getStatusCode()));
  }
}

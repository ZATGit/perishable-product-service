package com.zmart.food.product.controller;

import com.zmart.food.PerishableProductApplication;
import com.zmart.food.app.PerishableProductApplicationProduct;
import com.zmart.food.product.service.PerishableProductInventoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.zmart.food.product.utils.UtilConstants.STORE_API_ENDPOINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PerishableProductApplication.class)
public class ProductRestControllerIntegration extends PerishableProductApplicationProduct {

  private final ProductRestController productRestController;
  private final WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  @Mock private PerishableProductInventoryService perishableProductInventoryService;

  // Autowire mock perishableProductInventoryService instead of @InjectMocks
  @Autowired
  public ProductRestControllerIntegration(
      final WebApplicationContext webApplicationContext,
      final ProductRestController productRestController) {
    this.productRestController = productRestController;
    this.webApplicationContext = webApplicationContext;
  }

  @BeforeEach
  public void init() {
    Mockito.mock(ProductRestControllerIntegration.class);
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @AfterEach
  public void tearDown() {}

  @Test
  public void testAllProductItems() throws Exception {
    assertThat(this.perishableProductInventoryService).isNotNull();
    mockMvc
        .perform(post(STORE_API_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void testAllCurrentItemFutureValueInventory() throws Exception {
    final Integer futureDayOffset = 180;
    assertThat(this.perishableProductInventoryService).isNotNull();
    mockMvc
        .perform(
            post(STORE_API_ENDPOINT.concat("/future/day/").concat(futureDayOffset.toString()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[3].itemCode", is("Twinkies")))
        .andExpect(jsonPath("$.[3].quality", is(80)))
        .andDo(print());
  }

  @Test
  public void testIndividualProductItemItemCode() throws Exception {
    final String itemName = "Twinkies";
    assertThat(this.perishableProductInventoryService).isNotNull();
    mockMvc
        .perform(
            post(STORE_API_ENDPOINT.concat("/itemcode/").concat(itemName))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.quality", is(80)))
        .andDo(print());
  }

  @Test
  public void testIndividualProductItemQuality() throws Exception {
    final Integer quality = 80;
    assertThat(this.perishableProductInventoryService).isNotNull();
    mockMvc
        .perform(
            post(STORE_API_ENDPOINT.concat("/quality/").concat(quality.toString()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].sellBy", is((0))))
        .andExpect(jsonPath("$.[0].quality", is(quality)))
        .andExpect(jsonPath("$.[0].itemName", is("Twinkies")))
        .andExpect(jsonPath("$.[0].itemCode", is("Twinkies")))
        .andDo(print());
  }
}

package com.zmart.food.app;

import com.zmart.food.product.model.Product;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public interface MockProductList {

  List<Product> MOCK_PRODUCT_LIST =
      new ArrayList<>() {
        {
          add(
              new Product(
                  new Date(1L),
                  new Date(1L),
                  "Farm2Table Organic Spinach",
                  "OrgSpinach",
                  10,
                  20,
                  0));
          add(new Product(new Date(1L), new Date(1L), "Corn on the cob", "CornCob", 2, 0, 1));
          add(new Product(new Date(1L), new Date(1L), "Grannysmith Apple", "ApplesGran", 5, 7, 0));
          add(new Product(new Date(1L), new Date(1L), "Twinkies", "Twinkies", 0, 80, 3));
          add(new Product(new Date(1L), new Date(1L), "3lb Ground Beef", "3lbGrBeed", 15, 20, 2));
        }
      };

  /**
   * assumption is included Edge cases in 51 days
   * ----------------------------------------------------- +5 Farm2Table Organic Spinach 10 20 0
   * Grannysmith Apple 5 7 0 ------------------------------------------------------ Corn on the cob
   * 2 0 0+50 50 //The quality of an item is never more than 50 Twinkies 0 80 80 3lb Ground Beef
   * ==>20+5+(2*10) 0
   */
  List<Product> MOCK_PRODUCT_LIST_OFFSET_50_DAYS =
      new ArrayList<>() {
        {
          add(
              new Product(
                  new Date(1L),
                  new Date(50L),
                  "Farm2Table Organic Spinach",
                  "OrgSpinach",
                  -40,
                  0,
                  0));
          add(new Product(new Date(1L), new Date(50L), "Corn on the cob", "CornCob", -48, 50, 1));
          add(
              new Product(
                  new Date(1L), new Date(50L), "Grannysmith Apple", "ApplesGran", -45, 0, 0));
          add(new Product(new Date(1L), new Date(50L), "Twinkies", "Twinkies", 0, 80, 2));
          add(new Product(new Date(1L), new Date(50L), "3lb Ground Beef", "3lbGrBeed", -35, 0, 3));
        }
      };
}

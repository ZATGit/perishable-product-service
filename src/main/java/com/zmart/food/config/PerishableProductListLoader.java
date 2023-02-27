package com.zmart.food.config;

import com.zmart.food.product.model.Product;
import com.zmart.food.product.repository.PerishableProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component()
public class PerishableProductListLoader implements CommandLineRunner {

  private final PerishableProductRepository perishableProductRepository;

  @Autowired
  public PerishableProductListLoader(
      final PerishableProductRepository perishableProductRepository) {
    this.perishableProductRepository = perishableProductRepository;
  }

  @Override
  public void run(final String... args) throws Exception {
    this.perishableProductRepository.save(
        new Product(new Date(), new Date(), "Farm2Table Organic Spinach", "OrgSpinach", 10, 20, 0));
    this.perishableProductRepository.save(
        new Product(new Date(), new Date(), "Corn on the cob", "CornCob", 2, 0, 1));
    this.perishableProductRepository.save(
        new Product(new Date(), new Date(), "Grannysmith Apple", "ApplesGran", 5, 7, 0));
    this.perishableProductRepository.save(
        new Product(new Date(), new Date(), "Twinkies", "Twinkies", 0, 80, 3));
    this.perishableProductRepository.save(
        new Product(new Date(), new Date(), "3lb Ground Beef", "3lbGrBeed", 15, 20, 2));
    this.perishableProductRepository.save(
        new Product(new Date(), new Date(), "Moonberries", "MoonBerr", 15, 20, 0));
  }
}
